package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class NotificationMatchers {

    public static ExampleMatcher listNotificationFilter() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("title", m -> m.contains().ignoreCase())
                .withMatcher("message", m -> m.contains().ignoreCase());
    }
}
