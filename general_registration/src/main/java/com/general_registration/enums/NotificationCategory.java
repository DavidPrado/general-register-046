package com.general_registration.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum NotificationCategory {
    INFO("INFO"),
    SUCCESS("SUCCESS"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    URGENT("URGENT");

    private static final Map<String, NotificationCategory> BY_NAME =
            Arrays.stream(values())
                    .collect(Collectors.toMap(NotificationCategory::getValue, e -> e));

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static NotificationCategory fromValue(String value) {
        return BY_NAME.get(value);
    }
}