package com.awd.feature.toggle.aspect;


import com.awd.feature.toggle.annotations.GetNullable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class GetNullableAspect {
    @Around(value = "execution( * *(..)) && @annotation(getNullable)", argNames = "getNullable")
    public Object getNullableMessageForAnnotation(ProceedingJoinPoint point, GetNullable getNullable) throws Throwable {
        return getNullableMessage(point, getNullable);
    }

    private Object getNullableMessage(ProceedingJoinPoint point, GetNullable getNullable) throws Throwable {
        try {
            return point.proceed();
        } catch (NullPointerException npe) {
            log.warn("Null Pointer Exception thrown when trying to proceed to pointcut with message {}", getNullable.nullMessage(), npe);
            return null;
        }
    }
}
