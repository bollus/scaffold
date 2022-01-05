package jsz.dk.scaffold.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.annotations
 * @ClassName: UpdateTime
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 22:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UpdateTime {
    String value() default "";
}
