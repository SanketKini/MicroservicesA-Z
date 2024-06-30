package com.awd.feature.toggle.services.dao.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeaturesPKEntity {
    @Column(name = "FEATURE_NAME")
    String featureName;
    @Column(name = "ENVIRONMENT")
    String environment;

    @Override
    public String toString() {
        return "FEATURE_NAME" + featureName +
                "ENVIRONMENT" + environment;
    }
}
