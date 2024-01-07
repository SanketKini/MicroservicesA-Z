package com.awd.feature.toggle.facade;

import com.awd.feature.toggle.model.FeatureEnvData;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeatureManager {
    private static final String LOCAL_ENV = "local";

    private static FeatureManager instance = null;

    @Autowired
    private FeatureEnvData featureEnvData;

    @Autowired
    private FeatureToggleServiceFacade featureToggleServiceFacade;

    private Map<String, Boolean> featureMapProxy;
    private Map<String, Boolean> featureMap;


    private static synchronized FeatureManager getInstance() {
        return instance;
    }

    private static synchronized void setInstance(FeatureManager instance) {
        FeatureManager.instance = instance;
    }

    public boolean isFeatureEnabled(@NonNull String feature) throws Exception {
        if (featureMapProxy == null || featureMapProxy.isEmpty()) {
            log.warn("No features loaded during startup");
            return false;
        }
        final Boolean aBoolean = featureMapProxy.get(feature);
        if (aBoolean == null) {
            throw new Exception("Feature : " + feature + " not present in feature map " + featureMapProxy); // TODO setup custom exception handler
        }
        return aBoolean;
    }

    @PostConstruct
    private void init() throws Exception {
        log.debug("Loading features {}, {}, {}, {}, {}", featureEnvData.getFtServiceUrl(), featureEnvData.getFeatures(),
                featureEnvData.getLocalFeatureMap(), featureEnvData.getServiceName(), featureEnvData.getEnv());
        this.featureMap = this.loadFeatures(featureEnvData);
        this.featureMapProxy = new HashMap<>(this.featureMap);
        setInstance(this);
    }

    private Map<String, Boolean> loadFeatures(FeatureEnvData featureEnvData) throws Exception {
        final List<String> features = featureEnvData.getFeatures();
        if (CollectionUtils.isEmpty(features)) {
            log.warn("No features loaded from properties file ");
            return Collections.emptyMap();
        }
        boolean loadFromLocal = isAnyEmpty(featureEnvData.getFtServiceUrl(), featureEnvData.getServiceName(),
                featureEnvData.getEnv()) || LOCAL_ENV.equalsIgnoreCase(featureEnvData.getEnv());
        return loadFromLocal ? getFeaturesFromLocal() : getFeaturesFromService();
    }

    private Map<String, Boolean> getFeaturesFromLocal() throws Exception {
        final List<String> features = featureEnvData.getFeatures();
        final Map<String, Boolean> localFeatureMap = featureEnvData.getLocalFeatureMap();
        final Set<String> featureMapKeySet = localFeatureMap.keySet();
        log.info("Loading feature map from properties for features {}, feature map {}", features, localFeatureMap);
        if (!CollectionUtils.isEqualCollection(features, featureMapKeySet)) {
            log.error("Featuer list {} not matching with loaded feature map {}", features, localFeatureMap);
            throw new Exception("Feature list is not matching with loaded feature map"); // TODO setup custom exception handler
        }
        return localFeatureMap;
    }

    private Map<String, Boolean> getFeaturesFromService() throws Exception {
        final String correlationId = UUID.randomUUID().toString();
        log.info("Loading feature map from feature toggle service {} for features {} with correlation id {}",
                featureEnvData.getFtServiceUrl(), featureEnvData.getFeatures(), correlationId);
        return featureToggleServiceFacade.getServiceFeatureMap(correlationId);
    }

    private boolean isAnyEmpty(Object... items) {
        for (Object item : items) {
            if (item == null) {
                return true;
            }
            if (item instanceof String && StringUtils.isBlank((String) item)) {
                return true;
            }
        }
        return false;
    }

}
