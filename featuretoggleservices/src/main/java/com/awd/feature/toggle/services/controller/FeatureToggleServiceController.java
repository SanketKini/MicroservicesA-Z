package com.awd.feature.toggle.services.controller;

import com.awd.feature.toggle.services.dao.entities.StaticEnvironmentEntity;
import com.awd.feature.toggle.services.dto.StaticEnvDto;
import com.awd.feature.toggle.services.dto.StaticEnvListResponse;
import com.awd.feature.toggle.services.service.FeatureToggleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/features")
public class FeatureToggleServiceController {
    public static final String ENV = "env";
    public static final String STATE = "state";

    @Autowired
    private FeatureToggleService featureToggleService;

    @GetMapping(value = "/env", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StaticEnvListResponse> getAllEnvironments() {
        Optional<List<StaticEnvironmentEntity>> environments = featureToggleService.getAllEnvironments();
        if (environments.isPresent() && CollectionUtils.isNotEmpty(environments.get())) {
            List<StaticEnvDto> envResponseList = environments.get().stream()
                    .map(environment -> StaticEnvDto.builder()
                            .envKey(environment.getEnvKey())
                            .envName(environment.getEnvName())
                            .envDescription(environment.getEnvDescription())
                            .createdBy(environment.getCreatedBy())
                            .createdOn(environment.getCreatedOn())
                            .updatedBy(environment.getUpdatedBy())
                            .updatedOn(environment.getUpdatedOn())
                            .build())
                    .toList();
            return new ResponseEntity<>(StaticEnvListResponse.builder().envList(envResponseList).build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
