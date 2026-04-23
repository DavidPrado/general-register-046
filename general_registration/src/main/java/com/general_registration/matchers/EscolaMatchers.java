package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class EscolaMatchers {

    public static ExampleMatcher listEscolaFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("code", m -> m.contains().ignoreCase())
                .withMatcher("name", m -> m.contains().ignoreCase())
                .withMatcher("email", m -> m.contains().ignoreCase())
                .withMatcher("phone", m -> m.contains().ignoreCase())
                .withMatcher("uf", m -> m.contains().ignoreCase())
                .withMatcher("city", m -> m.contains().ignoreCase())
                .withMatcher("neighborhood", m -> m.contains().ignoreCase())
                .withMatcher("street", m -> m.contains().ignoreCase())
                .withMatcher("number", m -> m.contains().ignoreCase())
                .withMatcher("complement", m -> m.contains().ignoreCase());

    }
}
