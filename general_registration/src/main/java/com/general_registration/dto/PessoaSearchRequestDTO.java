package com.general_registration.dto;

import java.time.LocalDate;

public record PessoaSearchRequestDTO(
        String name,
        String rg,
        String cpf,
        String email,
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
