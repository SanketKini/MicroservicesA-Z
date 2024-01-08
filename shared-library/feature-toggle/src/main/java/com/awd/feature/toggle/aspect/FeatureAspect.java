package com.awd.feature.toggle.aspect;


import com.awd.feature.toggle.annotations.FeatureFlag;
import com.awd.feature.toggle.annotations.ToggleOffReturn;
import com.awd.feature.toggle.facade.FeatureManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Slf4j
public class FeatureAspect {

    @Around(value = "execution( * *(..)) && @annotation(featureFlag)", argNames = "featureFlag")
    public Object toggleFeatureAnnotation(ProceedingJoinPoint point, FeatureFlag featureFlag) throws Throwable {
        return toggleFeature(point, featureFlag);
    }

    @Around(value = "execution( * *(..)) && @within(featureFlag)", argNames = "featureFlag")
    public Object toggleFeatureWithin(ProceedingJoinPoint point, FeatureFlag featureFlag) throws Throwable {
        return toggleFeature(point, featureFlag);
    }

    @Around(value = "get (@FeatureFlag * *) && @within(featureFlag)", argNames = "featureFlag")
    public Object toggleFeatureField(ProceedingJoinPoint point, FeatureFlag featureFlag) throws Throwable {
        return toggleFeature(point, featureFlag);
    }

    private Object toggleFeature(ProceedingJoinPoint point, FeatureFlag featureFlag) throws Throwable {
        final String feature = featureFlag.value();
        final boolean enabled = FeatureManager.getInstance().isFeatureEnabled(feature);
        final boolean xorEnabled = enabled ^ featureFlag.inverse();
        final MethodSignature signature = (MethodSignature) point.getSignature();
        final boolean throwToggleOffError = featureFlag.throwToggleOffError();
        if (xorEnabled) {
            final Object returnValue = point.proceed();
            log.trace("Name: {}, feature: {} enabled, return: {}", signature.getName(), feature, returnValue);
            return returnValue;
        } else {
            log.trace("Name: {}, feature: {} disabled", signature.getName(), feature);
            if (throwToggleOffError) {
                log.trace("Feature {} is toggled off on method {}", feature, signature.getName());
                throw new Exception("something"); // TODO use custom Exception Handler
            }
            return getDefaultReturnValue(point, signature);
        }
    }

    private Object getDefaultReturnValue(ProceedingJoinPoint point, MethodSignature signature) {
        final Method method = signature.getMethod();
        final Object[] argValues = point.getArgs();
        final int parameterCount = method.getParameterCount();
        final Parameter[] parameters = method.getParameters();
        log.trace("Method parameters : {}, {}", parameterCount, argValues);
        for(int i =0; i<parameterCount; i++){
            Parameter parameter = parameters[i];
            for(Annotation annotation: parameter.getAnnotations()){
                if(!(annotation instanceof ToggleOffReturn)){
                    continue;
                }
                Object defaultReturnValue = argValues[i];
                log.debug("Found default return value : {}", defaultReturnValue);
                return defaultReturnValue;
            }
        }
        return null;
    }
}
