package com.awd.feature.toggle.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder(toBuilder = true)
public class FeatureEnvData {
    List<String> features;
    Map<String, Boolean> localFeatureMap;
    String ftServiceUrl;
    String env;
    String serviceName;
}
