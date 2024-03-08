package br.com.scopus.simulador.portalws.rest;

/**
 * Constantes para os servicos rest da aplicacao.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public final class RESTServices {

    public static final String CONTROLLER_USER = "/user";
    public static final String CONTROLLER_PROJECT = "/project";
    public static final String CONTROLLER_TRANSACTION = "/transaction";
    public static final String CONTROLLER_TEST_SCENARIO = "/testScenario";
    public static final String CONTROLLER_TEST_MASS = "/testMass";
    public static final String CONTROLLER_EXERCISE_MASS = "/exerciseMass";
    public static final String CONTROLLER_CONFIGURATION = "/configuration";

    public static final String REST_SAVE = "/save";
    public static final String REST_DELETE = "/delete/{id}";
    public static final String REST_DELETE_DTO = "/delete";
    public static final String REST_RECOVER_PASSWORD = "/recoverPassword/{email}/";
    public static final String REST_SEARCH = "/search";
    public static final String REST_FIND_BY_ID = "/findById/{id}";
    public static final String REST_FIND_BY_EMAIL = "/findByEmail/{email}/";
    public static final String REST_FIND_BY_NAME = "/findByName";
    public static final String REST_FIND_ROLE_USER_BY_PROFILE = "/findRoleUserByProfile/{idProfile}";
    public static final String REST_CHANGE_PASSWORD = "/changePassword";
    public static final String REST_GET_PROFILES = "/findProfiles";
    public static final String REST_GET_PARENT_FIELDS = "/findParentFields";

    public static final String REST_FIND_TRANSACTION_TYPES = "/findTransactionTypes";
    public static final String REST_FIND_TRANSACTION_FORMATS = "/findTransactionFormats";
    public static final String REST_FIND_FIELD_TYPES = "/findFieldsType";
    public static final String REST_FIND_PARENTS = "/findParents";
    public static final String REST_FIND_ACTIVES = "/findActives";
    public static final String REST_FIND_TRANSACTION = "/findByTransaction";
    public static final String REST_FIND_TRANSACTIONS_SCENARIO = "/findScenarioTransactions/{identification}";
    public static final String REST_FIND_TRANSACTIONS_MASS = "/findMassTransactions/{identification}";
    public static final String REST_FIND_SCENARIO_MASS = "/findScenarioMass/{layoutOutputTransactionId}";
    public static final String REST_FIND_TRANSACTION_FIELDS = "/findTransactionFields";
    public static final String REST_FIND_TRANSACTION_MECHANISMS = "/findMechanisms";
    public static final String REST_FIND_TRANSACTION_ROUTER = "/findRoutingTransactions/{layoutOutputTransactionId}";
    public static final String REST_FIND_ROUTER_FIELDS = "/findRouterFields";
    public static final String REST_EXECUTE = "/execute";

    private RESTServices() {

    }

}
