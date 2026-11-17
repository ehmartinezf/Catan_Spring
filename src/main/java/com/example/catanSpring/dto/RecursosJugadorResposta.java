package com.example.catanSpring.dto;
import java.util.Map;
import com.example.catanSpring.model.Jugador;
import com.example.catanSpring.model.TipusRecurs;

public record RecursosJugadorResposta(Integer jugadorId, String nom, Map<TipusRecurs, Integer> recursosRebuts) {
	
	public static RecursosJugadorResposta de(Jugador j, Map<TipusRecurs, Integer> recursosRebuts) {
		return new RecursosJugadorResposta(j.getId(), j.getNom(), recursosRebuts);
	}

}