package com.handwoong.everyonewaiter.config.aop

import com.handwoong.everyonewaiter.exception.CustomException
import com.handwoong.everyonewaiter.exception.ErrorCode
import com.handwoong.everyonewaiter.util.RedissonLock
import com.handwoong.everyonewaiter.util.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component

@Aspect
@Component
class RedissonAop(
    private val redissonClient: RedissonClient,
    private val useAopTransaction: UseAopTransaction,
) {

    private val log = logger()

    @Around("@annotation(com.handwoong.everyonewaiter.util.RedissonLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val redissonLock = method.getAnnotation(RedissonLock::class.java)

        val parameterNames = signature.parameterNames
        var redissonKey = ""
        for (i in 0..parameterNames.size) {
            if (parameterNames[i] == redissonLock.key) {
                redissonKey += "${redissonLock.key}-${joinPoint.args[i]}"
                break
            }
        }

        val lock = redissonClient.getLock(redissonKey)
        try {
            val isLock = lock.tryLock(redissonLock.waitTime, redissonLock.leaseTime, redissonLock.timeUnit)

            if (!isLock) {
                log.error("Redisson Lock Fail : $redissonKey")
                return false
            }

            log.info("Redisson Lock Key : $redissonKey")
            return useAopTransaction.proceed(joinPoint)
        } catch (e: InterruptedException) {
            throw CustomException(ErrorCode.SERVER_ERROR)
        } finally {
            lock.unlock()
        }
    }

}
