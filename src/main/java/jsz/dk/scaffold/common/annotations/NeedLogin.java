package jsz.dk.scaffold.common.annotations;

import java.lang.annotation.*;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.annotations
 * @ClassName: NeedLogin
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/14 22:19
 */
@Documented
@Inherited
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedLogin {
    // 是否有效,如果注解在类上，又想要某个方法上不生效，可用这个配置
    boolean isValid() default true;
    //登录注解
    boolean login() default true;
    //google验证注解
    boolean google() default false;
}