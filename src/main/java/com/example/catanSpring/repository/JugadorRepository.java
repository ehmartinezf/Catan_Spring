package com.example.catanSpring.repository;

import com.example.catanSpring.model.Jugador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Integer> {

    List<Jugador> findByPartidaIdOrderByOrdreTorn(int idPartida);
}
