package br.com.scopus.simulador.business.simulator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import br.com.scopus.simulador.dto.ExerciseMassDto;
import br.com.scopus.simulador.dto.ExerciseMassItemDto;
import br.com.scopus.simulador.repository.entity.enums.FieldType;
import br.com.scopus.simulador.repository.entity.enums.TransactionType;

/**
 * Classe base para implementação dos DAOS de exercitação de massa de testes.
 *
 * @author Carlos Alberto Beleli Junior
 * @version 1.0 - 27/10/2006
 */
public abstract class AbstratacSimulator {

    /** Constante para o número 4 */
    public static final long NUMERO_4 = 4L;

    /** Constante para o número 10 */
    public static final long NUMERO_10 = 10L;

    /** Constante para o número 16 */
    public static final long NUMERO_16 = 16L;

    /** Constante para o número 255 */
    public static final long NUMERO_255 = 255L;

    /** Constante para o número 256 */
    public static final int NUMERO_256 = 256;

    /** Constante para identificação do sinal de mais para os campos compactados */
    public static final byte PBCD_MAIS = 12;

    /** Constante para identificação do sinal de menos para os campos compactados */
    private static final byte PBCD_MENOS = 13;

    /**
     * Executa uma transação mainframe.
     * 
     * @param tipoTransacao Tipo de transação pesquisada (1 = CICS, 2 = IMS).
     * @param nomeTransacao Nome da transação a ser executada.
     * @param massa Objeto do tipo <tt>Massa</tt> contendo os dados de input e output da transação.
     * @throws Exception 
     * @throws DAOException Contendo o erro de acesso ao DAO.
     */
    public void executeTransaction(ExerciseMassDto dto, String transactionName, TransactionType transactionType) throws Exception {
        if (transactionType.equals(TransactionType.CICS)) {
            executeTransactionCICS(dto, transactionName, transactionType);
        } else if (transactionType.equals(TransactionType.IMS)) {
            executeTransactionIMS(dto, transactionName, transactionType);
        }
    }

    /**
     * Método abstrato para execução de transações CICS.
     * 
     * @param nomeTransacao Nome completo, parcial ou em branco, usado para pesquisar as transações.
     * @param massa Objeto do tipo <tt>Massa</tt> contendo os dados de input e output da transação.
     * @throws Exception Contendo o erro.
     */
    protected abstract void executeTransactionCICS(ExerciseMassDto dto, String transactionName, TransactionType transactionType) throws Exception;

    /**
     * Método abstrato para execução de transações IMS.
     * 
     * @param nomeTransacao Nome completo, parcial ou em branco, usado para pesquisar as transações.
     * @param massa Objeto do tipo <tt>Massa</tt> contendo os dados de input e output da transação.
     * @throws Exception 
     * @throws Exception Contendo o erro.
     */
    protected abstract void executeTransactionIMS(ExerciseMassDto dto, String transactionName, TransactionType transactionType) throws Exception;

