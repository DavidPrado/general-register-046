package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class FuncionarioMatchers {

    public static ExampleMatcher listFuncionarioFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }
}
