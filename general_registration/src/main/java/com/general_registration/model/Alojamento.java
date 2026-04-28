package com.general_registration.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("accommodation")
public class Alojamento extends BaseEntity {

    @Id
    private UUID id;
    private String roomNumber;
    private String buildingBlock;
    private Integer maxCapacity;
    private Double squareMeters;
    private String genderType;
    private String status;

}
