package com.example.catanSpring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catanSpring.dto.PartidaPeticio;
import com.example.catanSpring.dto.PartidaResposta;
import com.example.catanSpring.services.PartidaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/partida")
public class PartidaController {

	@Autowired
	private PartidaService service;
	
	
	@PostMapping
	public ResponseEntity<PartidaResposta> iniciarPartida(@Valid @RequestBody PartidaPeticio partida){
		return service.iniciarPartida(partida);
	}
	
}
