package com.example.catanSpring.util;

/*
 * Totes les consultes JPQL del projecte, en un sol lloc.
 * Les entitats del grup no porten @NamedQuery, així que posem el text aquí com a constants
 * (Sessió 4: @Query("JPQL") als repositories, o em.createQuery(text, Classe.class) als serveis).
 * Són constants "static final String" perquè així també es poden posar dins d'una anotació: @Query(Consultes.X).
 * Els paràmetres amb nom (:idPartida) s'omplen amb setParameter("idPartida", valor) o amb @Param("idPartida").
 */
public final class Consultes {

    private Consultes() {
    }

    // ---- Assentaments (poblats i ciutats) ----
    public static final String ASSENTAMENT_PER_VERTEX
            = "select a from Assentament a where a.vertex.id = :idVertex";

    // Tots els assentaments d'una partida, amb el propietari ja carregat (JOIN FETCH: evita el problema N+1)
    public static final String ASSENTAMENT_PER_PARTIDA = """
            select a from Assentament a join fetch a.propietari
            where a.vertex.partida.id = :idPartida
            """;

    // Assentaments que toquen un hexàgon (els que cobren quan surt el seu número)
    public static final String ASSENTAMENT_PER_HEXAGON = """
            select a from Assentament a join fetch a.propietari
            where a.vertex.id in (select hv.vertex.id from HexagonVertex hv where hv.hexagon.id = :idHexagon)
            """;

    // Regla de la distància: assentaments als vèrtexs veïns (units per una aresta) del vèrtex indicat
    public static final String ASSENTAMENT_VEINS_OCUPATS = """
            select count(a) from Assentament a
            where a.vertex.id in (select ar.vertexB.id from Aresta ar where ar.vertexA.id = :idVertex)
               or a.vertex.id in (select ar.vertexA.id from Aresta ar where ar.vertexB.id = :idVertex)
            """;

    // ---- Carreteres ----
    public static final String CARRETERA_PER_ARESTA
            = "select count(c) from Carretera c where c.aresta.id = :idAresta";

    // Carreteres d'un jugador que arriben a un vèrtex (per una de les dues puntes de l'aresta)
    public static final String CARRETERA_TOCA_VERTEX = """
            select count(c) from Carretera c
            where c.propietari.id = :idJugador
              and (c.aresta.vertexA.id = :idVertex or c.aresta.vertexB.id = :idVertex)
            """;

    // ---- Altres ----
    public static final String CARTA_COMPTAR_MA
            = "select count(c) from CartaDesenvolupament c where c.jugador.id = :idJugador and c.tipus = :tipus";

    // type(c) = :tipus filtra per subclasse (Poblat, Ciutat o Carretera)
    public static final String CONSTRUCCIO_COMPTAR_PER_TIPUS
            = "select count(c) from Construccio c where c.propietari.id = :idJugador and type(c) = :tipus";

    public static final String HEXAGON_PER_NUMERO
            = "select h from Hexagon h where h.partida.id = :idPartida and h.numero = :numero";

    // Els vèrtexs de cada hexàgon de la partida, amb hexàgon i vèrtex carregats (JOIN FETCH)
    public static final String HEXAGON_VERTEX_PER_PARTIDA = """
            select hv from HexagonVertex hv
            join fetch hv.hexagon h
            join fetch hv.vertex v
            where h.partida.id = :idPartida
            order by h.codi, hv.posicio
            """;

    public static final String HEXAGON_VERTEX_PER_VERTEX = """
            select hv from HexagonVertex hv
            join fetch hv.hexagon
            where hv.vertex.id = :idVertex
            """;

    public static final String JUGADOR_PER_PARTIDA
            = "select j from Jugador j where j.partida.id = :idPartida order by j.ordreTorn";

    public static final String POBLAT_DEL_JUGADOR_AL_VERTEX
            = "select p from Poblat p where p.propietari.id = :idJugador and p.vertex.id = :idVertex";
}
