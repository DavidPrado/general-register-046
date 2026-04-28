package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class AlojamentoAlunoViewMatcher {

    public static ExampleMatcher listAlojamentoAlunoViewFilter() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id")
                .withMatcher("roomNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("buildingBlock", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("studentName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("genderType", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("maxCapacity", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("statusAccommodation", ExampleMatcher.GenericPropertyMatchers.exact().ignoreCase())
                .withMatcher("entryDate", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("exitDate", ExampleMatcher.GenericPropertyMatchers.exact());

    }
}
