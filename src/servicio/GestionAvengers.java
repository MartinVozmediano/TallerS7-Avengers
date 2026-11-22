package servicio;

import modelo.Avenger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * IMPLEMENTACIÓN PROVISIONAL para ejecutar la GUI.
 * Integrante 2: sustituir este archivo por su implementación con HashMap.
 * Conservar exactamente los cuatro métodos públicos y sus firmas.
 */
public class GestionAvengers {
    private final HashMap<Integer, Avenger> avengers = new HashMap<>();

    public boolean registrarAvenger(Avenger avenger) {
        if (avenger == null || avengers.containsKey(avenger.getId())) return false;
        avengers.put(avenger.getId(), avenger);
        return true;
    }

    public Avenger buscarAvenger(int id) {
        return avengers.get(id);
    }

    public boolean modificarAvenger(Avenger avenger) {
        if (avenger == null || !avengers.containsKey(avenger.getId())) return false;
        avengers.put(avenger.getId(), avenger);
        return true;
    }

    public ArrayList<Avenger> listarAvengers() {
        ArrayList<Avenger> lista = new ArrayList<>(avengers.values());
        lista.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        return lista;
    }
}
