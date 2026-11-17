package com.example.catanSpring.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record PartidaPeticio(
		
		@NotEmpty(message="No hi ha noms de jugadors")
		List<String> noms,

		int puntsPerGuanyar,
		) {

}
