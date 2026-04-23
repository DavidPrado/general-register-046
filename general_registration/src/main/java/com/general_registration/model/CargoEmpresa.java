package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("position_enterprise")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CargoEmpresa extends BaseEntity {

    @Id
    private UUID id;
    private String position;
    private String description;
    private Boolean active;
}
