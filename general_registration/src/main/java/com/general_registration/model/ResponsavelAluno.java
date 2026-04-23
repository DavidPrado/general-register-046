package com.general_registration.model;


import com.general_registration.enums.Parentesco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("responsible_student")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsavelAluno extends BaseEntity{
    @Id
    private UUID id;
    private UUID idStudent;
    private UUID idPerson;
    private Parentesco kinship;
}
