package jsz.dk.signmanagement.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jsz.dk.signmanagement.common.entity.CacheKey;
import jsz.dk.signmanagement.common.entity.Consts;
import jsz.dk.signmanagement.common.entity.CustomException;
import jsz.dk.signmanagement.entity.*;
import jsz.dk.signmanagement.entity.dto.GoogleDTO;
import jsz.dk.signmanagement.entity.dto.UserDTO;
import jsz.dk.signmanagement.entity.vo.GoogleSecretVO;
import jsz.dk.signmanagement.entity.vo.UserLoginVO;
import jsz.dk.signmanagement.enums.CacheEnum;
import jsz.dk.signmanagement.enums.ResponseCode;
import jsz.dk.signmanagement.mapper.UserMapper;
import jsz.dk.signmanagement.services.UserService;
import jsz.dk.signmanagement.utils.GoogleAuthenticator;
import jsz.dk.signmanagement.utils.QRCodeUtil;
import jsz.dk.signmanagement.utils.RedisUtil;
import jsz.dk.signmanagement.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.lrshuai.encryption.MDUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.services.impl
 * @ClassName: UserServiceImpl
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 18:15
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final static String TAG = "[UserService]";
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 获取缓存中的数据
     */
    private Map<String,Object> getData(){
        Map<String,Object> data = new HashMap<>();
        setData(CacheKey.REGISTER_USER_KEY,data);
        setData(CacheKey.TOKEN_KEY_LOGIN_KEY,data);
        return data;
    }

    private void setData(String keyword,Map<String,Object> data){
        Set<String> keys = redisUtil.keys(keyword);
        for (String key : keys) {
            data.put(key, redisUtil.get(key));
        }
    }

    private User getUser(String username){
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    @Transactional
    public User registry(UserDTO dto) throws CustomException{
        if (getUser(dto.getUsername()) != null) {
            throw new CustomException(TAG, "用户已存在");
        }
        try {
            dto.setPassword(MDUtil.bcMD5(dto.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(TAG, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
        User user = new User();
        BeanCopier copier = BeanCopier.create(UserDTO.class, User.class, false);
        copier.copy(dto, user,null);
        userMapper.insert(user);
        return userMapper.selectById(user.getId());
    }

    @Override
    public GoogleSecretVO generateGoogleSecret(User user) {
        //Google密钥
        String randomSecretKey = GoogleAuthenticator.getRandomSecretKey();
        String googleAuthenticatorBarCode = GoogleAuthenticator.getGoogleAuthenticatorBarCode(randomSecretKey, user.getUsername(), "运维管理平台");
        GoogleSecretVO googleSecretVO = new GoogleSecretVO();
        //Google密钥
        googleSecretVO.setSecret(randomSecretKey);
        //用户二维码内容
        googleSecretVO.setSecretQrCode(googleAuthenticatorBarCode);
        return googleSecretVO;
    }

    @Override
    public UserLoginVO login(UserDTO dto) throws CustomException {
        User user = getUser(dto.getUsername());
        if(user == null){
            throw new CustomException(TAG, "用户名或密码错误");
        }
        String md5Pass;
        try {
             md5Pass = MDUtil.bcMD5(dto.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(TAG, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
        if(!user.getPassword().equals(md5Pass)){
            throw new CustomException(TAG, "用户名或密码错误");
        }
        //检查该用户已存在token并删除，注释掉可实现多机登陆
        Map<String ,Object> tokens = getData();
        tokens.forEach((key,value)->{
            JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
            if (jsonObject.get("username").equals(dto.getUsername())) {
                redisUtil.delete(key);
            }
        });
        //随机生成token
        String token = Tools.getUUID();
        redisUtil.setEx(String.format(CacheKey.TOKEN_KEY_LOGIN,token),user,1, TimeUnit.DAYS);
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setUsername(dto.getUsername());
        userLoginVO.setToken(token);
        userLoginVO.setTs(System.currentTimeMillis());
        return userLoginVO;
    }

    @Override
    public void genQrCode(String secretQrCode, HttpServletResponse response) throws CustomException {
        try {
            response.setContentType("image/png");
            OutputStream stream = response.getOutputStream();
            QRCodeUtil.encode(secretQrCode, stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(TAG, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
        }
    }

    @Override
    @Transactional
    public void bindGoogle(GoogleDTO dto, User user, HttpServletRequest request) throws CustomException{
        if (!StringUtils.isEmpty(user.getGoogleSecret())) {
            throw new CustomException(TAG, "该账号已绑定其他Google密钥");
        }
        if(!GoogleAuthenticator.check_code(dto.getSecret(), dto.getCode(), System.currentTimeMillis())){
            throw new CustomException(TAG, "Google验证码不正确");
        }
        User cacheUser = getUser(user.getUsername());
        cacheUser.setGoogleSecret(dto.getSecret());
        userMapper.updateById(cacheUser);
        //检查该用户已存在token并删除，注释掉可实现多机登陆
        Map<String ,Object> tokens = getData();
        tokens.forEach((key,value)->{
            JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
            if (jsonObject.get("username").equals(user.getUsername())) {
                redisUtil.delete(key);
            }
        });
        redisUtil.setEx(Tools.getTokenKey(request, CacheEnum.LOGIN),cacheUser,1, TimeUnit.DAYS);
    }

    @Override
    public void googleLogin(Long code, User user, HttpServletRequest request) throws CustomException {
        if (code == null) {
            throw new CustomException(TAG, "请输入Google验证码");
        }
        if(StringUtils.isEmpty(user.getGoogleSecret())){
            throw new CustomException(TAG,"该账号暂未绑定Google密钥");
        }
        boolean isTrue = GoogleAuthenticator.check_code(user.getGoogleSecret(), code, System.currentTimeMillis());
        if(!isTrue){
            throw new CustomException(TAG,"Google验证码不正确");
        }
        redisUtil.setEx(Tools.getTokenKey(request,CacheEnum.GOOGLE), Consts.SUCCESS,1,TimeUnit.DAYS);
    }
}
