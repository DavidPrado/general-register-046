package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class PessoaMatchers {

    public static ExampleMatcher listPessoaFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("name", m -> m.contains().ignoreCase())
                .withMatcher("rg", m -> m.contains().ignoreCase())
                .withMatcher("cpf", m -> m.contains().ignoreCase())
                .withMatcher("email", m -> m.contains().ignoreCase())
                .withMatcher("phoneHome", m -> m.contains().ignoreCase())
                .withMatcher("phoneMobile", m -> m.contains().ignoreCase())
                .withMatcher("phoneWork", m -> m.contains().ignoreCase())
                .withMatcher("uf", m -> m.contains().ignoreCase())
                .withMatcher("city", m -> m.contains().ignoreCase())
                .withMatcher("neighborhood", m -> m.contains().ignoreCase())
                .withMatcher("street", m -> m.contains().ignoreCase())
                .withMatcher("number", m -> m.contains().ignoreCase())
                .withMatcher("complement", m -> m.contains().ignoreCase())
                .withMatcher("dateOfBirth", ExampleMatcher.GenericPropertyMatcher::exact);
    }

}
