package pio.daw.ra8.mercadoLibre;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Individuo {
    
    @Id
    @GeneratedValue

    private int id;
    private String nombre;
    private int saldoActual;
    private int saldoInicial;
    
    public Individuo(int id, String nombre, int saldoActual, int saldoInicial) {
        this.id = id;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.saldoInicial = saldoInicial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(int saldoActual) {
        this.saldoActual = saldoActual;
    }

    public int getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(int saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
               + "{id=" + id + ", nombre='" + nombre
               + "', saldo Inicial='" + saldoInicial + "', saldo Actual=" + saldoActual + "}";
    }
}
