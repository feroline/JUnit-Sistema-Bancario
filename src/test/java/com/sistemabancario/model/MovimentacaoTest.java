package com.sistemabancario.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

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
     * Se o tipo for débito, o valor da movimentação não pode ser superior ao saldo total da {@link Conta} (R03).
     */

    @Test
    void valorMovimentacaoDebitoContaEspecialR03() throws Exception {
        final Conta conta = new Conta();
        conta.setEspecial(true);
        conta.setSaldo(100.00);
        conta.setLimite(150.00);

        final Movimentacao movimentacao = new Movimentacao(conta);
        movimentacao.setTipo(debito);
        movimentacao.setValor(300.50);

        if(movimentacao.getTipo() == debito &&
                movimentacao.getValor() > conta.getSaldoTotal()){
            throw new Exception("O valor da movimentação deve ser inferior ao saldo total na conta."+
                    "\n Saldo Total: "+ conta.getSaldoTotal()+
                    "\n Movimentação: "+ movimentacao.getValor()
            );
        }
    }
}
