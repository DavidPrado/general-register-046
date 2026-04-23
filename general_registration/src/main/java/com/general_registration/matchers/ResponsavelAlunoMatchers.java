package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class ResponsavelAlunoMatchers  {

    public static ExampleMatcher listResponsavelAlunoFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("idStudent", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("idPerson", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("kinship", m -> m.contains().ignoreCase());
    }
}
