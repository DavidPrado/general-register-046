package com.general_registration.matchers;

import org.springframework.data.domain.ExampleMatcher;


public class EstagioArquivoMatchers {

    private EstagioArquivoMatchers() {
    }

    public static ExampleMatcher listEstagioArquivo() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withMatcher("idInternship", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("fileName", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("fileDescription", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("fileSize", ExampleMatcher.GenericPropertyMatcher::exact);
    }
}
