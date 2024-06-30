package com.awd.feature.toggle.services.dao.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@Table(name = "STATIC_ENVIRONMENTS")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeaturesEntity {
    @Id
    FeaturesPKEntity pk;
    @Column(name = "ENABLED")
    boolean enabled;
    @Column(name = "CREATED_BY")
    String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "CREATED_ON")
    LocalDateTime createdOn;
    @Column(name = "UPDATED_BY")
    String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @Column(name = "UPDATED_ON")
    LocalDateTime updatedOn;
    @Column(name = "SUPPORTING_TICKET")
    String supportingTicket;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "ARCHIVED")
    boolean archived;
    @Column(name = "SERVICE_SET")
    String serviceSet;

    @Override
    public String toString() {
        return pk.toString() +
                "ENABLED" + enabled +
                "CREATED_BY" + createdBy +
                "CREATED_ON" + createdOn +
                "UPDATED_BY" + updatedBy +
                "UPDATED_ON" + updatedOn +
                "SUPPORTING_TICKET" + supportingTicket +
                "DESCRIPTION" + description +
                "ARCHIVED" + archived +
                "SERVICE_SET" + serviceSet;
    }
}
