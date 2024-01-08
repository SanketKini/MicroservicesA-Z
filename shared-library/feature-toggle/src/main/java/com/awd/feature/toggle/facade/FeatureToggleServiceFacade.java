package com.awd.feature.toggle.facade;

import com.awd.feature.toggle.model.Feature;
import com.awd.feature.toggle.model.FeatureEnvData;
import com.awd.feature.toggle.model.RegisterServiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FeatureToggleServiceFacade {

    public static final String ACTIVE = "active";
    private static final String LIST_FEATURES_URI = "/api/features/{env}/list/{state}";
    private static final String REGISTER_SERVICE_URI = "/api/features/{env}/flags/register";

    @Autowired
    @Qualifier("featureToggleWebClient")
    private WebClient featureToggleWebClient;

    @Autowired
    private FeatureEnvData featureEnvData;

    public Map<String, Boolean> getServiceFeatureMap(String correlationId) throws Exception {
        final List<String> envFeatureData = featureEnvData.getFeatures();
        if (CollectionUtils.isEmpty(envFeatureData)) {
            log.warn("No Features were loaded during startup, defaulting to disabled for correlation id: {}", correlationId);
            return Collections.emptyMap();
        }

        final List<Feature> allFeatures = getAllFeatures(correlationId);
        if (CollectionUtils.isEmpty(allFeatures)) {
            log.error("Got empty feature flag map from feature toggle service, expected feature toggle values for {} on env {} for correlation id: {}",
                    envFeatureData, featureEnvData.getEnv(), correlationId);
            throw new IllegalStateException("Got empty feature flag map from service"); // TODO custom exception handler
        }
        final Map<String, Boolean> filteredFeatureMap = allFeatures.stream()
                .filter(feature -> envFeatureData.contains(feature.getFeatureName()))
                .collect(Collectors.toMap(Feature::getFeatureName, Feature::isEnabled));
        checkFeatureListMatch(envFeatureData, filteredFeatureMap);
        return filteredFeatureMap;
    }

    private List<Feature> getAllFeatures(String correlationId) {
        return featureToggleWebClient.get()
                .uri(uriBuilder -> uriBuilder.path(LIST_FEATURES_URI).build(featureEnvData.getEnv(), ACTIVE))
                .header("X-Correlation-Id", correlationId)
                .header("X-Service-Name", featureEnvData.getEnv() + "/" + featureEnvData.getServiceName())
                .retrieve()
                .onStatus(this::isNot2xxStatus, clientResponse -> {
                    log.error("Got error status: {}, body: {}, correlationId: {}", clientResponse.statusCode(), clientResponse.bodyToMono(String.class), correlationId);
                    return Mono.error(new IllegalStateException("Could not fetch list of feature flags from service call. Failed with HTTP Status: " + clientResponse.statusCode()));
                })
                .bodyToFlux(Feature.class)
                .collectList()
                .doOnSuccess(features -> log.debug("Fetched feature list of size: {}", features.size()))
                .block();
    }

    public void register(String correlationId) {
        final RegisterServiceRequest registerServiceRequest = RegisterServiceRequest.builder()
                .serviceFeatures(featureEnvData.getFeatures())
                .serviceName(featureEnvData.getServiceName())
                .build();
        featureToggleWebClient.post()
                .uri(uriBuilder -> uriBuilder.path(REGISTER_SERVICE_URI).build(featureEnvData.getEnv()))
                .header("X-Correlation-Id", correlationId)
                .header("X-Service-Name", featureEnvData.getEnv() + "/" + featureEnvData.getServiceName())
                .body(Mono.just(registerServiceRequest), RegisterServiceRequest.class)
                .retrieve()
                .onStatus(this::isNot2xxStatus, clientResponse -> {
                    log.error("Got error status: {}, body: {}, correlationId: {}", clientResponse.statusCode(), clientResponse.bodyToMono(String.class), correlationId);
                    return Mono.error(new IllegalStateException("Could not fetch list of feature flags from service call. Failed with HTTP Status: " + clientResponse.statusCode()));
                })
                .toBodilessEntity()
                .block();
log.debug("Successfully registered service: {} for features: {}", featureEnvData.getServiceName(), featureEnvData.getFeatures());
    }

    private boolean isNot2xxStatus(HttpStatusCode httpStatusCode) {
        log.debug("Received http response status: {}", httpStatusCode.toString());
        return !httpStatusCode.is2xxSuccessful();
    }

    private void checkFeatureListMatch(List<String> envFeatureData, Map<String, Boolean> filteredFeatureMap) throws Exception {
        Set<String> assignedFeatures = filteredFeatureMap.keySet();
        boolean equalCollection = CollectionUtils.isEqualCollection(envFeatureData, assignedFeatures);
        log.info("Final map of feature flags: {}, equalCollection: {}", filteredFeatureMap, equalCollection);
        if (!equalCollection) {
            log.error("Flags {}, have no definition or are archived in env {}", CollectionUtils.removeAll(envFeatureData, assignedFeatures), featureEnvData.getEnv());
            throw new IllegalStateException("Features missing in definition or archived in env"); // TODO custom exception handler
        }
    }

}
