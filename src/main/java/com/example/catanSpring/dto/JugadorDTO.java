package com.example.catanSpring.dto;

import java.util.List;

import com.example.catanSpring.model.ColorJugador;

public record JugadorDTO(int id, String nom, ColorJugador color, int ordreTorn, List<RecursDTO> recursos) {

}
