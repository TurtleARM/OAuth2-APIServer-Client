package com.unimi.apiserver.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Ordine {
    private String sedeOrdine;
    private char tipoOrdine;
}
