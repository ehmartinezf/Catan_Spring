package com.example.catanSpring.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.catanSpring.dto.PartidaPeticio;
import com.example.catanSpring.dto.PartidaResposta;
import com.example.catanSpring.model.Aresta;
import com.example.catanSpring.model.CartaDesenvolupament;
import com.example.catanSpring.model.ColorJugador;
import com.example.catanSpring.model.Hexagon;
import com.example.catanSpring.model.HexagonVertex;
import com.example.catanSpring.model.Jugador;
import com.example.catanSpring.model.Partida;
import com.example.catanSpring.model.TipusCarta;
import com.example.catanSpring.model.TipusTerreny;
import com.example.catanSpring.model.Vertex;
import com.example.catanSpring.repository.CartaRepository;
import com.example.catanSpring.repository.ConstruccioRepository;
import com.example.catanSpring.repository.HexagonRepository;
import com.example.catanSpring.repository.JugadorRepository;
import com.example.catanSpring.repository.PartidaRepository;
import com.example.catanSpring.repository.VertexRepository;
import com.example.catanSpring.util.CsvLector;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class PartidaService {

	@Autowired
	private PartidaRepository repository;
	
	@Autowired 
	private VertexRepository vRepository;
	
	@Autowired 
	private HexagonRepository hRepository;
	
	@Autowired
	private JugadorRepository jRepository;
	
	@Autowired
	private ConstruccioRepository cRepository;
	
	@Autowired
	private CartaRepository cartaRepository;
	
	@Transactional
	public ResponseEntity<PartidaResposta> iniciarPartida(PartidaPeticio pp){
		 if (pp.noms() == null || pp.noms().size() < 2 || pp.noms().size() > 4) {
	            System.out.println("Hi ha d'haver entre 2 i 4 jugadors");
	            return ResponseEntity.badRequest().body(null);
	        }
	        for (String nom : pp.noms()) {
	            if (nom == null || nom.isBlank()) {
	                System.out.println("Cap jugador pot tenir el nom buit");
	                return ResponseEntity.badRequest().body(null);
	            }
	        }
	        if (pp.puntsPerGuanyar() < 3) {
	            System.out.println("Els punts per guanyar han de ser com a mínim 3");
	            return ResponseEntity.badRequest().body(null);
	        }
	        
	        Partida partida = new Partida(pp.puntsPerGuanyar());
		return ResponseEntity.ok(PartidaResposta.de(partida));
	}
	

    private Partida crearPartida(Session session, List<String> noms, int puntsPerGuanyar) {
        Partida partida = new Partida(puntsPerGuanyar);
        repository.save(partida);
        creaTauler(partida);
        List<Jugador> jugadors = creaJugadors(session, partida, noms);
        creaPila(partida);
        partida.setJugadorActiu(jugadors.get(0));
        return partida;
    }

    private void creaTauler(Partida partida) {
        Map<Integer, Vertex> vertexs = new LinkedHashMap<>();
        for (String[] fila : CsvLector.llegir("vertexs.csv")) {
            int codi = Integer.parseInt(fila[0]);
            Vertex v = new Vertex(codi);
            partida.afegirVertex(v);
            vRepository.save(v);
            vertexs.put(codi, v);
        }

        for (String[] fila : CsvLector.llegir("arestes.csv")) {
            Aresta a = new Aresta(Integer.parseInt(fila[0]),
                    obtenir(vertexs, Integer.parseInt(fila[1]), "arestes.csv"),
                    obtenir(vertexs, Integer.parseInt(fila[2]), "arestes.csv"));
            partida.afegirAresta(a);
            cRepository.save(a);
        }

        List<TipusTerreny> terrenys = new ArrayList<>();
        afegir(terrenys, TipusTerreny.BOSC, 4);
        afegir(terrenys, TipusTerreny.PASTURES, 4);
        afegir(terrenys, TipusTerreny.CAMPS, 4);
        afegir(terrenys, TipusTerreny.TURONS, 3);
        afegir(terrenys, TipusTerreny.MUNTANYES, 3);
        afegir(terrenys, TipusTerreny.DESERT, 1);
        Collections.shuffle(terrenys);

        List<Integer> numeros = new ArrayList<>(List.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
        Collections.shuffle(numeros);

        List<String[]> filesHexagons = CsvLector.llegir("hexagons.csv");
        Map<Integer, Hexagon> hexagons = new LinkedHashMap<>();
        int i = 0;
        for (String[] fila : filesHexagons) {
            Hexagon h = new Hexagon(Integer.parseInt(fila[0]), Integer.parseInt(fila[1]), Integer.parseInt(fila[2]));
            TipusTerreny terreny = terrenys.get(i++);
            h.setTerreny(terreny);
            h.setNumero(terreny == TipusTerreny.DESERT ? null : numeros.remove(0));
            partida.afegirHexagon(h);
            hexagons.put(h.getCodi(), h);
        }

        for (String[] fila : CsvLector.llegir("hexagon_vertex.csv")) {
            Hexagon h = obtenir(hexagons, Integer.parseInt(fila[0]), "hexagon_vertex.csv");
            Vertex v = obtenir(vertexs, Integer.parseInt(fila[1]), "hexagon_vertex.csv");
            HexagonVertex hv = new HexagonVertex(v, Integer.parseInt(fila[2]));
            h.afegirHexagonVertex(hv);
        }

        for (Hexagon h : hexagons.values()) {
        	hRepository.save(h);
        	if (h.getTerreny() == TipusTerreny.DESERT) {
                partida.setHexagonLladre(h);
            }
        }
    }
    
    private List<Jugador> creaJugadors(EntityManager em, Partida partida, List<String> noms) {
        List<String> ordre = new ArrayList<>(noms);
        Collections.shuffle(ordre);
        List<Jugador> jugadors = new ArrayList<>();
        for (int i = 0; i < ordre.size(); i++) {
            Jugador j = new Jugador(ordre.get(i).trim(), ColorJugador.values()[i], i + 1, partida);
            partida.afegirJugador(j);
            jRepository.save(j);
            jugadors.add(j);
        }
        return jugadors;
    }

    private void creaPila(EntityManager em, Partida partida) {
        List<CartaDesenvolupament> pila = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            pila.add(new CartaDesenvolupament(TipusCarta.CAVALLER));
        }
        for (int i = 0; i < 5; i++) {
            pila.add(new CartaDesenvolupament(TipusCarta.PUNT_VICTORIA));
        }
        for (int i = 0; i < 2; i++) {
            pila.add(new CartaDesenvolupament(TipusCarta.MONOPOLI));
        }
        for (CartaDesenvolupament c : pila) {
            partida.afegirCarta(c);
            cartaRepository.save(c);
        }
    }

    private static <T> void afegir(List<T> llista, T valor, int vegades) {
        for (int i = 0; i < vegades; i++) {
            llista.add(valor);
        }
    }

    private static <T> T obtenir(Map<Integer, T> mapa, int codi, String fitxer) {
        T valor = mapa.get(codi);
        if (valor == null) {
            throw new IllegalStateException(fitxer + ": no existeix el codi " + codi);
        }
        return valor;
    }
}
