package com.awd.feature.toggle.services.dao;

import com.awd.feature.toggle.model.*;
import com.awd.feature.toggle.services.dao.entities.StaticEnvironmentEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public interface FeatureTogglePostGreDAO {
    void createFeatureFlag(String user, CreateFeatureRequest createFeatureRequest, String environment);

    List<Feature> getFeatureFlags(boolean excludeArchived);

    List<Feature> getFeatureFlags(StaticEnvironments environment, FlagStates state);

    void updateServiceSet(StaticEnvironments environment, Set<String> featureNames, String serviceName, boolean add);

    void updateFeatureFlag(String user, StaticEnvironments environment, Toggle toggleFlagValue, String featureName);

    void archiveFeatureFlag(String user, StaticEnvironments environment, String featureName);

    Optional<List<StaticEnvironmentEntity>> getAllEnvironments();
}
