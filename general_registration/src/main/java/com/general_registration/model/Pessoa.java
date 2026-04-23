package com.general_registration.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("person")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pessoa extends BaseEntity {

    @Id
    private UUID id;
    private String name;
    private String rg;
    private String cpf;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneHome;
    private String phoneMobile;
    private String phoneWork;
    private String uf;
    private String city;
    private String neighborhood;
    private String street;
    private String number;
    private String complement;
}
