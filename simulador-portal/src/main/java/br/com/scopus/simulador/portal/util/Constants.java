package br.com.scopus.simulador.portal.util;

/**
 * Contem as constantes utilizadas na aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public final class Constants {

    /**
     * Pagina de login da aplicacao.
     */
    public static final String PAGINA_LOGIN = "/";
    /**
     * Pagina inicial para usuarios logados da aplicacao.
     */
    public static final String PAGINA_HOME = "/pages/restricted/home/home.html";
    /**
     * Pagina de acesso negado da aplicacao.
     */
    public static final String PAGINA_ACESSO_NEGADO = "/pages/open/login/login.html";

    /**
     * Login REST services URL's
     */
    public static final String CONTROLLER_LOGIN = "/login";
    public static final String REST_USER = "/user";
    public static final String REST_LOGOUT = "/logout";
    public static final String CONTROLLER_USUARIO = "/usuario";
    public static final String CONTROLLER_PROJECT = "/projeto";
    public static final String CONTROLLER_CONFIGURATION = "/configuracao";
    public static final String CONTROLLER_TRANSACTION = "/transacao";
    public static final String CONTROLLER_TEST_SCENARIO = "/testScenario";
    public static final String CONTROLLER_TEST_MASS = "/testMass";
    public static final String CONTROLLER_EXERCISE_MASS = "/exerciseMass";
    public static final String REST_DELETE = "/delete/{id}";
    public static final String REST_CONSULTA = "/search";

    /**
     * Usuário REST service URL's
     */
    public static final String REST_RECUPERAR_SENHA = "/recuperarSenha/{email}/";
    public static final String REST_CADASTRO_USUARIO = "/cadastroUsuario";
    public static final String REST_CONSULTA_USUARIO_ID = "/consultaUsuario/{id}";
    public static final String REST_CONSULTA_USUARIO_EMAIL = "/consultaUsuario/{email}/";
    public static final String REST_ALTERAR_SENHA = "/alterarSenha";
    public static final String REST_CONSULTA_COMBO_USUARIO = "/buscaComboUsuarios";
    public static final String REST_CONSULTA_PERFIS = "/getPerfis";
    public static final String REST_CONSULTA_USUARIO = "/consultaUsuario";

    /**
     * Projeto REST service URL's
     */
    public static final String REST_CADASTRO_PROJETO = "/cadastroProjeto";
    public static final String REST_CONSULTA_PROJETO_ID = "/consultaProjeto/{id}";
    public static final String REST_EXCLUIR_PROJETO_ID = "/excluirProjeto/{id}";

    /**
     * Transação REST service URL's
     */
    public static final String REST_CADASTRO_TRANSACAO = "/cadastroTransacao";
    public static final String REST_EXCLUIR_TRANSCAO = "/excluirTransacao";
    public static final String REST_CONSULTA_TRANSACAO_ID = "/consultaTransacao/{id}";
    public static final String REST_FIND_TRANSACTION_TYPES = "/findTransactionTypes";
    public static final String REST_FIND_TRANSACTION_FORMATS = "/findTransactionFormats";
    public static final String REST_FIND_TRANSACTION_BY_NAME = "/findTransactionByName";
    public static final String REST_FIND_FIELDS_TYPE = "/findFieldsType";
    public static final String REST_FIND_PARENTS = "/findParents";
    public static final String REST_FIND_PARENT_FIELDS = "/findParentFields";
    public static final String REST_CONSULTA_TRANSACAO_BY_TRANSACTION = "/consultaTransacao";
    public static final String REST_FIND_SCENARIOS_TRANSACTIONS = "/findTransactionsScenarios/{identification}";
    
    
    /**
     * Cenário de teste REST service URL's
     */
    public static final String REST_CADASTRO_CENARIO_TESTE = "/cadastroCenario";
    public static final String REST_CONSULTA_CENARIO_TESTE_ID = "/consultaCenario/{id}";
    
    
    /**
     * Massa de teste REST service URL's
     */
    public static final String REST_CADASTRO_MASSA_TESTE = "/cadastroMassa";
    public static final String REST_CONSULTA_MASSA_TESTE_ID = "/consultaMassa/{id}";
    public static final String REST_CONSULTA_MASSA_TESTE = "/consultaMassa/";
    public static final String REST_EXCLUIR_MASSA_TESTE = "/excluirMassa";
    public static final String REST_CONSULTA_COMBO_CENARIO = "/consultaComboCenarioTeste/{transactionId}";
    public static final String REST_CONSULTA_CAMPOS = "/consultaCamposMassa/{transactionId}";
    public static final String REST_CONSULTA_COMBO_PROJETO = "/consultaComboProjeto/";    
    public static final String REST_CONSULTA_TRANSACOES_MASSA = "/consultaTransacoesMassa/{identification}";
    public static final String REST_CONSULTA_CAMPOS_MASSA = "/consultaCamposMassa/";
    
    /**
     * Massa de teste REST service URL's
     */
    public static final String REST_CADASTRO_CONFIGURACAO = "/cadastroConfiguracao";
    public static final String REST_CONSULTA_CONFIGURACAO_ID = "/consultaConfiguracao/{id}";
    public static final String REST_EXCLUIR_CONFIGURACAO_ID = "/excluirConfiguracao/{id}";
    
    /**
     * Exercita massa de teste REST service URL's
     */
    public static final String REST_CONSULTA_CAMPOS_ENTRADA = "/consultaCamposEntrada";
    public static final String REST_CONSULTA_COMBO_MECANISMO = "/consultaComboMecanismo";
    public static final String REST_CONSULTA_COMBO_ROTEADORAS = "/consultaComboRoteadoras/{layoutOutputTransactionId}";
    public static final String REST_CONSULTA_CAMPOS_ROTEADORA = "/consultaCamposRoteadora";
    public static final String REST_EXECUTA_TRANSACAO = "/executaTransacao";
    
}
