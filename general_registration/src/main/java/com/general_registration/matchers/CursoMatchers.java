package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class CursoMatchers {
    public static ExampleMatcher listCursoFilter() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("name", m -> m.contains().ignoreCase())
                .withMatcher("description", m -> m.contains().ignoreCase())
                .withMatcher("active", ExampleMatcher.GenericPropertyMatchers.exact());
    }
}
