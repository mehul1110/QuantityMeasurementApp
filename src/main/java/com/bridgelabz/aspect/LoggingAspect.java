package com.bridgelabz.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * UC17: AOP Logging Aspect — Enterprise Observability.
 *
 * Intercepts ALL public methods in the service and controller layers using
 * @Around advice. Logs entry/exit and execution time without modifying any
 * business logic. This is the Aspect-Oriented Programming (AOP) pattern:
 * cross-cutting concerns (logging) are separated from business logic.
 *
 * @Aspect  — marks this class as an AspectJ aspect
 * @Component — registers it as a Spring bean
 * @Around  — wraps the method execution (entry + exit + exception)
 * ProceedingJoinPoint.proceed() — delegates to the real method
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // =========================================================
    // Pointcut Definitions
    // =========================================================

    /**
     * Matches all public methods in the service layer.
     */
    @Pointcut("execution(* com.bridgelabz.service.*.*(..))")
    public void serviceLayer() {}

    /**
     * Matches all public methods in the controller layer.
     */
    @Pointcut("execution(* com.bridgelabz.controller.*.*(..))")
    public void controllerLayer() {}

    // =========================================================
    // Service Layer Advice — logs business operation timing
    // =========================================================

    /**
     * Around advice: logs entry, execution time, and exit for all service methods.
     *
     * Benefits:
     * - Zero impact on business logic (no changes to service code needed)
     * - Single place to change logging behaviour for all service methods
     * - Captures actual execution time for performance tracking
     *
     * @param joinPoint provides access to method signature and arguments
     * @return the return value of the target method
     */
    @Around("serviceLayer()")
    public Object logServiceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className  = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info("→ Entering [{}.{}()]", className, methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("← Exiting  [{}.{}()] — completed in {}ms", className, methodName, duration);
            return result;

        } catch (Exception e) {
            long duration = System.currentTimeMillis() - startTime;
            log.error("✗ Exception in [{}.{}()] after {}ms: {}",
                    className, methodName, duration, e.getMessage());
            throw e;
        }
    }

    // =========================================================
    // Controller Layer Advice — logs HTTP request handling
    // =========================================================

    /**
     * Around advice for controller methods: logs request entry/exit.
     */
    @Around("controllerLayer()")
    public Object logControllerMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className  = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("► HTTP request handled by [{}.{}()]", className, methodName);
        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.debug("◄ Response from [{}.{}()] in {}ms", className, methodName, duration);
            return result;

        } catch (Exception e) {
            log.error("✗ Controller exception in [{}.{}()]: {}",
                    className, methodName, e.getMessage());
            throw e;
        }
    }
}
