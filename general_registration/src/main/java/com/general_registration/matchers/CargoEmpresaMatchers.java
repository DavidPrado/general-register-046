package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class CargoEmpresaMatchers {


    public static ExampleMatcher listCargoEmpresaFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("position", m -> m.contains().ignoreCase())
                .withMatcher("description", m -> m.contains().ignoreCase())
                .withMatcher("active", ExampleMatcher.GenericPropertyMatchers.exact());
    }
}
