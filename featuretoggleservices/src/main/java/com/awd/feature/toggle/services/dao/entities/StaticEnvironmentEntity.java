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
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "STATIC_ENVIRONMENTS")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StaticEnvironmentEntity {
    @Id
    @Column(name = "ENV_KEY")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long envKey;
    @Column(name = "ENV_NAME")
    String envName;
    @Column(name = "ENV_DESCRIPTION")
    String envDescription;
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

    @Override
    public String toString() {
        return "ENV_KEY" + envKey +
                "ENV_NAME" + envName +
                "ENV_DESCRIPTION" + envDescription +
                "CREATED_BY" + createdBy +
                "CREATED_ON" + createdOn +
                "UPDATED_BY" + updatedBy +
                "UPDATED_ON" + updatedOn;
    }
}
