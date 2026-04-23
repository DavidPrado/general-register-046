package com.general_registration.enums;


import lombok.Getter;

@Getter
public enum Parentesco {
    PAI("Pai"),
    MAE("Mãe"),
    AVO_PATERNO("Avô Paterno"),
    AVO_MATERNA("Avó Materna"),
    AVO_PATERNA("Avó Paterna"),
    AVO_MATERNO("Avô Materno"),
    TIO_TIA("Tio(a)"),
    IRMAO_IRMA("Irmão(ã)"),
    PADRASTO("Padrasto"),
    MADRASTA("Madrasta"),
    TUTOR_LEGAL("Tutor(a) Legal"),
    BISAVO_BISAVA("Bisavô(ã)"),
    CURADOR("Curador(a)"),
    FAMILIA_ACOLHEDORA("Família Acolhedora"),
    OUTROS("Outros");

    private final String descricao;

    Parentesco(String descricao) {
        this.descricao = descricao;
    }
}
