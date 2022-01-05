package jsz.dk.scaffold.enums;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.enums
 * @ClassName: RequestMethod
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/03 19:17
 */
public enum RequestMethod {
    POST("POST"),
    GET("GET"),
    PUT("PUT"),
    PATCH("PATCH"),
    DELETE("DELETE"),
    COPY("COPY"),
    HEAD("HEAD"),
    OPTION("OPTION"),
    LINK("LINK"),
    UNLINK("UNLINK"),
    PURGE("PURGE"),
    LOCK("LOCK"),
    UNLOCK("UNLOCK"),
    PROPFIND("PROPFIND"),
    VIEW("VIEW");

    RequestMethod(String val) {}
}
