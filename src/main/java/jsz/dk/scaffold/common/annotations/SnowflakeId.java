package jsz.dk.scaffold.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.annotations
 * @ClassName: SnowflakeId
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 22:47
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SnowflakeId {
    String value() default "";
}
