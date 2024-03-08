package br.com.scopus.simulador.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumerador contendo os codigos de retorno e suas respectivas mensagens que serao utilizads nos
 * retornos dos servicos da aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@AllArgsConstructor
public enum ReturnCode {
    
    SUCCESS(0, "msg.servico.executado.com.sucesso"), 
    FAIL(-1, "msg.falha.executar.servico"), 
    ACCESS_DENIED(-2, "msg.acesso.negado"),
    INVALID_PARAMETERS(-3, "msg.parametros.invalidos"),
    INVALID_PARAMETERS_XXX(-3, "msg.parametros.invalidos.xxx"),
    NOT_FOUND(-4, "msg.nenhum.registro.encontrado"),

    //Erros de usuario
    INVALID_USER(-101, "msg.usuario.nao.encontrado"),
    INVALID_PASSWORD(-102, "msg.usuario.senha.invalido"),
    USER_DISABLED(-103, "msg.usuario.inativo"),
    EMAIL_REGISTERED(-107, "msg.email.ja.cadastrado"),
    EMAIL_SEND_ERROR(-108, "msg.erro.ao.enviar.email"),
    
    //Erros de projetos
    INVALID_DATE_INITIAL_FINAL(-201, "msg.erro.datainicial.maior.datafinal"),
    PROJECT_REGISTERED(-202, "msg.projeto.ja.cadastrado"),
    
    //Erros de transações
    INVALID_INPUT_FIELDS(-301, "msg.erro.campos.entrada.vazio"),
    INVALID_OUTPUT_FIELDS(-302, "msg.erro.campos.saida.vazio"),
    INVALID_TRANSACTION_VERSION(-303, "msg.erro.versao.invalida"),
    
    //Erros de massas de teste
    INVALID_PERMISSION(-401,"msg.erro.permissao.invalida"),
    INVALID_INPUT_LIST(-402,"msg.erro.campos.entrada.invalido");
    
    @Getter
    private Integer code;
    @Getter
    private String message;
    
}
