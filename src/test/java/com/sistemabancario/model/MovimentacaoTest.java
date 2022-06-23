package com.sistemabancario.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.lang.model.type.NullType;

import java.beans.ExceptionListener;
import java.util.List;

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
    protected final char debito = 'D';
    protected final char credito = 'C';
    protected final double limite = 150.00;
    protected final String valorIllegal = "O valor da movimentação deve ser maior que 0";
    protected final String tipoIllegal = "Tipo inserido incorreto";

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
        Assertions.assertEquals(credito,movimentacao.getTipo());

    }

    @Test
    void testTipoMovimentacaoDebitoValidoR01() {
        final Movimentacao movimentacao = new Movimentacao(new Conta());

        movimentacao.setTipo(debito);
        Assertions.assertEquals(debito, movimentacao.getTipo());

    }


    @Test
    void testTipoMovimentacaoInvalidoR01(){
        char tipoIncorreto = '1';
        final Movimentacao movimentacao = new Movimentacao(new Conta());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimentacao.setTipo(tipoIncorreto);
        });

        assertEquals(tipoIllegal, exception.getMessage());

    }


    /**
     * Valor monetário da movimentação.
     * O valor não deve ser negativo, uma vez que existe o atributo {@link #tipo} (R02).
     */

    @Test
    void testValorValidoR02() {
        double valor = 100.00;

        final Movimentacao movimentacao = new Movimentacao(new Conta());
        movimentacao.setValor(valor);

        assertEquals(valor , movimentacao.getValor());
    }
    @Test
    void testValorInvalidoR02() {
        double tipoIncorreto = -100.00;
        final Movimentacao movimentacao = new Movimentacao(new Conta());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            movimentacao.setValor(tipoIncorreto);
        });

        assertEquals(valorIllegal, exception.getMessage());

    }


    /**
     * Valor monetário da movimentação.
     * Se o tipo for débito, o valor da movimentação não pode ser superior ao saldo total da {@link Conta} (R03).
     */

    private Conta newConta(double saldo, boolean especial){
        Conta conta = new Conta();
        conta.setSaldo(saldo);
        conta.setNumero("78945-8");
        conta.setId(0);

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
        movimentacao.setDescricao("Teste");
        movimentacao.setConfirmada(true);
        movimentacao.setId(0);
        conta.addMovimentacao(movimentacao);

        return movimentacao;
    }
    @Test
    void testValorDebitoR03() throws Exception {
        final double saldo = 500.00;
        final double valor = 500.00;

        Conta conta = newConta(saldo,true);
        Movimentacao movimentacao = newMovimentacao(debito,valor, conta);

        assertEquals(debito, movimentacao.getTipo());
//        assertAll( "valorTotal",

//                () -> {
//                    double valorPego = movimentacao.getValor();
//                    assertNotNull(valorPego);
//
//                    assertAll("valor"
//                            {
//                                    () -> assertTrue(valorPego.great);
//                            }
//                    );
//                }
//        );

//       ( (valor+saldo+limite), movimentacao.getValor());
        if(movimentacao.getTipo() == debito && movimentacao.getTipo() > (valor+saldo+limite)){
            throw new Exception("O valor da movimentação deve ser menor que o Saldo Total da conta"+
                    "\n Saldo Total: "+ conta.getSaldoTotal()+
                    "\n Movimentação: "+ movimentacao.getValor());
        }

    }

    @Test
    void testValorDebitoEspecialR03() throws Exception {
        final double saldo = 500.00;
        final double valor = 500.00;

        Conta conta = newConta(saldo,true);
        Movimentacao movimentacao = newMovimentacao(debito,valor, conta);

        double saldoAntigo =  conta.getSaldo();

//        assertEquals(valor,movimentacao.getValor());
//        assertEquals(saldo, conta.getSaldo());
//
//        assertEquals((movimentacao.getValor() + saldoAntigo), conta.getSaldoTotal());

        if(movimentacao.getTipo() == debito && movimentacao.getValor() > (saldo+limite)){
            throw new Exception("O valor da movimentação deve ser menor que o Saldo Total da conta"+
                    "\n Saldo Total: "+ saldoAntigo+
                    "\n Movimentação: "+ movimentacao.getValor());
        }

    }

    @Test
    void testValorCreditoR03() {
        final double saldo = 100.00;
        final double valor = 300.00;
        Conta conta = newConta(saldo,false);
        Movimentacao movimentacao = newMovimentacao(credito,valor, conta);

        double saldoAntigo =  conta.getSaldo();

        assertEquals(valor,movimentacao.getValor());
        assertEquals(saldo, conta.getSaldo());
        assertEquals((movimentacao.getValor() + saldoAntigo), conta.getSaldoTotal());


    }
    @Test
    void testValorCreditoEspecialR03() throws Exception {
        final double saldo = 100.00;
        final double valor = 300.00;

        Conta conta = newConta(saldo,true);
        Movimentacao movimentacao = newMovimentacao(credito,valor, conta);

//        assertEquals(valor,movimentacao.getValor());
//        assertEquals(saldo, conta.getSaldo());
//        assertEquals((saldo+valor+limite), conta.getSaldoTotal());
        if(movimentacao.getTipo() == credito && conta.getSaldoTotal() != (saldo+valor+limite)){
            throw new Exception("O Saldo Total da conta deve ser a soma do Saldo com a Movimentação"+
                    "\n Movimentação: "+ movimentacao.getValor()+
                    "\n Saldo: "+ conta.getSaldo()+
                    "\n Saldo Total desejado: "+ (saldo+valor+limite));
        }

    }

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
    @Test
    void testVerificarConfirmacaoMovimentacaoR04 (){
        final double saldo = 100.00;
        final double valor = 20.00;

        Conta conta = newConta(saldo,false);
        Movimentacao movimentacao = newMovimentacao(credito,valor, conta);

        assertEquals(conta.getId(),conta.getMovimentacoes().get(0).getConta().getId());
        assertEquals(true,conta.getMovimentacoes().get(conta.getMovimentacoes().indexOf(movimentacao)).isConfirmada());

    }

    /**
     * Instancia uma movimentação para uma determinada {@link Conta} bancária,
     * onde a conta passada deve ser armazenada no atributo {@link #conta} (R05).
     * @param conta a {@link Conta} para vincular a movimentação.
     */
    @Test
    void testVinculaMovimentacaoDaContaR05() {
        final double saldo = 0;
        final double valor = 20.00;

        Conta conta = newConta(saldo,false);
        Movimentacao movimentacao = newMovimentacao(credito,valor, conta);
        String descricao = "Teste para ver se a movimentação é a mesma armazenada na conta";
        movimentacao.setDescricao(descricao);

//        assertEquals(conta.getId(),conta.getMovimentacoes().get(0).getConta().getId());
        assertEquals(movimentacao.getId(), conta.getMovimentacoes().get(conta.getMovimentacoes().indexOf(movimentacao)).getId()); // verifica se o id é o mesmo
        assertEquals(movimentacao.getValor(), conta.getMovimentacoes().get(conta.getMovimentacoes().indexOf(movimentacao)).getValor()); //verifica se o valor é o mesmo
        assertEquals(movimentacao.getTipo(), conta.getMovimentacoes().get(conta.getMovimentacoes().indexOf(movimentacao)).getTipo()); //verifica se o valor é o mesmo
        assertEquals(movimentacao.getDescricao(), conta.getMovimentacoes().get(conta.getMovimentacoes().indexOf(movimentacao)).getDescricao()); //verifica se a descrição é o mesma

    }
}
