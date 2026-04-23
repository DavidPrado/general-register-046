package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("enterprise")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Empresa extends BaseEntity{
    @Id
    private UUID id;
    private String name;
    private String cnpj;
    private String email;
    private String phone;
    private String uf;
    private String city;
    private String neighborhood;
    private String street;
    private String cep;
    private String number;
    private String complement;
    private String stateRegistration;
    private String municipalRegistration;
    private String corporateName;
    private String fantasyName;
    private String cnae;
}
