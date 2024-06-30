package com.awd.feature.toggle.services.service;

import com.awd.feature.toggle.model.*;
import com.awd.feature.toggle.services.dao.FeatureTogglePostGreDAO;
import com.awd.feature.toggle.services.dao.entities.StaticEnvironmentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FeatureToggleServiceImpl implements FeatureToggleService {
    @Autowired
    private FeatureTogglePostGreDAO featureTogglePostGreDAO;

    @Override
    public void createFeatureFlag(String user, String env, CreateFeatureRequest createFeatureRequest) throws Exception {
        validateEnvironment(env);
        final List<Feature> features = featureTogglePostGreDAO.getFeatureFlags(true);
        final boolean anyMatch = features.stream()
                .filter(feature -> feature.getEnv().getEnvName().equalsIgnoreCase(env))
                .map(Feature::getFeatureName)
                .anyMatch(feature -> feature.equals(createFeatureRequest.getFeatureName()));
        if (anyMatch) {
            log.error("Active feature flag already exists with name: {}", createFeatureRequest.getFeatureName());
            throw new Exception("Active Feature Flag already Exists"); // TODO custom exception handler
        }
        log.debug("Creating feature flag with name: {} in environment: {}", createFeatureRequest.getFeatureName(), env);
        featureTogglePostGreDAO.createFeatureFlag(user, createFeatureRequest, env);
    }

    @Override
    public List<Feature> getFeatureFlags() {
        return null;
    }

    @Override
    public List<Feature> getFeatureFlags(StaticEnvironments environment, FlagStates state) {
        return null;
    }

    @Override
    public void registerService(StaticEnvironments environment, RegisterServiceRequest registerServiceRequest) {

    }

    @Override
    public void updateFeatureFlag(String user, StaticEnvironments environment, Toggle toggleFlagValue, String featureName) {

    }

    @Override
    public void archiveFeatureFlag(String user, StaticEnvironments environment, String featureName) {

    }

    @Override
    public Optional<List<StaticEnvironmentEntity>> getAllEnvironments() {
        return featureTogglePostGreDAO.getAllEnvironments();
    }

    private void validateEnvironment(String env) throws Exception {
        boolean noneMatch = Arrays.stream(StaticEnvironments.values()).noneMatch(environment -> environment.getEnvName().equalsIgnoreCase(env));
        if (noneMatch) {
            log.error("Please verify the environment you are trying to create the toggle in: {}", env);
            throw new Exception("Invalid environment provided"); // TODO custom exception handler
        }
    }
}
