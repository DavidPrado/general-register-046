package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;


@Table("employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Funcionario extends BaseEntity {
    @Id
    private UUID id;
    private UUID idPerson;
    private UUID idEnterprise;
    private UUID idPositionEnterprise;
}
