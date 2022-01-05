package jsz.dk.scaffold.interceptor;

import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import jsz.dk.scaffold.common.SnowflakeIdWorker;
import jsz.dk.scaffold.common.annotations.CreateTime;
import jsz.dk.scaffold.common.annotations.SnowflakeId;
import jsz.dk.scaffold.common.annotations.State;
import jsz.dk.scaffold.common.annotations.UpdateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Properties;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.interceptor
 * @ClassName: SqlInterceptor
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/02 22:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class SqlInterceptor extends AbstractSqlParserHandler implements Interceptor {
    private Properties properties;

    @Override
    @SuppressWarnings("all")
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // 获取 SQL 命令
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 获取参数
        Object parameter = invocation.getArgs()[1];
        // 获取私有成员变量
        Field[] declaredFields = parameter.getClass().getDeclaredFields();
        if (parameter.getClass().getSuperclass() != null) {
            Field[] superField = parameter.getClass().getSuperclass().getDeclaredFields();
            declaredFields = ArrayUtils.addAll(declaredFields, superField);
        }
        // 是否为mybatis plus
        boolean isPlusUpdate = parameter.getClass().getDeclaredFields().length == 1
                && parameter.getClass().getDeclaredFields()[0].getName().equals("serialVersionUID");
        //兼容mybatis plus的update
        if (isPlusUpdate) {
            Map<String, Object> updateParam = (Map<String, Object>) parameter;
            Class<?> updateParamType = updateParam.get("param1").getClass();
            declaredFields = updateParamType.getDeclaredFields();
            if (updateParamType.getSuperclass() != null) {
                Field[] superField = updateParamType.getSuperclass().getDeclaredFields();
                declaredFields = ArrayUtils.addAll(declaredFields, superField);
            }
        }
        for (Field field : declaredFields) {
            /*
            insert
             */
            //创建时间
            if (field.getAnnotation(CreateTime.class) != null) {
                if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                    field.setAccessible(true);
                    field.set(parameter, new Timestamp(System.currentTimeMillis()));
                }
            }
            //雪花ID构建
            if (field.getAnnotation(SnowflakeId.class) != null) {
                if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                    field.setAccessible(true);
                    field.set(parameter, SnowflakeIdWorker.idWorker.nextId());
                }
            }
            //状态
            if (field.getAnnotation(State.class) != null){
                if (sqlCommandType.INSERT.equals(sqlCommandType)){
                    field.setAccessible(true);
                    field.set(parameter, field.getAnnotation(State.class).value());
                }
            }
            /*
            insert & update
             */
            //更新时间
            if (field.getAnnotation(UpdateTime.class) != null) {
                if (SqlCommandType.INSERT.equals(sqlCommandType)
                        || SqlCommandType.UPDATE.equals(sqlCommandType)) {
                    field.setAccessible(true);
                    //兼容mybatis plus的update
                    if (isPlusUpdate) {
                        Map<String, Object> updateParam = (Map<String, Object>) parameter;
                        field.set(updateParam.get("param1"), new Timestamp(System.currentTimeMillis()));
                    } else {
                        field.set(parameter, new Timestamp(System.currentTimeMillis()));
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
