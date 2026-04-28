package com.general_registration.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("accommodation_student")
public class AlojamentoAluno extends BaseEntity {

    @Id
    private UUID id;
    private UUID idAccommodation;
    private UUID idStudent;
    private LocalDate entryDate;
    private LocalDate exitDate;
}
