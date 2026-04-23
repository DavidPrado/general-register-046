package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class EmpresaMatchers {


    public static ExampleMatcher listEmpresaFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("name", m -> m.contains().ignoreCase())
                .withMatcher("cnpj", m -> m.contains().ignoreCase())
                .withMatcher("email", m -> m.contains().ignoreCase())
                .withMatcher("phone", m -> m.contains().ignoreCase())
                .withMatcher("uf", m -> m.contains().ignoreCase())
                .withMatcher("city", m -> m.contains().ignoreCase())
                .withMatcher("neighborhood", m -> m.contains().ignoreCase())
                .withMatcher("street", m -> m.contains().ignoreCase())
                .withMatcher("cep", m -> m.contains().ignoreCase())
                .withMatcher("number", m -> m.contains().ignoreCase())
                .withMatcher("complement", m -> m.contains().ignoreCase())
                .withMatcher("stateRegistration", m -> m.contains().ignoreCase())
                .withMatcher("municipalRegistration", m -> m.contains().ignoreCase())
                .withMatcher("corporateName", m -> m.contains().ignoreCase())
                .withMatcher("fantasyName", m -> m.contains().ignoreCase())
                .withMatcher("cnae", m -> m.contains().ignoreCase());
    }


}