    /**
     * Obtém do buffer de envio da transação mainframe.
     * 
     * @param dto Objeto do tipo <tt>Massa</tt> contendo os dados de input e output da transação.
     * @param removeFirst Flag indicativa para o não processamento do primeiro campo da
     *            massa.
     * @return Array de bytes para envio ao mainframe.
     * @throws UnsupportedEncodingException Contendo a exceção de conversão da dados.
     */
    protected byte[] getInputData(ExerciseMassDto dto, boolean removeFirst)
        throws UnsupportedEncodingException {
        int tamanhoBuffer = 0;
        
        List<ExerciseMassItemDto> fields = new ArrayList<>();
        if (dto.getRouterInputList() != null) fields.addAll(dto.getRouterInputList());
        fields.addAll(dto.getInputList());
        List<byte[]> arrayList = new ArrayList<byte[]>();

        if (removeFirst) {
            fields.remove(0);
        }
        for (ExerciseMassItemDto field : fields) {
            byte[] bufferCampo = null;
            if (field.getType().equals(FieldType.ALPHANUMERIC)) {
                bufferCampo = formatBuffer(field.getValue(), field.getSize(), ' ', false);
            } else if (field.getType().equals(FieldType.BYTE)) {
                bufferCampo = formatByteBuffer(field.getValue(), field.getSize());
            } else if (field.getType().equals(FieldType.DECIMAL_COMPRESSED)) {
                bufferCampo = formatCompressedBuffer(field.getValue(), field.getSize());
            } else {
                bufferCampo = formatBuffer(field.getValue(), field.getSize(), '0', true);
            }
            arrayList.add(bufferCampo);
            tamanhoBuffer += bufferCampo.length;
        }

        byte[] bufferEnvio = new byte[tamanhoBuffer];
        int indiceAtual = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            byte[] bufferCampo = (byte[]) arrayList.get(i);
            for (int j = 0; j < bufferCampo.length; j++) {
                bufferEnvio[indiceAtual++] = bufferCampo[j];
            }
        }
        return bufferEnvio;
    }

    /**
     * Processa o buffer de retorno da transação mainframe, configurando o objeto <tt>Massa</tt>
     * recebido.
     * 
     * @param dadosRetorno Array de bytes retornados pela transação mainframe.
     * @param massa Objeto do tipo <tt>Massa</tt> contendo os dados de input e output da transação.
     * @param desprezarPrimeiroCampo Flag indicativa para o não processamento do primeiro campo da massa.
     * @throws UnsupportedEncodingException Contendo a exceção de conversão da dados.
     */
    protected void processOutputData(byte[] dadosRetorno, ExerciseMassDto dto, TransactionType transactionType,
        boolean removeFirst) throws UnsupportedEncodingException {
        int indiceBuffer = 0;
        
        List<ExerciseMassItemDto> fields = new ArrayList<>();
        if (dto.getRouterOutputList() != null) fields.addAll(dto.getRouterOutputList());
        fields.addAll(dto.getOutputList());

        if (removeFirst) {
            if (dto.getOutputList().size() > 0 && transactionType.equals(TransactionType.IMS)) {
                fields.remove(dto.getRouterOutputList().size()).setValue("");
            }
            fields.remove(0).setValue("");
        }
        for (ExerciseMassItemDto field : fields) {
            if (field.getType().equals(FieldType.DECIMAL_COMPRESSED)) {
                field.setValue(getCompressedBufferValue(dadosRetorno, indiceBuffer, indiceBuffer + field.getSize()));

            } else if (field.getType().equals(FieldType.BYTE)) {
                field.setValue(getByteBufferValue(dadosRetorno, indiceBuffer, indiceBuffer + field.getSize()));

            } else {
                field.setValue(getBufferValue(dadosRetorno, indiceBuffer, field.getSize()));
            }
            indiceBuffer += field.getSize();
        }
    }

    /**
     * Formata um buffer com preenchimento à esquerda ou à direita.
     *
     * @param value Objeto do tipo <tt>String</tt> a ser formatado.
     * @param size Valor <tt>int</tt> contendo o tamanho fixo para formatação.
     * @param fill Valor <tt>char</tt> contendo o caractere de preenchimento para a
     *            formatação da string.
     * @param toTheLeft Valor <tt>boolean</tt> indicando se o preenchimento ou truncamento deve
     *            ser feito à esquerda da string.
     * @return Objeto <tt>byte[]</tt> formatado. Se o tamanho parâmetro <tt>valor</tt> exceder o
     *         tamanho de formatação, então este método o seu conteúdo será truncado à esquerda ou à
     *         direita, conforme o conteúdo do parâmetro <tt>pelaEsquerda</tt>.
     * @throws UnsupportedEncodingException Contendo a exceção de conversão da dados.
     */
    protected byte[] formatBuffer(String value, int size, char fill, boolean toTheLeft)
        throws UnsupportedEncodingException {
        String result = "";
        StringBuffer formatedValue = new StringBuffer();

        if ((value != null) && (value.length() > 0)) {
            result = value;
        }

        if ((result.length() > size) && (size > 0)) {
            if (toTheLeft) {
                // Retira os caracteres excedentes pela esquerda
                formatedValue.append(
                    result.substring(result.length() - size, result.length()));
            } else {
                // Retira os caractere excedentes pela direita
                formatedValue.append(result.substring(0, size));
            }
        } else {
            if (toTheLeft) {
                // Preenche o valor formatado com os caracteres filler à esquerda
                for (int i = 0; i < (size - result.length()); i++) {
                    formatedValue.append(fill);
                }

                // Coloca o valor para formatar à direita
                formatedValue.append(result);
            } else {
                // Coloca o valor para formatar à esquerda
                formatedValue.append(result);

                // Preenche o valor formatado com os caracteres filler à direita
                for (int i = 0; i < (size - result.length()); i++) {
                    formatedValue.append(fill);
                }
            }
        }
        return formatedValue.toString().getBytes("Cp037");
    }

    /**
     * Obtém o valor string de uma porção de um buffer retornado do mainframe.
     *
     * @param output Objeto do tipo <tt>byte[]</tt> contendo o buffer retorno do mainframe.
     * @param initialIndex Índice inicial do segmento do buffer.
     * @param segmentSize Tamanho do segmento do buffer.
     * @return Objeto <tt>String</tt> formatado.
     * @throws UnsupportedEncodingException Contendo a exceção de conversão da dados.
     */
    protected String getBufferValue(byte[] output, int initialIndex, int segmentSize)
        throws UnsupportedEncodingException {
        String retorno = "";

        if (output != null) {
            int indInicial = initialIndex;
            if (indInicial < 0) {
                indInicial = 0;
            } else if (indInicial > output.length) {
                indInicial = output.length;
            }
            int tamSegmento = segmentSize;
            if (tamSegmento < 0) {
                tamSegmento = 0;
            } else if (indInicial + tamSegmento > output.length) {
                tamSegmento = output.length - indInicial;
            }

            retorno = new String(output, indInicial, tamSegmento, "Cp037");
        }

        return retorno;
    }

    /**
     * Formata um buffer com dados numéricos compactados.
     *
     * @param value Objeto do tipo <tt>String</tt> a ser formatado.
     * @param size Valor <tt>int</tt> contendo o tamanho fixo para formatação.
     * @return Objeto <tt>byte[]</tt> formatado. Se o tamanho parâmetro <tt>valor</tt> exceder o
     *         tamanho de formatação, então este método o seu conteúdo será truncado à esquerda ou à
     *         direita, conforme o conteúdo do parâmetro <tt>pelaEsquerda</tt>.
     */
    protected byte[] formatCompressedBuffer(String value, int size) {
        long newValue = 0L;

        if (value != null && value.length() > 0) {
            newValue = Long.parseLong(value);
        }
        byte[] bufferCampo = new byte[size];
        if (newValue >= 0L) {
            bufferCampo[size - 1] = PBCD_MAIS;
        } else {
            newValue = Math.abs(newValue);
            bufferCampo[size - 1] = PBCD_MENOS;
        }
        boolean prim = false;
        int index = bufferCampo.length - 1;
        for (int i = 0; i < bufferCampo.length * 2 - 1; i++) {
            if (prim) {
                bufferCampo[index] = (byte) (newValue % NUMERO_10);
                newValue /= NUMERO_10;
                prim = false;
            } else {
                bufferCampo[index] = (byte) ((bufferCampo[index] + ((newValue % NUMERO_10) << NUMERO_4)) & NUMERO_255);
                newValue /= NUMERO_10;
                prim = true;
                index--;
            }
        }
        return bufferCampo;
    }

    /**
     * Obtém o valor string de uma porção de um buffer compactado retornado do mainframe.
     *
     * @param output Objeto do tipo <tt>byte[]</tt> contendo o buffer retorno do mainframe.
     * @param initialIndex Índice inicial do segmento do buffer.
     * @param finalIndex Índice final do segmento do buffer.
     * @return Objeto <tt>String</tt> formatado.
     * @throws UnsupportedEncodingException Contendo a exceção de conversão da dados.
     */
    protected String getCompressedBufferValue(byte[] output, int initialIndex, int finalIndex)
        throws UnsupportedEncodingException {
        String retorno = "";

        if (output != null) {
            int indInicial = initialIndex;
            if (indInicial < 0) {
                indInicial = 0;
            } else if (indInicial > output.length) {
                indInicial = output.length;
            }
            int indFinal = finalIndex;
            if (indFinal < indInicial) {
                indFinal = indInicial;
            } else if (indFinal > output.length) {
                indFinal = output.length;
            }

            long result = 0L;
            int temp;
            for (int i = indInicial; i < indFinal - 1; i++) {
                if (output[i] < 0) {
                    temp = output[i] + NUMERO_256;
                } else {
                    temp = output[i];
                }
                result *= NUMERO_10;
                result += (long) (temp % NUMERO_16) + (long) (temp / NUMERO_16) * NUMERO_10;
                result *= NUMERO_10;
            }

            if (indInicial < indFinal) {
                if (output[indFinal - 1] < 0) {
                    temp = output[indFinal - 1] + NUMERO_256;
                } else {
                    temp = output[indFinal - 1];
                }
                result += temp / NUMERO_16;
                if (temp % NUMERO_16 == PBCD_MENOS) {
                    result = -result;
                }
            }
            retorno = String.valueOf(result);
        }

        return retorno;
    }

    /**
     * Formata um buffer com dados numéricos do tipo "byte".
     *
     * @param value Objeto do tipo <tt>String</tt> a ser formatado.
     * @param size Valor <tt>int</tt> contendo o tamanho fixo para formatação.
     * @return Objeto <tt>byte[]</tt> formatado. Se o tamanho parâmetro <tt>valor</tt> exceder o
     *         tamanho de formatação, então este método o seu conteúdo será truncado à esquerda ou à
     *         direita, conforme o conteúdo do parâmetro <tt>pelaEsquerda</tt>.
     */
    protected byte[] formatByteBuffer(String value, int size) {
        long novoValor = 0L;

        if (value != null && value.length() > 0) {
            novoValor = Math.abs(Long.parseLong(value, (int) NUMERO_16));
        }
        byte[] bufferCampo = new byte[size];
        for (int i = bufferCampo.length - 1; i >= 0; i--) {
            bufferCampo[i] = (byte) (novoValor % NUMERO_256);
            novoValor /= NUMERO_256;
        }
        return bufferCampo;
    }

    /**
     * Obtém o valor string de uma porção de um buffer com o tipo "byte" retornado do mainframe.
     *
     * @param output Objeto do tipo <tt>byte[]</tt> contendo o buffer retorno do mainframe.
     * @param initialIndex Índice inicial do segmento do buffer.
     * @param finalIndex Índice final do segmento do buffer.
     * @return Objeto <tt>String</tt> formatado.
     * @throws UnsupportedEncodingException Contendo a exceção de conversão da dados.
     */
    protected String getByteBufferValue(byte[] output, int initialIndex, int finalIndex)
        throws UnsupportedEncodingException {
        String retorno = "";

        if (output != null) {
            int indInicial = initialIndex;
            if (indInicial < 0) {
                indInicial = 0;
            } else if (indInicial > output.length) {
                indInicial = output.length;
            }
            int indFinal = finalIndex;
            if (indFinal < indInicial) {
                indFinal = indInicial;
            } else if (indFinal > output.length) {
                indFinal = output.length;
            }

            long result = 0L;
            int temp;
            for (int i = indInicial; i < indFinal; i++) {
                if (output[i] < 0) {
                    temp = output[i] + NUMERO_256;
                } else {
                    temp = output[i];
                }
                result *= NUMERO_256;
                result += (long) temp;
            }

            retorno = Long.toString(result, (int) NUMERO_16);
        }

        return retorno;
    }

    /**
     * Método auxiliar para juntar 2 arrays do <tt>byte</tt> em um único array.
     *
     * @param array1 Objeto do tipo <tt>byte[]</tt> a ser concatenado.
     * @param array2 Objeto do tipo <tt>byte[]</tt> a ser concatenado.
     * @return Objeto <tt>byte[]</tt> com a concatenação dos valores dos objetos array1 e array2.
     */
    protected byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] retorno;

        if (array1 == null) {
            retorno = array2;
        } else if (array2 == null) {
            retorno = array1;
        } else {
            retorno = new byte[array1.length + array2.length];
            System.arraycopy(array1, 0, retorno, 0, array1.length);
            System.arraycopy(array2, 0, retorno, array1.length, array2.length);
        }

        return retorno;
    }

    /**
     * Método auxiliar para obter a cópia do trecho de um determinado array.
     *
     * @param array Objeto do tipo <tt>byte[]</tt> a ser processado.
     * @param initialIndex Índice inicial do array para a cópia.
     * @param finalIndex Índice final do array para a cópia.
     * @return Objeto <tt>byte[]</tt> contendo a cópia do trecho solicitado.
     */
    protected byte[] subArray(byte[] array, int initialIndex, int finalIndex) {
        byte[] retorno;

        if (array == null || initialIndex > finalIndex) {
            retorno = (byte[]) null;
        } else if (initialIndex == finalIndex) {
            retorno = new byte[0];
        } else {
            retorno = new byte[finalIndex - initialIndex];
            System.arraycopy(array, initialIndex, retorno, 0, finalIndex - initialIndex);
        }

        return retorno;
    }
}
