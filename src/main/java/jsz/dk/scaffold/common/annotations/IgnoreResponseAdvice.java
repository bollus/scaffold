package jsz.dk.scaffold.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.annotations
 * @ClassName: IgnoreResponseAdvice
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/13 19:46
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreResponseAdvice {
}
