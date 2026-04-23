package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class CursoAlunoMatchers {

    public static ExampleMatcher listCursoAlunoFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("idStudent", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("idCourse", ExampleMatcher.GenericPropertyMatchers.exact());
    }

}
