package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("v_internship_complete")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstagioView {
    @Id
    private UUID id;
    private UUID idStudent;
    private UUID idEnterprise;
    private UUID idEmployeeSupervisor;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double internshipSalary;
    private Integer hoursPerWeek;
    private Integer hoursPerDay;
    private Boolean terminationContract;
    private String terminationContractReason;
    private String studentName;
    private String studentCpf;
    private String enterpriseName;
    private String enterpriseCnpj;
}
