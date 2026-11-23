package servicio;

import modelo.Avenger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


public class GestionAvengers {
    /**
     * Mapa que almacena los Avengers registrados, donde la clave es el ID del Avenger.
     */
    private final HashMap<Integer, Avenger> avengers = new HashMap<>();

    /**
     * Registra un nuevo Avenger en el sistema.
     *
     * @param avenger El objeto Avenger a registrar.
     * @return true si el registro fue exitoso, false si el Avenger ya existe o es nulo.
     */
    public boolean registrarAvenger(Avenger avenger) {
        if (avenger == null) {
            return false;
        }

        int id = avenger.getId();
        if (avengers.containsKey(id)) {
            return false;
        }

        for (Avenger a : avengers.values()) {
            if (a.getNombre().equalsIgnoreCase(avenger.getNombre().trim())) {
                return false;
            }
        }

        if(avenger.getNombre() == null ||
                avenger.getNombre().isBlank() ||
                !avenger.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$")){
            return false;
        }

        avengers.put(id, avenger);
        return true;
    }

    /**
     * Busca un Avenger en el sistema por su ID.
     *
     * @param id El ID del Avenger a buscar.
     * @return El objeto Avenger si se encuentra, null si no.
     */
    public Avenger buscarAvenger(int id) {

        if (id <= 0) {
            return null;
        }

        return avengers.get(id);
    }

    /**
     * Modifica la información de un Avenger existente.
     *
     * @param avenger El objeto Avenger con la información actualizada.
     * @return true si la modificación fue exitosa, false si el Avenger no existe o es nulo.
     */
    public boolean modificarAvenger(Avenger avenger) {
        if (avenger == null || !avengers.containsKey(avenger.getId())) return false;
        avengers.replace(avenger.getId(), avenger); //replace ya que put no lanza excepción si la clave no existe, pero replace sí.
        return true;
    }

    public ArrayList<Avenger> listarAvengers() {
        ArrayList<Avenger> lista = new ArrayList<>(avengers.values());
        lista.sort(Comparator.comparingInt(Avenger::getId));
        return lista;
    }
}
