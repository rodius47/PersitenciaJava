package pio.daw.ra8.mercadoLibre;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import pio.daw.ra8.modelo.Producto;
import pio.daw.ra8.util.JPAUtil;

@Entity
public class Intercambio {
    
    @Id
    @GeneratedValue
    
    private String id;
    private String receptor;
    private String emisor;
    private int numIndividuos;
    private int saldoInicial;


    // public static void main(String[] args) {
    //     banner();
    //     EntityManagerFactory emf = JPAUtil.crearEMF("target/rollback.odb");
    //     EntityManager em = emf.createEntityManager();

    //     try {
    //         // ── Estado inicial (BBDD vacía) ──────────────────────────────────
    //         mostrarTotal(em, "Antes de la transacción");

    //         // ── Transacción que falla a mitad ────────────────────────────────
    //         System.out.println("\n── Transacción con error a mitad ───────────");
    //         EntityTransaction tx = em.getTransaction();
    //         try {
    //             tx.begin();
    //             System.out.println("  persist → Producto 1 (Teclado)");
    //             em.persist(new Producto("Teclado mecánico", 79.99, 10));

    //             System.out.println("  persist → Producto 2 (Ratón)");
    //             em.persist(new Producto("Ratón inalámbrico", 34.50, 20));

    //             System.out.println("  persist → Producto 3 (precio negativo = error)");
    //             Producto malo = new Producto("Artículo roto", -1.0, 5);
    //             // Simulamos una validación de negocio
    //             if (malo.getPrecio() < 0) {
    //                 throw new IllegalArgumentException(
    //                     "El precio no puede ser negativo: " + malo.getPrecio());
    //             }
    //             em.persist(malo);

    //             tx.commit();
    //             System.out.println("  commit — no debería llegar aquí");

    //         } catch (IllegalArgumentException e) {
    //             if (tx.isActive()) tx.rollback();
    //             System.out.println("  ✗ Error: " + e.getMessage());
    //             System.out.println("    → rollback ejecutado.");
    //         }

    //         // ── Verificar que la BBDD quedó vacía ────────────────────────────
    //         mostrarTotal(em, "Después del rollback");

    //         // ── Transacción correcta para contrastar ─────────────────────────
    //         System.out.println("\n── Transacción correcta (sin errores) ──────");
    //         tx = em.getTransaction();
    //         tx.begin();
    //         em.persist(new Producto("Teclado mecánico",   79.99, 10));
    //         em.persist(new Producto("Ratón inalámbrico",  34.50, 20));
    //         em.persist(new Producto("Monitor 24\"",      219.00,  5));
    //         tx.commit();
    //         System.out.println("  commit ejecutado.");
    //         mostrarTotal(em, "Tras commit exitoso");

    //         System.out.println("\n✓ Demo de rollback completada.");

    //     } finally {
    //         em.close();
    //         emf.close();
    //     }
    // }

    // private static void mostrarTotal(EntityManager em, String etiqueta) {
    //     em.clear();
    //     List<Producto> lista = em.createQuery(
    //         "SELECT p FROM Producto p", Producto.class
    //     ).getResultList();
    //     System.out.printf("  [%s] → %d productos en la BBDD%n", etiqueta, lista.size());
    //     lista.forEach(p -> System.out.println("    " + p));
    // }

    // private static void banner() {
    //     System.out.println("═".repeat(55));
    //     System.out.println("  EJEMPLO: Rollback – integridad ante errores");
    //     System.out.println("═".repeat(55));
    // }


    // ========================================================================================================================================================


    public static void main(String[] args) {
        // banner();
        EntityManagerFactory emf = JPAUtil.crearEMF("target/transferencia.odb");
        EntityManager em = emf.createEntityManager();

        try {
            // ── Preparar datos ───────────────────────────────────────────────
            em.getTransaction().begin();
            Producto almacenA = new Producto("Teclado (Almacén A)", 79.99, 20);
            Producto almacenB = new Producto("Teclado (Almacén B)", 79.99,  5);
            em.persist(almacenA);
            em.persist(almacenB);
            em.getTransaction().commit();

            long idA = almacenA.getId();
            long idB = almacenB.getId();
            // mostrarStock(em, idA, idB, "Estado inicial");

            // ── Escenario 1: transferencia exitosa ───────────────────────────
            System.out.println("\n── Escenario 1: transferir 8 unidades A→B ──");
            transferir(em, idA, idB, 8);
            // mostrarStock(em, idA, idB, "Tras transferencia exitosa");

            // ── Escenario 2: stock insuficiente → rollback ───────────────────
            System.out.println("\n── Escenario 2: transferir 20 unidades A→B ─");
            System.out.println("  (A solo tiene " + em.find(Producto.class, idA).getStock()
                               + " unidades → debe fallar)");
            transferir(em, idA, idB, 20);
            // mostrarStock(em, idA, idB, "Tras intento fallido (BBDD sin cambios)");

            System.out.println("\n✓ Demo de transacciones completada.");

        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * Transfiere {@code cantidad} unidades del producto {@code idOrigen} al {@code idDestino}.
     * Si no hay suficiente stock, lanza una excepción y hace rollback.
     */
    private static void transferir(EntityManager em, long idOrigen, long idDestino, int cantidad) {
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

            origen.setSaldoActual(origen.getSaldoActual()   - cantidad);
            destino.setSaldoActual(destino.getSaldoActual() + cantidad);

            tx.commit();
            System.out.println("  ✓ Transferencia de " + cantidad + " unidades confirmada.");

        } catch (IllegalStateException e) {
            if (tx.isActive()) tx.rollback();
            System.out.println("  ✗ Transferencia cancelada: " + e.getMessage());
            System.out.println("    → rollback ejecutado. La BBDD no ha cambiado.");
        }
    }

    // private static void mostrarStock(EntityManager em, long idA, long idB, String titulo) {
    //     em.clear(); // refrescar desde la BBDD
    //     System.out.println("  [" + titulo + "]");
    //     System.out.println("    Almacén A: saldo=" + em.find(Individuo.class, idA).getSaldoActual());
    //     System.out.println("    Almacén B: saldo=" + em.find(Individuo.class, idB).getSaldoActual());
    // }

    // private static void banner() {
    //     System.out.println("═".repeat(55));
    //     System.out.println("  EJEMPLO: Transacciones – transferencia de stock");
    //     System.out.println("═".repeat(55));
    // }
}
