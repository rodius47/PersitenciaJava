package pio.daw.ra8.ejemplos.ejercicios.serializacion;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import pio.daw.ra8.ejemplos.serializacion.AlumnoSerial;

/**
 * BLOQUE 2 – Serialización de una colección de objetos.
 * Muestra que se puede serializar directamente un List<T>
 * siempre que T implemente Serializable.
 *
 * Ejecutar: mvn -q exec:java -Dexec.mainClass="pio.daw.ra8.ejemplos.serializacion.EjemploSerializarLista"
 */
public class EjemploSerializarLibros {

    private static final String FICHERO = "target/libros.dat";

    public static void main(String[] args) {
        banner();
        new File("target").mkdirs();

        // Crear la lista y datos de esta misma
        List<LibroSerial> libros = new ArrayList<>();
        libros.add(new LibroSerial("Un café con Venito", "Carlos Alfonso", 10.9));
        libros.add(new LibroSerial("La mañana de aller",   "María Morlan", 9.5));
        libros.add(new LibroSerial("Un mundo sin...",   "Blanca Venebiento", 12.5));

        System.out.println("Serializando " + libros.size() + " libros...");

        // Para guardar la lista 
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FICHERO))) {
            oos.writeObject(libros);
            System.out.println("Guardado en: " + FICHERO);
        } catch (IOException e) {
            System.err.println("Error al serializar: " + e.getMessage());
            return;
        }

        // Para recuperar la lista
        System.out.println("\nDeserializando...");
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FICHERO))) {

            @SuppressWarnings("unchecked")
            List<LibroSerial> recuperados = (List<LibroSerial>) ois.readObject();

            recuperados.forEach(a -> System.out.println("  - " + a));
            System.out.println("\n✓ Lista recuperada: " + recuperados.size() + " libros.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al deserializar: " + e.getMessage());
        }
    }

    private static void banner() {
        System.out.println("═".repeat(55));
        System.out.println("  EJEMPLO: Serializar una lista de Libros");
        System.out.println("═".repeat(55));
    }

}
