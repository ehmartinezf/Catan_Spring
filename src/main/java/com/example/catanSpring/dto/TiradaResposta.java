package com.example.catanSpring.dto;

import java.util.List;

public record TiradaResposta(int dau1, int dau2, int total, boolean lladrePendent, List<RecursosJugadorResposta> produccio) {

}