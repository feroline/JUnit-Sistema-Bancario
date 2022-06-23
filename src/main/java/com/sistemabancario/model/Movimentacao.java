package com.sistemabancario.model;

import com.sun.nio.sctp.IllegalReceiveException;

import java.util.Random;

/**
 * Representa uma movimentação em uma {@link Conta} bancária, que pode indicar entrada ou saída.
 *
 * <p>
 * <b>NOTA</b>: Mesmo sendo possível obter o saldo somando-se todas as movimentações,
 * à medida que os dados no sistema aumentarem ao longo do tempo, 
 * calcular o saldo pode se tornar uma operação extremamente lenta.
 * Isto normalmente ocorrer quando o histórico de movimentações se torna longo
 * (principalmente depois de alguns anos).
 * </p>
 * 
 * @author Manoel Campos da Silva Filho
 */
public class Movimentacao implements Cadastro {
    private long id;
    private String descricao;

    /**
     * Conta bancária a qual a movimentação está vinculada.
     */
    private Conta conta;

    /**
     * Tipo da movimentação deve ser 'C' para crédito (entrada de dinheiro)
     * ou 'D' para débito (saída de dinheiro) (R01).
     */
    private char tipo;

    /**
     * Valor monetário da movimentação.
     * O valor não deve ser negativo, uma vez que existe o atributo {@link #tipo} (R02).
     * Se o tipo for débito, o valor da movimentação não pode ser superior ao saldo total da {@link Conta} (R03).
     */
    private double valor;

    /**
     * Indica se a movimentação foi confirmada, neste caso, devendo ser registrada no saldo da
     * conta, quando for adicionada à lista de movimentações usando
     * {@link Conta#addMovimentacao(Movimentacao)}.
     *
     * <ul>
     *  <li>Movimentacoes devem ser instanciadas como "confirmadas" por padrão (R04).</li>
     *  <li>
     *      Somente operações como depósito em cheque devem ser
     *      registradas inicialmente como não confirmadas. Após uma operação ser
     *      confirmada, deve-se atualizar o saldo da conta.
     *  <li>
     * </ul>
     *
     * @see Conta#depositoDinheiro(double)
     * @see Conta#depositoCheque(double)
     */
    private boolean confirmada;

    /**
     * Instancia uma movimentação para uma determinada {@link Conta} bancária,
     * onde a conta passada deve ser armazenada no atributo {@link #conta} (R05).
     * @param conta a {@link Conta} para vincular a movimentação.
     */
    public Movimentacao(Conta conta){

        this.setConfirmada(true);
        this.setConta(conta);

        //TODO: COLOCAR O VALOR ENTRE O ÚLTIMO E ÚLTIMO+1



    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public char getTipo(){
        return tipo;
    }

    public void setTipo(char tipo){
        if (tipo == 'D' || tipo == 'C') {
            this.tipo = tipo;
        }else{
            throw new IllegalArgumentException("Tipo inserido incorreto");
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }
    public void setValor(double valor) {
        if(valor <= 0 ){
            throw new IllegalArgumentException("O valor da movimentação deve ser maior que 0");
        }
        this.valor = valor;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public void setConfirmada(boolean confirmada) {
        this.confirmada = confirmada;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Conta getConta() {
        return this.conta;
    }

}