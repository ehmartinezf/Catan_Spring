
import com.example.catanSpring.model.Assentament;
import com.example.catanSpring.model.Ciutat;
import com.example.catanSpring.model.Jugador;
import com.example.catanSpring.model.RecursJugador;
import com.example.catanSpring.model.TipusRecurs;
import org.hibernate.Hibernate;

    // ================= INVENTARI DEL JUGADOR =================
// Jugador (la vostra entitat) només té el Set<RecursJugador>; aquests mètodes el consulten i el modifiquen.
// Treballen sobre entitats gestionades: en acabar la transacció, Hibernate desa els canvis sol (dirty checking).

  

    public static int quantitat(Jugador j, TipusRecurs tipus) {
        for (RecursJugador r : j.getRecursos()) {
            if (r.getTipusRecurs() == tipus) {
                return r.getQuantitat();
            }
        }
        return 0;
    }

    // quantitat negativa = treure
    public static void afegirRecurs(Jugador j, TipusRecurs tipus, int quantitat) {
        for (RecursJugador r : j.getRecursos()) {
            if (r.getTipusRecurs() == tipus) {
                r.setQuantitat(r.getQuantitat() + quantitat);
                return;
            }
        }
    }

    public static int totalRecursos(Jugador j) {
        int total = 0;
        for (RecursJugador r : j.getRecursos()) {
            total += r.getQuantitat();
        }
        return total;
    }

    public static boolean potPagar(Jugador j, Map<TipusRecurs, Integer> cost) {
        for (Map.Entry<TipusRecurs, Integer> e : cost.entrySet()) {
            if (quantitat(j, e.getKey()) < e.getValue()) {
                return false;
            }
        }
        return true;
    }

    public static void pagar(Jugador j, Map<TipusRecurs, Integer> cost) {
        for (Map.Entry<TipusRecurs, Integer> e : cost.entrySet()) {
            afegirRecurs(j, e.getKey(), -e.getValue());
        }
    }

    // Producció d'un assentament: poblat 1, ciutat 2. Hibernate.getClass tria la classe REAL encara que sigui un proxy.
    public static int produccio(Assentament a) {
        return Hibernate.getClass(a) == Ciutat.class ? 2 : 1;
    }
