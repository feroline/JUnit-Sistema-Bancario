package com.sistemabancario.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import javax.lang.model.type.NullType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste base para implementação dos testes
 * unitários para a classe {@link Movimentacao}.
 * Os testes foram gerados pelo IDE apenas como modelo
 * (fiz apenas algumas melhorias de boas práticas 
 * como tirar visibilidade public e definir variáveis como final).
 * 
 * Assim, NENHUM DELES FUNCIONA E O CÓDIGO PRECISA SER ALTERADO
 * de acordo com as regras de negócio dos métodos da classe {@link Movimentacao}.
 * Ao iniciar a alteração de um teste aqui,
 * a primeira coisa a fazer é remover a chamada de fail(),
 * que indica que o teste é apenas um protótipo.
 * 
 * @author Ana Carolina Rodrigues Rocha
 */
class MovimentacaoTest {
    private final char debito = 'D';
    private final char credito = 'C';
    private final double limite = 150.00;

    @Test
    void testGetId() {
        final Movimentacao instance = new Movimentacao(new Conta());
        final long esperado = 1;
        instance.setId(esperado);
        final long obtido = instance.getId();
        assertEquals(esperado, obtido);
    }

    /**
     * Tipo da movimentação deve ser 'C' para crédito (entrada de dinheiro)
     * ou 'D' para débito (saída de dinheiro) (R01).
     */
    @Test
    void testTipoMovimentacaoCreditoValidoR01() {
        final Movimentacao movimentacao = new Movimentacao(new Conta());

        movimentacao.setTipo(credito);
        Assertions.assertEquals(movimentacao.getTipo(),credito);

    }

    @Test
    void testTipoMovimentacaoDebitoValidoR01() {
        final Movimentacao movimentacao = new Movimentacao(new Conta());

        movimentacao.setTipo(debito);
        Assertions.assertEquals(movimentacao.getTipo(),debito);

    }


    @Test
    void testTipoMovimentacaoInvalidoR01(){
        final Movimentacao movimentacao = new Movimentacao(new Conta());

        movimentacao.setTipo('1');
        Assertions.assertEquals(movimentacao.getTipo(),debito);
    }


    /**
     * Valor monetário da movimentação.
     * O valor não deve ser negativo, uma vez que existe o atributo {@link #tipo} (R02).
     */

    @Test
    void testValorValidoR02() throws Exception {
        final Movimentacao movimentacao = new Movimentacao(new Conta());
        movimentacao.setValor(100.00);

        if(movimentacao.getValor() <= 0 ){
            throw new Exception("O valor da movimentação deve ser maior que 0");
        }

    }
    @Test
    void testValorInvalidoR02() throws Exception {
        final Movimentacao movimentacao = new Movimentacao(new Conta());
        movimentacao.setValor(-100.00);

        if(movimentacao.getValor() <= 0 ){
            throw new Exception("O valor da movimentação deve ser maior que 0");
        }

    }


    /**
     * Valor monetário da movimentação.
     * Se o tipo for débito, o valor da movimentação não pode ser superior ao saldo total da {@link Conta} (R03).
     */

    private Conta newConta(double saldo, boolean especial){
        Conta conta = new Conta();
        conta.setSaldo(saldo);

        if (especial){
            conta.setEspecial(true);
            conta.setLimite(limite);
        }else {
            conta.setEspecial(false);
        }

        return conta;
    }

    private Movimentacao newMovimentacao(char tipo, double valor, Conta conta){

        Movimentacao movimentacao = new Movimentacao(conta);
        movimentacao.setTipo(tipo);
        movimentacao.setValor(valor);

        return movimentacao;
    }
    @Test
    void testValorDebito() throws Exception {
        final double saldo = 500.00;
        final double valor = 500.00;

        Conta conta = newConta(saldo,true);
        Movimentacao movimentacao = newMovimentacao(debito,valor, conta);

        conta.addMovimentacao(movimentacao);

        if(movimentacao.getTipo() == debito && movimentacao.getValor() > (valor+saldo+limite)){
            throw new Exception("O valor da movimentação deve ser menor que o Saldo Total da conta"+
                    "\n Saldo Total: "+ conta.getSaldoTotal()+
                    "\n Movimentação: "+ movimentacao.getValor());
        }

    }

    @Test
    void testValorDebitoEspecial() throws Exception {
        final double saldo = 500.00;
        final double valor = 500.00;

        Conta conta = newConta(saldo,true);
        Movimentacao movimentacao = newMovimentacao(debito,valor, conta);

        double saldoAntigo =  conta.getSaldo();
        conta.addMovimentacao(movimentacao);

        if(movimentacao.getTipo() == debito && movimentacao.getValor() > (saldo+limite)){
            throw new Exception("O valor da movimentação deve ser menor que o Saldo Total da conta"+
                    "\n Saldo Total: "+ saldoAntigo+
                    "\n Movimentação: "+ movimentacao.getValor());
        }

    }

    @Test
    void testValorCredito() throws Exception {
        Conta conta = newConta(100.00,false);
        Movimentacao movimentacao = newMovimentacao(credito,300.00, conta);

        double saldoAntigo =  conta.getSaldo();
        conta.addMovimentacao(movimentacao);

        if(movimentacao.getTipo() == credito && conta.getSaldoTotal() != (movimentacao.getValor() + saldoAntigo)){
            throw new Exception("O Saldo Total da conta deve ser a soma do Saldo com a Movimentação"+
                    "\n Movimentação: "+ movimentacao.getValor()+
                    "\n Saldo: "+ conta.getSaldo()+
                    "\n Saldo Total desejado: "+ (movimentacao.getValor() + saldoAntigo));
        }

    }
    @Test
    void testValorCreditoEspecial() throws Exception {
        final double saldo = 100.00;
        final double valor = 300.00;

        Conta conta = newConta(saldo,true);
        Movimentacao movimentacao = newMovimentacao(credito,valor, conta);

        conta.addMovimentacao(movimentacao);

        if(movimentacao.getTipo() == credito && conta.getSaldoTotal() != (saldo+valor+limite)){
            throw new Exception("O Saldo Total da conta deve ser a soma do Saldo com a Movimentação"+
                    "\n Movimentação: "+ movimentacao.getValor()+
                    "\n Saldo: "+ conta.getSaldo()+
                    "\n Saldo Total desejado: "+ (saldo+valor+limite));
        }

    }

}
