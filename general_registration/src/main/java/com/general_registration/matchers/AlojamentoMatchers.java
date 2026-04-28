package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class AlojamentoMatchers {

    public static ExampleMatcher listAlojamentoFilter() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("roomNumber", m -> m.contains().ignoreCase())
                .withMatcher("buildingBlock", m -> m.contains().ignoreCase())
                .withMatcher("genderType", m -> m.contains().ignoreCase())
                .withMatcher("status", m -> m.contains().ignoreCase())
                .withMatcher("maxCapacity", ExampleMatcher.GenericPropertyMatcher::exact);
    }
}
