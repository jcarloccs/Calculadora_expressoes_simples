package model.entities;

import java.math.BigDecimal;

import model.enums.Operadores;
import model.enums.Parenteses;

public class Items {

    private BigDecimal numero;
    private Operadores operador;
    private Parenteses parenteses;

    public Items() {
    }

    public Items(BigDecimal numero) {
        this.numero = numero;
    }

    public Items(Operadores operador) {
        this.operador = operador;
    }

    public Items(Parenteses parenteses) {
        this.parenteses = parenteses;
    }

    public void resultado(BigDecimal numero, Operadores operador) {
        this.numero = numero;
        this.operador = operador;
    }

    public void repor(BigDecimal numero, Operadores operador, Parenteses parenteses) {
        this.numero = numero;
        this.operador = operador;
        this.parenteses = parenteses;
    }

    public BigDecimal getNumero() {
        return numero;
    }

    public Operadores getOperador() {
        return operador;
    }

    public Parenteses getParenteses() {
        return parenteses;
    }

    public void setParenteses(Parenteses parenteses) {
        this.parenteses = parenteses;
    }

    @Override
    public String toString() {
        return "Items [numero=" + numero.toString() + ", operador=" + operador + ", parenteses=" + parenteses + "]";
    }

}