package br.com.scopus.simulador.business.i18n;

import lombok.Getter;

/**
 * Enumerador contendo as chaves para internacionalizacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public enum I18nKeys {

    ServicoExecutadoComSucesso("msg.servico.executado.com.sucesso"), 
    NenhumRegistroEncontrado("msg.nenhum.registro.encontrado"), 
    FalhaAoExecutarServico("msg.falha.executar.servico"), 
    AcessoNegado("msg.acesso.negado"),
    ParametrosInvalidos("msg.parametros.invalidos"),
    ParametrosInvalidosXxx("msg.parametros.invalidos.xxx"),
    UsuarioNaoEncontrado("msg.usuario.nao.encontrado"),
    SenhaInvalida("msg.usuario.senha.incorreto"),
    UsuarioInativo("msg.usuario.inativo"),
    EmailJaCadastrado("msg.email.ja.cadastrado"),
    ErroAoEnviarEmail("msg.erro.ao.enviar.email"),
    ProjetoJaCadastrado("msg.projeto.ja.cadastrado"),
    DataInicialMaiorDataFinal("msg.erro.datainicial.maior.datafinal"),
    VersaoInvalidaTransacao("msg.erro.versao.invalida"),
    CamposDeInputVazio("msg.erro.campos.entrada.vazio"),
    CamposDeOutputVazio("msg.erro.campos.saida.vazio"),
    PermissaoInvalida("msg.erro.permissao.invalida"),
    CamposDeInputInvalidos("msg.erro.campos.entrada.invalido");
    
    @Getter
    String key;
    
    private I18nKeys(String key) {
        this.key = key;
    }
    
}