package jsz.dk.scaffold.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jsz.dk.scaffold.common.annotations.OperationLogDetail;
import jsz.dk.scaffold.common.entity.CustomException;
import jsz.dk.scaffold.common.entity.OperationLog;
import jsz.dk.scaffold.entity.User;
import jsz.dk.scaffold.enums.CacheEnum;
import jsz.dk.scaffold.utils.IPUtil;
import jsz.dk.scaffold.utils.RedisUtil;
import jsz.dk.scaffold.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ProjectName: sign-management
 * @Package: jsz.dk.signmanagement.aop
 * @ClassName: LogAspect
 * @Author: Strawberry
 * @Description:
 * @Date: 2021/07/12 16:11
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private RedisUtil redisUtil;

    @Pointcut("@annotation(jsz.dk.scaffold.common.annotations.OperationLogDetail)")
    public void operationLog(){}

    @Around("operationLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        Object res = null;
        long time = System.currentTimeMillis();
        try {
            res = joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            return res;
        }finally {
            try{
                ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                User user = new User();
                user.setUsername("none");
                if (attr != null){
                    String tokenKey = Tools.getTokenKey(attr.getRequest(), CacheEnum.LOGIN);
                    JSONObject jsonObject = (JSONObject)JSON.toJSON(redisUtil.get(tokenKey));
                    if (jsonObject != null) {
                        user = jsonObject.toJavaObject(User.class);
                    }
                }
                addOperationLog(joinPoint, res, time,user);
            }catch (Exception e){
                log.warn("LogAspect ???????????????{}",e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void addOperationLog(JoinPoint joinPoint, Object res, long time, User currentUser){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationLog operationLog = new OperationLog();
        operationLog.setRunTime(time);
        operationLog.setReturnValue(JSON.toJSONString(res));
        operationLog.setId(UUID.randomUUID().toString());
        operationLog.setArgs(JSON.toJSONString(joinPoint.getArgs()));
        operationLog.setCreateTime(new Date());
        operationLog.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        operationLog.setUserId(String.valueOf(currentUser.getId()));
        operationLog.setUserName(currentUser.getUsername());
        OperationLogDetail annotation = signature.getMethod().getAnnotation(OperationLogDetail.class);
        if (annotation != null){
            operationLog.setLevel(annotation.level());
            operationLog.setDescribe(getDetail(((MethodSignature) joinPoint.getSignature()).getParameterNames(), joinPoint.getArgs(), annotation, currentUser));
            operationLog.setOperationType(annotation.operationType().getValue());
            operationLog.setOperationUnit(annotation.operationUnit().getValue());
        }
        log.info("???????????????{}",operationLog.toString());
    }

    private String getDetail(String[] argName, Object[] args, OperationLogDetail annotation, User currentUser){
        Map<Object, Object> map = new HashMap<>(4);
        for (int i = 0; i < argName.length; i++){
            map.put(argName[i], args[i]);
        }

        String detail = annotation.detail();
        try {
            detail = "'" + currentUser.getUsername() + "'=>" + annotation.detail();
            for (Map.Entry<Object, Object> entry : map.entrySet()){
                Object k = entry.getKey();
                Object v = entry.getValue();
                detail = detail.replace("{{" + k + "}}", JSON.toJSONString(v));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return detail;
    }

    @SuppressWarnings("unused")
    @Before("operationLog()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        ServletRequestAttributes attr=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attr != null;
        HttpServletRequest request =attr.getRequest();
        String clientIp = IPUtil.getIpAddr(request);
        String userAgent = request.getHeader("user-agent");
        String origin = request.getHeader("origin");
        String referer = request.getHeader("referer");
        String method = request.getMethod();
        String remoteUser = request.getRemoteUser();
        String requestUri = request.getRequestURI();
        log.info("??????????????????: client-ip:{}, request-uri:{}, method:{}, origin:{}, referer:{}, remote-user:{}, user-agent:{}",clientIp,requestUri,method,origin,referer,remoteUser,userAgent);
    }

    /**
     * ??????????????????????????????
     */
    @AfterReturning(returning = "ret", pointcut = "operationLog()")
    public void doAfterReturning(Object ret){
        log.info("?????????????????????{}",ret);
    }

    /**
     * ??????????????????
     */
    @SuppressWarnings("unused")
    @AfterThrowing(throwing = "ce",pointcut = "operationLog()")
    public void throwException(CustomException ce){
        log.info("??????????????????: {}-{}",ce.getTag(),ce.getMsg());
    }
}
