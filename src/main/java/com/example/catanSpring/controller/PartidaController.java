package com.example.catanSpring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.catanSpring.service.PartidaService;

@RestController
@RequestMapping(path="/partida")
public class PartidaController {

	@Autowired
	private PartidaService service;

	@PostMapping(path="/{idPartida}/daus")
	public ResponseEntity<?> tirarDaus(@PathVariable int idPartida) {
		return service.tirarDaus(idPartida);
	}

}
