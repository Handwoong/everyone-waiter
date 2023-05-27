package com.handwoong.everyonewaiter.config.aop

import com.handwoong.everyonewaiter.util.logger
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.lang.reflect.Method

@Aspect
@Component
class LoggingAop {

    private val log = logger()

    @Pointcut("execution(* com.handwoong.everyonewaiter.service.*.*(..)) && !@annotation(com.handwoong.everyonewaiter.util.ExcludeLog)")
    private fun servicePointcut() {
    }

    @Before("servicePointcut()")
    fun parameterLogBefore(joinPoint: JoinPoint) {
        val method = getMethod(joinPoint)
        log.info("============== Method Start : ${method.name} ==============")
        for (arg in joinPoint.args) {
            log.info("Parameter : '${arg.javaClass.simpleName}' '${arg}'")
        }
    }

    @AfterReturning(value = "servicePointcut()", returning = "returnValue")
    fun afterReturning(joinPoint: JoinPoint, returnValue: Any?) {
        val method = getMethod(joinPoint)
        log.info("Return : '${returnValue?.javaClass?.simpleName}' '${returnValue}'")
        log.info("============== Method End : ${method.name} ==============")
    }

    @Around("servicePointcut()")
    fun serviceLog(joinPoint: ProceedingJoinPoint): Any? {
        val transactionName = TransactionSynchronizationManager.getCurrentTransactionName()
        val startTime = System.currentTimeMillis()

        log.info("============ [${transactionName}] Transaction Start ============")
        val result = joinPoint.proceed()
        val endTime = System.currentTimeMillis()
        log.info("========= [${transactionName}] Transaction End '${endTime - startTime}'ms  =========")

        return result
    }

    private fun getMethod(joinPoint: JoinPoint): Method {
        val methodSignature = joinPoint.signature as MethodSignature
        return methodSignature.method
    }

}
