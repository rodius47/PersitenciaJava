package pio.daw.ra8.ejemplos.serializacion;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import pio.daw.ra8.ejemplos.relaciones.Libro;
import pio.daw.ra8.modelo.Categoria;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;

@Entity
public class Autor {

        @Id @GeneratedValue
    private long id;
    private String nombre;
    private String autor;

     @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private static List<Libro> libros = new ArrayList<>();





    public long getId() {
        return id;
    }

     public void setId(long id) {
         this.id = id;
     }

     public String getNombre() {
         return nombre;
     }

     public void setNombre(String nombre) {
         this.nombre = nombre;
     }

     public String getAutor() {
         return autor;
     }

     public void setAutor(String autor) {
         this.autor = autor;
     }

     public static List<Libro> getLibros() {
         return libros;
     }

     public void setLibros(List<Libro> libros) {
         this.libros = libros;
     }

    public Autor(String string) {

    }

    public static void main(String[] args) {
         EntityManagerFactory emf = JPAUtil.crearEMF("target/paginacion.odb");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Autor Lorca = new Autor("Lorca");
            Libro LaGalatea = new Libro("La Galatea", "Lorca", 49.99, 10);
            LaGalatea.setAutor("Lorca");
            Autor.getLibros().add(LaGalatea);

            em.persist(Lorca);  // CascadeType.ALL persiste también los productos
            em.getTransaction().commit();
                        
            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


}

