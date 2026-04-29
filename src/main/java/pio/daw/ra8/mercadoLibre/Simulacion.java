package pio.daw.ra8.mercadoLibre;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import pio.daw.ra8.util.JPAUtil;


@Entity
/**
 * Ejecutar: mvn -q exec:java "-Dexec.mainClass=pio.daw.ra8.mercadoLibre.Simulacion"
 */
public class Simulacion {
    
    @Id
    @GeneratedValue

    private int id;
    private String nombre;
    private int numRondas;
    private int numIndividuos;
    private int saldoInicial;


    public static void main(String[] args){
        EntityManagerFactory emf = JPAUtil.crearEMF("target/Simulacion.odb");
        EntityManager em = emf.createEntityManager();

        try {
            
            em.getTransaction().begin();
            em.persist(new Individuo(1, "Pablo Motos", 100, 100));
            em.getTransaction().commit();

            // ── Consultar todos los Individuos (polimorfismo) ─────────────────
            System.out.println("── Todos los Individuos (Pablo Motos) ─────");
            List<Individuo> todos = em.createQuery(
                "SELECT I FROM Individuo I ORDER BY I.saldoActual DESC",
                Individuo.class
            ).getResultList();
            todos.forEach(I -> System.out.println("  " + I));


        } finally {
            em.close();
            emf.close();
        }
    }
    
}
