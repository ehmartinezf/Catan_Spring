package com.example.catanSpring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catanSpring.services.PartidaService;

@RestController
@RequestMapping("/partida")
public class PartidaController {

	@Autowired
	private PartidaService service;
	
	
}
