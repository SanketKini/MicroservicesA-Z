package com.awd.feature.toggle.services.dao.repository;

import com.awd.feature.toggle.services.dao.entities.FeaturesEntity;
import com.awd.feature.toggle.services.dao.entities.FeaturesPKEntity;
import com.awd.feature.toggle.services.dao.entities.StaticEnvironmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<FeaturesEntity, Object> {
    public FeaturesEntity findByPk(FeaturesPKEntity pk);
}
