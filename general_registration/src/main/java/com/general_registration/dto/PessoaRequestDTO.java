package com.general_registration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PessoaRequestDTO(
        @NotBlank(message = "O campo 'name' é obrigatório.")
        String name,
        String rg,
        @CPF(message = "CPF inválido ou inexistente")
        String cpf,
        @Email(message = "E-mail inválido")
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,
        String phoneHome,
        String phoneMobile,
        String phoneWork,
        String uf,
        String city,
        String neighborhood,
        String street,
        String number,
        String complement
) {
}
