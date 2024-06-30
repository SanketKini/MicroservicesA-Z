package com.awd.feature.toggle.services.dao;

import com.awd.feature.toggle.model.*;
import com.awd.feature.toggle.services.dao.entities.FeaturesEntity;
import com.awd.feature.toggle.services.dao.entities.FeaturesPKEntity;
import com.awd.feature.toggle.services.dao.entities.StaticEnvironmentEntity;
import com.awd.feature.toggle.services.dao.repository.FeatureRepository;
import com.awd.feature.toggle.services.dao.repository.StaticEnvironmentRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class FeatureTogglePostGreDAOImpl implements FeatureTogglePostGreDAO {
    @Autowired
    private StaticEnvironmentRepository staticEnvironmentRepository;

    @Autowired
    private FeatureRepository featureRepository;

    @Override
    public void createFeatureFlag(String user, CreateFeatureRequest createFeatureRequest, String environment) {
        FeaturesPKEntity pk = FeaturesPKEntity.builder()
                .featureName(createFeatureRequest.getFeatureName())
                .environment(environment)
                .build();
        FeaturesEntity featuresEntity = FeaturesEntity.builder()
                .pk(pk)
                .enabled(createFeatureRequest.isEnabled())
                .createdBy(user)
                .createdOn(LocalDateTime.now())
                .updatedBy(user)
                .updatedOn(LocalDateTime.now())
                .supportingTicket(createFeatureRequest.getSupportingTicketRef())
                .description(createFeatureRequest.getComment())
                .archived(false)
                .build();
        featureRepository.save(featuresEntity);
    }

    @Override
    public List<Feature> getFeatureFlags(boolean excludeArchived) {
        List<FeaturesEntity> allFeatures = featureRepository.findAll();
        return allFeatures.stream().filter(feature -> !feature.isArchived())
                .map(featuresEntity -> Feature.builder()
                        .featureName(featuresEntity.getPk().getFeatureName())
                        .enabled(featuresEntity.isEnabled())
                        .archived(featuresEntity.isArchived())
                        .serviceSet(mapServiceList(featuresEntity.getServiceSet()))
                        .createdBy(featuresEntity.getCreatedBy())
                        .build())
                .toList();
    }

    private Set<String> mapServiceList(String serviceSet) {
        return StringUtils.isNotEmpty(serviceSet) ?
                new HashSet<>(Arrays.asList(serviceSet.split(","))) :
                new HashSet<>();
    }

    @Override
    public List<Feature> getFeatureFlags(StaticEnvironments environment, FlagStates state) {
        return null;
    }

    @Override
    public void updateServiceSet(StaticEnvironments environment, Set<String> featureNames, String serviceName, boolean add) {

    }

    @Override
    public void updateFeatureFlag(String user, StaticEnvironments environment, Toggle toggleFlagValue, String featureName) {

    }

    @Override
    public void archiveFeatureFlag(String user, StaticEnvironments environment, String featureName) {

    }

    @Override
    public Optional<List<StaticEnvironmentEntity>> getAllEnvironments() {
        return Optional.of(staticEnvironmentRepository.findAll());
    }
}
