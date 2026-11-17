package com.example.catanSpring.repositoris;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.catanSpring.model.Partida;

public interface PartidaRepository extends JpaRepository<Partida, Integer> {
	
}
