package pio.daw.ra8.ejemplos.ejercicios.serializacion;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import pio.daw.ra8.ejemplos.relaciones.Libro;

public class LibroSerial implements Serializable{

    public static final Path rutaPorDefecto = Path.of(null);

    private String titulo;
    private String autor;
    private double precio;


    public LibroSerial(String titulo, String autor, double precio) {
        this.titulo = titulo;
        this.autor   = autor;
        this.precio  = precio;
    }
    
    public String getTitulo()           { return titulo; }
    public String getAutor()            { return autor; }
    public double getPrecio()           { return precio; }

    public void setTitulo(String Titulo){
        this.titulo = Titulo;
    }
    public void setAutor(String Autor){
        this.autor = Autor;
    }
    public void setPrecio(double Precio){
        this.precio = Precio;
    }

    @Override
    public String toString() {
        return "Libro{titulo='" + titulo + "', autor=" + autor + ", precio='" + precio + "'}";
    }
    // Para guardar la lista 
    public void guardar(Path ruta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ruta.toFile()))) {
            oos.writeObject(this);
            System.out.println("Guardado en: " + ruta);
        } catch (IOException e) {
            System.err.println("Error al serializar: " + e.getMessage());
            return;
        }
    }

    // Para recuperar la lista
    public static Libro cargar(Path ruta){
        Libro recuperado = null;
        try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(ruta.toFile()))) {

                recuperado = (Libro) ois.readObject();
                System.out.println("\n✓ Recuperado: " + recuperado);

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al deserializar: " + e.getMessage());
            }
            return recuperado;
    }
        

        // Para guardar la lista 

        
        // public void Cargar(Path ruta){
        //     // try (ObjectOutputStream oos = new ObjectOutputStream(
        //     //         new FileOutputStream(ruta.toFile()))) {
        //     //     oos.writeObject(ruta);
        //     //     System.out.println("Guardado en: " + ruta);
        //     // } catch (IOException e) {
        //     //     System.err.println("Error al serializar: " + e.getMessage());
        //     //     return;
        //     // }

        //     System.out.println("\nDeserializando...");
        //     try (ObjectInputStream ois = new ObjectInputStream(
        //             new FileInputStream(ruta.toFile()))) {

        //         @SuppressWarnings("unchecked")
        //         List<LibroSerial> recuperados = (List<LibroSerial>) ois.readObject();

        //         recuperados.forEach(a -> System.out.println("  - " + a));
        //         System.out.println("\n✓ Lista recuperada: " + recuperados.size() + " libros.");

        //     } catch (IOException | ClassNotFoundException e) {
        //         System.err.println("Error al deserializar: " + e.getMessage());
        //     }
        // }

        // Para recuperar la lista
        // System.out.println("\nDeserializando...");
        // try (ObjectInputStream ois = new ObjectInputStream(
        //         new FileInputStream(FICHERO))) {

        //     @SuppressWarnings("unchecked")
        //     List<LibroSerial> recuperados = (List<LibroSerial>) ois.readObject();

        //     recuperados.forEach(a -> System.out.println("  - " + a));
        //     System.out.println("\n✓ Lista recuperada: " + recuperados.size() + " libros.");

        // } catch (IOException | ClassNotFoundException e) {
        //     System.err.println("Error al deserializar: " + e.getMessage());
        // }
    public static void main(String[] args) {
        // Crear la lista y datos de esta misma
            List<LibroSerial> libros = new ArrayList<>();
            libros.add(new LibroSerial("Un café con Venito", "Carlos Alfonso", 10.9));
            libros.add(new LibroSerial("La mañana de aller",   "María Morlan", 9.5));
            libros.add(new LibroSerial("Un mundo sin...",   "Blanca Venebiento", 12.5));

            System.out.println(libros);

            libros.guardar(rutaPorDefecto);

            System.out.println("Libro guardado");

            Libro librosCargados = libros.cargar(rutaPorDefecto);

            // System.out.println("Serializando " + libros.size() + " libros...");
    }
}


