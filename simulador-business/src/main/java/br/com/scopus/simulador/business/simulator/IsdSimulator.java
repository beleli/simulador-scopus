package br.com.scopus.simulador.business.simulator;

import org.springframework.stereotype.Component;

import br.com.scopus.simulador.dto.ExerciseMassDto;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;
import isdd.instantaneo.IISDINS;
import isdd.instantaneo.IISDINSConexao;
import isdd.instantaneo.ISDINSFactory;
import isdd.planodeacesso.IISDPA;
import isdd.planodeacesso.IISDPAConexao;
import isdd.planodeacesso.IISDPAConexaoSistemaSeguranca;
import isdd.planodeacesso.ISDPAFactory;
import isdd.planodeacesso.ISDPAStatusRecepcao;

@Component("isdSimulator")
public class IsdSimulator extends AbstratacSimulator {

    /** Índice inicial do campo header. */
    private static final int INDICE_INICIAL_HEADER = 4;

    /** Tamanho do campo header. */
    private static final int TAMANHO_HEADER = 3;

    /** Índice inicial do campo periférico. */
    private static final int INDICE_INICIAL_PERIFERICO = 15;

    /** Tamanho do campo periférico. */
    private static final int TAMANHO_PERIFERICO = 1;

    /** Buffer de header CICS contendo apenas brancos. */
    private static byte[] bufferHeaderCICSBrancos;

    /** Nome da aplicação */
    private String nomeAplicacao;

    /** Construtor padrão desta classe. */
    private IsdSimulator() {
        try {
            nomeAplicacao = "CETE";
            bufferHeaderCICSBrancos = "                 ".getBytes("Cp037");
        } catch (Exception exception) {

        }
    }

    @Override
    protected void executeTransactionCICS(ExerciseMassDto dto, String transactionName,
        TransactionType transactionType) throws Exception {
        IISDINS objIsdins = null;
        IISDINSConexao objInsconn = null;
        try {
            if (dto.getService() > 0) {
                objIsdins = ISDINSFactory.criarObjeto(nomeAplicacao, dto.getService());
            } else {
                objIsdins = ISDINSFactory.criarObjeto(nomeAplicacao);
            }
            byte[] dadosEnvio = getInputData(dto, false);
            objInsconn = objIsdins.conectarTransacao(transactionName);
            objInsconn.setIdHeader(getBufferValue(dadosEnvio, INDICE_INICIAL_HEADER, TAMANHO_HEADER));
            objInsconn
                .setIdPeriferico(getBufferValue(dadosEnvio, INDICE_INICIAL_PERIFERICO, TAMANHO_PERIFERICO).charAt(0));

            objInsconn.transmitir(subArray(dadosEnvio, bufferHeaderCICSBrancos.length, dadosEnvio.length));
            processOutputData(concatArrays(bufferHeaderCICSBrancos, objInsconn.receber()), dto, transactionType, false);
        } catch (Exception exception) {
            throw exception;
        } finally {
            if (objInsconn != null) {
                try {
                    objInsconn.desconectarTransacao();
                } catch (Exception exception) {
                    objInsconn = null;
                }
            }
        }
    }

    @Override
    protected void executeTransactionIMS(ExerciseMassDto dto, String transactionName, TransactionType transactionType)
        throws Exception {
        IISDPA objIsdpa = null;
        IISDPAConexao objpaconn = null;
        IISDPAConexaoSistemaSeguranca objIsdpaconnseg = null;
        ISDPAStatusRecepcao objrecebestatus = null;

        try {
            if (dto.getService() > 0) {
                objIsdpa = ISDPAFactory.criarObjeto(nomeAplicacao, dto.getService());
            } else {
                objIsdpa = ISDPAFactory.criarObjeto(nomeAplicacao);
            }
            if (dto.isSecurityHeader()) {
                objIsdpaconnseg = objIsdpa.conectarSistemaSeguranca(null, 1, dto.getUser(), dto.getPassword(), null,
                    null, null, '1');

                objpaconn = objIsdpa.conectarTransacao(transactionName, objIsdpaconnseg);
            } else {
                objpaconn = objIsdpa.conectarTransacao(transactionName);
            }
            objpaconn.transmitir(getInputData(dto, true));
            objrecebestatus = objpaconn.receber();
            objrecebestatus.getCodRetornoAplicacao();
            processOutputData(objrecebestatus.getBufferRx(), dto, transactionType, true);
        } catch (Exception exception) {
            throw exception;
        } finally {
            if (objIsdpaconnseg != null) {
                try {
                    objIsdpaconnseg.desconectarSistemaSeguranca();
                } catch (Exception exception) {
                    objIsdpaconnseg = (IISDPAConexaoSistemaSeguranca) null;
                }
            }
            if (objpaconn != null) {
                try {
                    objpaconn.desconectarTransacao();
                } catch (Exception exception) {
                    objpaconn = (IISDPAConexao) null;
                }
            }
        }
    }
}
