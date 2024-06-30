package com.awd.feature.toggle.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum StaticEnvironments {
    DEV("dev"), TEST("test"), UAT("uat"), PROD("prod"), ALL("ALL");

    private String envName;

    StaticEnvironments(String envName) {
        this.envName = envName;
    }
}
