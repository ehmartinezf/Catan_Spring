package com.example.catanSpring.dto;

import com.example.catanSpring.model.Partida;

public record PartidaResposta(
		
	public static PartidaResposta de(Partida p) {
		return new PartidaResposta();
	}
		) {

}
