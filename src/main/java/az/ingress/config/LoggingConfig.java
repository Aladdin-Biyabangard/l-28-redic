package az.ingress.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingConfig {

    @Around("execution(* az.ingress.service.concret..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();

        log.info("ActionLog.{}.START", methodName);

        try {
            Object result = joinPoint.proceed();
            log.info("ActionLog.{}.SUCCESS", methodName);
            return result;
        } catch (Exception ex) {
            log.error("ActionLog.{}.FAILED - Error: {}", methodName, ex.getMessage(), ex);
            throw ex;
        }
    }


}
