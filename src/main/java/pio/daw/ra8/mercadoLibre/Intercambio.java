package pio.daw.ra8.mercadoLibre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Intercambio {
    
    @Id
    @GeneratedValue
    
    private String id;
    private String receptor;
    private String emisor;
    private int numIndividuos;
    private int saldoInicial;
}
