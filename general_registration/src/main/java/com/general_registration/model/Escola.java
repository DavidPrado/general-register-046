package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("school")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Escola extends BaseEntity {
    @Id
    private UUID id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private String uf;
    private String city;
    private String neighborhood;
    private String street;
    private String number;
    private String complement;
}
