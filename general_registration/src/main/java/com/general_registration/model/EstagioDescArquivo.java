package com.general_registration.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("internship_files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstagioDescArquivo extends BaseEntity {

    @Id
    private UUID id;
    private UUID idInternship;
    private String fileName;
    private String fileDescription;
    private Integer fileSize;
    private String mimeType;
}
