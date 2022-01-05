package jsz.dk.scaffold.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jsz.dk.scaffold.common.entity.CustomException;
import jsz.dk.scaffold.entity.User;
import jsz.dk.scaffold.enums.CacheEnum;
import jsz.dk.scaffold.enums.ResponseCode;
import jsz.dk.scaffold.utils.RedisUtil;
import jsz.dk.scaffold.utils.Tools;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;


/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.controller
 * @ClassName: BaseController
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/15 17:53
 */
public class BaseController {
    @Resource
    private RedisUtil redisUtil;

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest();
    }
    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getResponse();
    }

    /**
     * 从token 中获取用户信息
     */
    protected User getUser() throws CustomException{
        String tokenKey = Tools.getTokenKey(this.getRequest(), CacheEnum.LOGIN);
        JSONObject jsonObject = (JSONObject)JSON.toJSON(redisUtil.get(tokenKey));
        if(jsonObject == null) throw new CustomException(ResponseCode.AUTH_LOGIN_NOT_VALID.getMessage(),ResponseCode.AUTH_LOGIN_NOT_VALID.getCode());
        return jsonObject.toJavaObject(User.class);
    }
}
