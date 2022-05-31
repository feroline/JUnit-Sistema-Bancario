package com.sistemabancario.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContaTest {
    @Test
    void setNumeroR1(){
        final Conta conta = new Conta();
        final String invalido = "123";
        assertThrows(IllegalArgumentException.class, () -> conta.setNumero(invalido));
        final String obtido = conta.getNumero();
        assertNotEquals(invalido, obtido);
    }

    @Test
    void setNumeroR1a(){
        final Conta conta = new Conta();
        final String invalido = "abcde-f";
        //assertThrows == tem que dar erro
        assertThrows(IllegalArgumentException.class, () -> conta.setNumero(invalido));
    }

    @Test
    void setNumeroR1b(){
        final Conta conta = new Conta();
        final String esperado = "12345-6";
        conta.setNumero(esperado);
        final String obtido = conta.getNumero();
        assertEquals(esperado, obtido);
    }

    @Test
    void setNumeroR1c(){
        final Conta conta = new Conta();
        final String invalido = "12345-f";
        assertThrows(IllegalArgumentException.class, () -> conta.setNumero(invalido));
    }

}
