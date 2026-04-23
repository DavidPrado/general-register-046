package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class AlunoMatchers {

    public static  ExampleMatcher listAlunoFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("idPerson", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("idEnterprise", ExampleMatcher.GenericPropertyMatchers.exact());
    }

}
