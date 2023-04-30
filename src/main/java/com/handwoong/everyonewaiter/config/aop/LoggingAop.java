package com.handwoong.everyonewaiter.config.aop;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Aspect
@Component
public class LoggingAop {

    @Pointcut("execution(* com.handwoong.everyonewaiter.service.*.*(..))")
    private void servicePointcut() {
    }

    @Before("servicePointcut()")
    public void parameterLogBefore(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("============== Method Start : {} ==============", method.getName());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.info("Parameter : '{}' '{}'", arg.getClass().getSimpleName(), arg);
        }
    }

    @AfterReturning(value = "servicePointcut()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        Method method = getMethod(joinPoint);
        log.info("============== Method End : {} ==============", method.getName());
        if (returnValue != null) {
            log.info("Return : '{}' '{}'", returnValue.getClass().getSimpleName(), returnValue);
        }
    }

    @Around("servicePointcut()")
    public Object serviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        long start = System.currentTimeMillis();
        log.info("============ [{}] Transaction Start ============", transactionName);
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("========= [{}] Transaction End '{}'ms  =========", transactionName, end - start);
        return result;
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
