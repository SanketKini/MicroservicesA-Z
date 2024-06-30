package com.awd.feature.toggle.services.dao.repository;

import com.awd.feature.toggle.services.dao.entities.StaticEnvironmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticEnvironmentRepository extends JpaRepository<StaticEnvironmentEntity, Object> {
}
