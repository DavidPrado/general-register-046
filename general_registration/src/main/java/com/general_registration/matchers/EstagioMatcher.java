package com.general_registration.matchers;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@NoArgsConstructor
public class EstagioMatcher {

    public static ExampleMatcher listEstagioFilter(){
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("idStudent", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("idEnterprise", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("idEmployeeSupervisor", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("startDate", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("endDate", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("intershipSalary", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("hoursPerWeek", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("hoursPerDay", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("terminationContract", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("terminationContractReason", m -> m.contains().ignoreCase());
    }

    public static ExampleMatcher listViewFilter() {
        return ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
                .withMatcher("idStudent", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("idEnterprise", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("idEmployeeSupervisor", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("startDate", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("endDate", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("intershipSalary", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("hoursPerWeek", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("hoursPerDay", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("terminationContract", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("terminationContractReason", m -> m.contains().ignoreCase())
                .withMatcher("studentName", m -> m.contains().ignoreCase())
                .withMatcher("studentCpf", m -> m.contains().ignoreCase())
                .withMatcher("enterpriseName", m -> m.contains().ignoreCase())
                .withMatcher("enterpriseCnpj", m -> m.contains().ignoreCase());
    }
}
