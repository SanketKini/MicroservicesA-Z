package com.awd.feature.toggle.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum FlagStates {
    ACTIVE, INACTIVE;
}
