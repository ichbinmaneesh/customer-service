package com.interview.customer.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class Logging {

    @Around("execution(* com.user.details.controllers.*.*(..))||execution(* com.user.details.services.*.*(..))||execution(* com.user.details.repositories.*.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Entering method {}", joinPoint.getSignature().getName());
        Object result =  joinPoint.proceed();
        log.info("Exiting method {}", joinPoint.getSignature().getName());
        return result;
    }
}
