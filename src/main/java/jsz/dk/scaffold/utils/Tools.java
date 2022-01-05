package jsz.dk.scaffold.utils;

import jsz.dk.scaffold.common.entity.CacheKey;
import jsz.dk.scaffold.common.entity.Consts;
import jsz.dk.scaffold.enums.CacheEnum;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.utils
 * @ClassName: Tools
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/15 17:39
 */
public class Tools {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    /**
     *  获取 token
     */
    public static String getToken(HttpServletRequest request){
        String token = request.getHeader(Consts.TOKEN);
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(Consts.TOKEN);
        }
        return token;
    }

    /**
     *  获取 tokenKey
     */
    public static String getTokenKey(HttpServletRequest request, CacheEnum cacheEnum){
        String token = getToken(request);
        if(cacheEnum.equals(CacheEnum.GOOGLE)){
            token =  String.format(CacheKey.TOKEN_KEY_GOOGLE,token);
        }else if(cacheEnum.equals(CacheEnum.LOGIN)){
            token =  String.format(CacheKey.TOKEN_KEY_LOGIN,token);
        }
        return token;
    }
}
