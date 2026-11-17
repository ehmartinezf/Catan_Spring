package com.example.catanSpring.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.catanSpring.model.Jugador;
import com.example.catanSpring.repository.JugadorRepository;

/*
 * Mateixa lògica que JugadorDao, però amb repositories i @Transactional.
 * Els cossos de construirPoblat, construirCarretera, millorarACiutat i passarTorn són els dels mètodes PRIVATS del DAO
 * (els que rebien la Session), adaptats a les entitats de CatanSpring:
 *  - Session -> EntityManager, i session.find/persist -> repository.
 *  - Les consultes JPQL són a util/Consultes (les entitats no porten @NamedQuery).
 *  - Poblat, Ciutat i Carretera es creen amb new X() + setPropietari(...) + setVertex(...) / setAresta(...).
 *  - L'inventari (pagar, afegir recursos) són mètodes estàtics de ReglesJoc: Jugador no els té.
 */
@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    // ============================ GET /jugador/{id}/recursos ============================
    @Transactional(readOnly = true)
    public ResponseEntity<?> recursos(int idJugador) {
        Jugador j = jugadorRepository.findById(idJugador).orElse(null);
        if (j == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Missatges.JUGADOR_NO_EXISTEIX);
        }
        // El DTO es construeix DINS de la transacció: l'inventari (RecursJugador) encara es pot llegir
        return ResponseEntity.ok(PartidaService.aJugadorDTO(j));
    }
}
