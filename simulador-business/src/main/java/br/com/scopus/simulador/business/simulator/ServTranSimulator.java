package br.com.scopus.simulador.business.simulator;

import org.springframework.stereotype.Component;

import br.com.scopus.simulador.dto.ExerciseMassDto;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

@Component("servTranSimulator")
public class ServTranSimulator extends AbstratacSimulator {

    /** Máscara do numerador */
    private static final int INTEIRO_MASCARA_NUMERADOR = 0xfff;

    /** Numerador para execução das transações ServTran */
    private static int numerador = 0;

    /** Nome do usuário */
    private String nomeUsuario;

    private ServTranSimulator() {
        this.nomeUsuario = "CETE";
    }

    /**
     * 
     *
     * @return O próximo numerador para execução das transações ServTran
     */
    private synchronized int getNextNumerador() {
        return ++numerador & INTEIRO_MASCARA_NUMERADOR;
    }

    @Override
    protected void executeTransactionCICS(ExerciseMassDto dto, String transactionName, TransactionType transactionType)
        throws Exception {
        /*CommServTrans objServTrans = null;
        try {
            // Conexão ao ServTrans
            objServTrans = new CommServTrans();
            objServTrans.conecta(nomeUsuario);

            // Envio da requisição
            int numeradorLocal = getNextNumerador();
            byte[] blocoTransmissao = getInputData(dto, false);
            objServTrans.envia(blocoTransmissao, dto.getCics(), numeradorLocal);

            // Processamento do retorno
            byte[] retornoTransacao = objServTrans.recebe(numeradorLocal, 0);
            processOutputData(retornoTransacao, dto, transactionType, false);
        } catch (Exception exception) {
            // throw new DAOException(exception);
        } finally {
            if (objServTrans != null) {
                try {
                    objServTrans.desconecta();
                } catch (Exception exception) {
                    objServTrans = null;
                }
            }
        }*/
    }

    @Override
    protected void executeTransactionIMS(ExerciseMassDto dto, String transactionName, TransactionType transactionType)
        throws Exception {
        //Não implementado.
    }
}
