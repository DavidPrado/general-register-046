package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("v_accommodation_student_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlojamentoAlunoView {
    @Id
    private UUID id;
    private String roomNumber;
    private String buildingBlock;
    private Integer maxCapacity;
    private String genderType;
    private String studentName;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private String statusAccommodation;
}
