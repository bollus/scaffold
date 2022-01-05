package jsz.dk.scaffold.common.annotations;

import jsz.dk.scaffold.configuration.DefaultConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.annotations
 * @ClassName: EnableGlobalDispose
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/13 19:45
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DefaultConfiguration.class)
@SuppressWarnings("unused")
public @interface EnableGlobalDispose {
}
