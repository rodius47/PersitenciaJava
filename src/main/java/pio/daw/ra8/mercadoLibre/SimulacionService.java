package pio.daw.ra8.mercadoLibre;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import pio.daw.ra8.util.JPAUtil;

public class SimulacionService {
    public static int getRandomId() {
        int range = (10 - 1) + 1;
        int random = (int) ((range * Math.random()) + 1);
        return random;
    }
    
     public static void main(String[] args){
        EntityManagerFactory emf = JPAUtil.crearEMF("target/Simulacion.odb");
        EntityManager em = emf.createEntityManager();

        try {
            
            em.getTransaction().begin();
            em.persist(new Individuo(1, "Pablo Motos", 100, 100));
            em.persist(new Individuo(2, "Lola Índigo", 100, 100));
            em.persist(new Individuo(3, "Jose María", 100, 100));
            em.persist(new Individuo(4, "Bad Bunny", 100, 100));
            em.persist(new Individuo(5, "Vito Quiles", 100, 100));
            em.persist(new Individuo(6, "Ronal Rigan", 100, 100));
            em.persist(new Individuo(7, "Kiko Perez", 100, 100));
            em.persist(new Individuo(8, "Eduardo Montilla", 100, 100));
            em.persist(new Individuo(9, "Pedro Ximenez", 100, 100));
            em.persist(new Individuo(10, "María Morlan", 100, 100));
            em.getTransaction().commit();


            // ── Consultar todos los Individuos (polimorfismo) ─────────────────
            System.out.println("── Todos los Individuos ---------------─────");
            List<Individuo> todos = em.createQuery(
                "SELECT I FROM Individuo I ORDER BY I.saldoActual DESC",
                Individuo.class
            ).getResultList();
            todos.forEach(I -> System.out.println("  " + I));



            // int range = (4 - 1) + 1;
     		// int random = (int) ((range * Math.random()) + 1);
            
            // int idOrigen = random.getRandomId(); // Pablo Motos
            // while(random != idOrigen){

            //     random = (int) ((range * Math.random()) + 1);
            //     int idDestino = random; // Lola Índigo

                


            // }
            
            
            // // // int idOrigen = SimulacionService.getRandomId();

            // // // do {
            // // //     int idDestino = SimulacionService.getRandomId();
            // // //     if(idDestino != idOrigen){
            // // //         transferir(em, idOrigen, idDestino, 10);
            // // //         break;
            // // //     }
            // // // } while (true);

            // // //  // ── Transaccion ─────────────────
            // // // System.out.println("── Todos los Individuos ---------------─────");
            // // // Individuo origen = em.createQuery(
            // // //     "SELECT I FROM Individuo I WHERE I.id = :idOrigen",
            // // //     Individuo.class
            // // // ).getSingleResult();
            // // // System.out.println("  " + origen);
            // // // Individuo destino = em.createQuery(
            // // //     "SELECT I FROM Individuo I WHERE I.id = :idDestino",
            // // //     Individuo.class
            // // // ).getSingleResult();
            // // // System.out.println("  " + destino); 
            

             // ── Escenario 1: transferencia exitosa ───────────────────────────
            for(int i = 0; i <100 ; i++){
                int idOrigen = SimulacionService.getRandomId();
                int idDestino = SimulacionService.getRandomId();
                while(idDestino == idOrigen){
                    idDestino = SimulacionService.getRandomId();
                }
                System.out.println("\n── Escenario 1: transferir 8 unidades A to B ──");
                transferir(em, idOrigen, idDestino, 10);
                mostrarStock(em, idOrigen, idDestino, "Tras transferencia exitosa"); //& Esto es para la prueva del codigo
            }
             System.out.println("── Todos los Individuos actualizados---------------─────");
            todos = em.createQuery(
                "SELECT I FROM Individuo I ORDER BY I.saldoActual DESC",
                Individuo.class
            ).getResultList();
            todos.forEach(I -> System.out.println("  " + I));


             System.out.println("──  Individuos  más rico y más pobreactualizados---------------─────");
             System.err.println("\n── Individuo más rico ──\n");
              todos = em.createQuery(
                "SELECT I FROM Individuo I ORDER BY I.saldoActual DESC",
                Individuo.class
            ).setMaxResults(1).getResultList();
            todos.forEach(I -> System.out.println("  " + I));
            //  List<Individuo> maximo = em.createQuery(
            //     "SELECT I FROM Individuo I WHERE I.saldoActual = (SELECT MAX(I2.saldoActual) FROM Individuo I2)",
            //     Individuo.class
            // ).getResultList();
             System.err.println("\n── Individuo más pobre ──\n");
             todos = em.createQuery(
                "SELECT I FROM Individuo I ORDER BY I.saldoActual ASC",
                Individuo.class
            ).setMaxResults(1).getResultList();
            todos.forEach(I -> System.out.println("  " + I));
            // List<Individuo> minimo = em.createQuery(
            //     "SELECT I FROM Individuo I WHERE I.saldoActual = (SELECT MIN(saldoActual) FROM Individuo) "
            // ).getResultList();
            // todos.forEach(I -> System.out.println("  " + I));
            System.err.println("\n── Individuo con más del 50% del saldo inicial ──\n");
             todos = em.createQuery(
                "SELECT I FROM Individuo I WHERE I.saldoActual >= 50 ORDER BY I.saldoActual DESC ",
                Individuo.class
            ).getResultList();
            todos.forEach(I -> System.out.println("  " + I));


        } finally {
            em.close();
            emf.close();
        }



    }

    private static void transferir(EntityManager em, int idOrigen, int idDestino, int cantidad) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Individuo origen  = em.find(Individuo.class, idOrigen);
            Individuo destino = em.find(Individuo.class, idDestino);

            if (origen.getSaldoActual() < cantidad) {
                throw new IllegalStateException(
                    "Saldo insuficiente: se piden " + cantidad
                    + " pero solo hay " + origen.getSaldoActual());
            }

            origen.setSaldoActual(origen.getSaldoActual() - cantidad);
            destino.setSaldoActual(destino.getSaldoActual() + cantidad);

            tx.commit();
            System.out.println("  OK Transferencia de " + cantidad + " unidades confirmada.");

        } catch (IllegalStateException e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("  ERRORER Transferencia cancelada: " + e.getMessage());
            System.out.println("    → rollback ejecutado. La BBDD no ha cambiado.");
        }
    }
    private static void mostrarStock(EntityManager em, int idA, int idB, String Nombre) {
        em.clear(); // refrescar desde la BBDD
        System.out.println("  [" + Nombre + "]");
        System.out.println("    Persona A: saldo actual=" + em.find(Individuo.class, idA).getSaldoActual());
        System.out.println("    Persona B: saldo actual=" + em.find(Individuo.class, idB).getSaldoActual());
    }
}
