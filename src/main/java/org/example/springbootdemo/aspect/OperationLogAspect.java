package org.example.springbootdemo.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.springbootdemo.annotation.LogOperation;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志切面 aspect
 */
@Aspect
@Component
@Slf4j
public class OperationLogAspect {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    // 定义切入点：所有使用了 @LogOperation 注解的方法
    @Pointcut("@annotation(logOperation)")
    public void logPointcut(LogOperation logOperation) {
    }

    /**
     * @Around 通过 joinPoint.proceed()，因此能在执行目标方法前和后执行你要做的操作。往往去做日志记录、事务管理、性能监控
     * @Before 方法执行前，往往去做权限校验、参数校验
     * @After 方法执行后，往往去做资源清理、审计
     * @AfterReturning 方法成功返回后
     * @AfterThrowing 方法抛出异常后
     */
    // 环绕通知
    @Around("logPointcut(logOperation)")
    public Object logAround(ProceedingJoinPoint joinPoint, LogOperation logOperation) throws Throwable {
        long start = System.currentTimeMillis(); // 方法执行前的时间

        try {
            // 执行目标方法（核心！）
            Object result = joinPoint.proceed();
            // 存储 log 日志
            saveLog(joinPoint, logOperation, result, null, start);
            return result;
        } catch (Exception e) {
            // 存储 log 日志
            saveLog(joinPoint, logOperation, null, e, start);
            throw e;
        }
    }

    /**
     * 存储 log 日志
     *
     * @param joinPoint    切点
     * @param logOperation 日志注解
     * @param result       切点执行结果
     * @param exception    异常
     * @param startTime    开始时间
     */
    private void saveLog(ProceedingJoinPoint joinPoint, LogOperation logOperation,
                         Object result, Exception exception, long startTime) {
        long time = System.currentTimeMillis() - startTime; // 方法耗时

        String methodName = joinPoint.getSignature().toShortString(); // 方法名

        // 日志 string
        StringBuilder logInfo = new StringBuilder(100)
                .append("[操作日志] 类型:").append(logOperation.operationType())
                .append(" | 描述:").append(logOperation.value())
                .append(" | 方法:").append(methodName)
                .append(" | 耗时:").append(time).append("ms");

        // 如果需要保存请求参数
        if (logOperation.saveRequestParams()) {
            logInfo.append(" | 参数:").append(getArgs(joinPoint.getArgs()));
        }

        // 如果方法没有抛出异常
        if (exception == null) {
            logInfo.append(" | 结果:").append(getResult(result));
            log.info(logInfo.toString());
        } else {
            logInfo.append(" | 异常:").append(exception.getMessage());
            log.error(logInfo.toString());
        }
    }

    // 根据 joinPoint.getArgs() 获取方法参数
    private String getArgs(Object[] args) {
        try {
            // 序列化
            return JSON_MAPPER.writeValueAsString(Arrays.stream(args)
                    .filter(arg -> !(arg instanceof HttpServletRequest || arg instanceof MultipartFile))
                    .toArray());
        } catch (Exception e) {
            return "参数序列化失败";
        }
    }

    // 根据 joinPoint.proceed() 获取方法结果
    private String getResult(Object result) {
        if (result == null) return "null";
        try {
            // 序列化
            return JSON_MAPPER.writeValueAsString(result);
        } catch (Exception e) {
            return "结果序列化失败";
        }
    }

}
