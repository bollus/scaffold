package jsz.dk.scaffold.interceptor.advice;

import jsz.dk.scaffold.common.annotations.IgnoreResponseAdvice;
import jsz.dk.scaffold.common.entity.ResponseParent;
import jsz.dk.scaffold.configuration.DefaultProperties;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.interceptor.advice
 * @ClassName: BaseResponseAdvice
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/13 19:44
 */
@RestControllerAdvice
public class BaseResponseAdvice implements ResponseBodyAdvice<Object> {

    private final DefaultProperties defaultProperties;

    public BaseResponseAdvice(DefaultProperties defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return filter(methodParameter);
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        // 如果手动进行了返回封装，判断类型防止二次封装
        if (!(o instanceof ResponseParent)) {
            return ResponseParent.succeed(o);
        }
        return o;
    }

    /**
     * 用来过滤不需要包装返回参数的接口
     */
    private Boolean filter(MethodParameter methodParameter) {
        Class<?> declaringClass = methodParameter.getDeclaringClass();
        // 检查过滤包路径
        long count = defaultProperties.getAdviceFilterPackage().stream()
                .filter(l -> declaringClass.getName().contains(l)).count();
        if (count > 0) {
            return false;
        }
        // 检查<类>过滤列表
        if (defaultProperties.getAdviceFilterClass().contains(declaringClass.getName())) {
            return false;
        }
        // 检查注解是否存在
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return !Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class);
    }
}

