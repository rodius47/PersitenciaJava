package pio.daw.ra8.ejemplos.ejercicios.serializacion.jpa;

import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import pio.daw.ra8.util.JPAUtil;

public class ConsolaLibro {
    
    private EntityManager em;

    public ConsolaLibro(){
        EntityManagerFactory emf = JPAUtil.crearEMFWithoutDelete("target/ConsolaLibro.odb");
        this.em = emf.createEntityManager();
    }

    public void printMenu(){
        StringBuilder sb = new StringBuilder();
        sb.append("1 - Añade un libro\n");
        sb.append("2 - Lista los libros\n");
        sb.append("3 - Actualiza un libro\n");
        sb.append("4 - Borrar libro\n");
        sb.append("5 - Salir\n");
        System.out.println(sb.toString());

    }

    private String askForString(Scanner sc, String name){
        System.out.print(name + ": ");
        String nombre = sc.nextLine();
        return nombre;

    }
    
    private double askForDouble(Scanner sc, String precio){
        System.out.print(precio + ": ");
        double elPrecio = sc.nextDouble();
        return elPrecio;

    }

    private double askForInt(Scanner sc, int i){
        System.out.print(i + ": ");
        int in = sc.nextInt();
        return in;

    }
    
    Scanner sc = new Scanner(System.in);

    public void añadirLibro(){

        String nombre = askForString(sc, "Nombre");
        String autor = askForString(sc, "Autor");
        String ISBN = askForString(sc, "ISBN");
        double precio = askForDouble(sc, "Precio");

        this.em.getTransaction().begin();
        this.em.persist(new Libro(ISBN, nombre, autor, precio));
        this.em.getTransaction().commit();

        System.out.println("Añadido a la BBDD: " + this.em.find(Libro.class, ISBN));

    }

    public void listarLibros(){
        this.em.createQuery("SELECT l FROM Libro l ORDER BY l.titulo", Libro.class)
        .getResultList()
        .forEach(l -> System.out.println(l));
    }

    /**
     * Pregunta por el ISBN del Libro a actualizar. Se comprueba que ese codigo pertenece a un libro existente.
     * Preguntamos por los campos nombre, autor y precio. Si el usuario no introduce texto y le da a enter mantenemos
     * la informacion previa de ese campo. Ademas, en el nombre comprobamos que el nuevo nombre no corresponde
     * a nigun libro en la BBDD. Finalmente ejecutamso la actualizacion.
     * @param ISBN
     */
    public void actualizarLibro(){
        System.out.println("\n── Actualizando el Libro ───────────");
        // Libro actualizar = em.find(Libro.class, ISBN);
        Libro actualizar = null;
        Scanner sc2 = new Scanner(System.in);
        String ISBN = null;

        do {
            ISBN = askFoString(sc2, "ISBN or cancel ");
            if(ISBN.toLowerCase().equals("cancel")){
                return;
            }
            actualizar = em.find(Libro.class, ISBN);
        } while (actualizar == null);




        System.out.print("\n Nombre del Libro: ");
        String titulo = askForString(sc2, String.format("Nombre (%s)", actualizar.getTitulo()) );
        if(em.createQuery("SELECT l FROM Libro l WHERE l.titulo == :title", Libro.class)
            .setParameter("title", titulo).getMaxResults() == 0){
            System.err.println("ERROR: No puedes introducir el nombre de un libro existente");
            return;
        }
        titulo = (titulo.isEmpty()) ? actualizar.getTitulo() : titulo;



            
            
            // System.out.print("\n Nombre para el Libro: ");
            // String nombre = sc2.nextLine();
        System.out.print("\n Autor para el Libro: ");
        String autor = askForString(sc2, String.format("Autor (%s)", actualizar.getAutor()) );
        
        autor = (autor.isEmpty()) ? actualizar.getAutor() : autor;
            // String autor = sc2.nextLine();




        System.out.print("\n Precio para el Libro: ");
        double precio = askForDouble(sc2, String.format("Precio (%d)", actualizar.getPrecio()) );
        precio = (precio.isEmpty()) ? actualizar.getPrecio() : precio;
            // String precio = sc2.nextLine();




            em.getTransaction().begin();
            actualizar.setTitulo(titulo);
            actualizar.setTitulo(autor);
            actualizar.setTitulo(precio);
            em.getTransaction().commit();
            System.out.println("  " + em.find(Libro.class, ISBN));
        


        sc2.close();
    }

    public void borrarLibro(){

        String ISBN = askFoString(sc, "ISBN");
        Libro l = em.
        

    }

    public void run(){
        int input = 0;
        do {printMenu();
            input = askForInt(sc, "Acción");
            switch (input) {
                case 1:
                    añadirLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    actualizarLibro();
                    break;
                case 4:
                    borrarLibro();
                    break;
            
                default:
                    break;
            }
            
        } while (input != 5);
    }

    public static void main(String[] args){

    }

}
