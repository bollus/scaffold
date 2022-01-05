package jsz.dk.scaffold.common.annotations;

import jsz.dk.scaffold.enums.OperationType;
import jsz.dk.scaffold.enums.OperationUnit;

import java.lang.annotation.*;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.common.annotations
 * @ClassName: OperationLogDetail
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/12 16:16
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLogDetail {

    /**
     * 方法描述， 可使用占位符获取参数：{{args}}
     */
    String detail() default "";

    /**
     * 日志等级，分级度：1-9
     */
    int level() default 0;

    /**
     * 操作类型(enum):主要为select，insert，update，delete
     */
    OperationType operationType() default OperationType.UNKNOWN;

    /**
     * 被操作的对象(此处使用enum):可以是任何对象，如表名(user)，或者是工具(redis)
     */
    OperationUnit operationUnit() default OperationUnit.UNKNOWN;


}
