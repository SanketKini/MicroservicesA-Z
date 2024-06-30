package com.awd.feature.toggle.services.service;

import com.awd.feature.toggle.model.*;
import com.awd.feature.toggle.services.dao.entities.StaticEnvironmentEntity;

import java.util.List;
import java.util.Optional;

public interface FeatureToggleService {
    void createFeatureFlag(String user, String env, CreateFeatureRequest createFeatureRequest) throws Exception;

    List<Feature> getFeatureFlags();

    List<Feature> getFeatureFlags(StaticEnvironments environment, FlagStates state);

    void registerService(StaticEnvironments environment, RegisterServiceRequest registerServiceRequest);

    void updateFeatureFlag(String user, StaticEnvironments environment, Toggle toggleFlagValue, String featureName);

    void archiveFeatureFlag(String user, StaticEnvironments environment, String featureName);

    Optional<List<StaticEnvironmentEntity>> getAllEnvironments();
}
