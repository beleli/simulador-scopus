package br.com.scopus.simulador.portal.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Web Service properties configuration.
 * 
 * @author Eduardo Luiz Pelorca - epelorca@scopus.com.br
 * @since 08/03/2017
 */
@NoArgsConstructor
@EqualsAndHashCode
@Component
public class ApplicationConfigRestService implements Serializable {

    private static final long serialVersionUID = 7896624401620243915L;

    @Getter
    @Value("${web.service.url.saveUser}")
    private String saveUserWebSerivceURL;

    @Getter
    @Value("${web.service.url.userFindById}")
    private String userFindByIdWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.userRecoverPassword}")
    private String userRecoverPasswordWebSerivceURL;

    @Getter
    @Value("${web.service.url.userFindByName}")
    private String userFindByNameWebSerivceURL;

    @Getter
    @Value("${web.service.url.userFindByEmail}")
    private String userFindByEmailWebSerivceURL;

    @Getter
    @Value("${web.service.url.findRoleUserByProfile}")
    private String userFindRoleUserByProfileWebSerivceURL;

    @Getter
    @Value("${web.service.url.userChangePassword}")
    private String userChangePasswordWebSerivceURL;

    @Getter
    @Value("${web.service.url.userFindProfile}")
    private String userFindProfileWebSerivceURL;

    @Getter
    @Value("${web.service.url.userFindByUser}")
    private String userFindByUserWebSerivceURL;

    @Getter
    @Value("${web.service.url.projectByProject}")
    private String projectByProjectWebSerivceURL;

    @Getter
    @Value("${web.service.url.saveProject}")
    private String saveProjectWebSerivceURL;

    @Getter
    @Value("${web.service.url.projectFindById}")
    private String projectFindByIdWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.projectDelete}")
    private String projectDeleteWebServiceURL;
    
    @Getter
    @Value("${web.service.url.comboProject}")
    private String comboProjectWebServiceURL;

    @Getter
    @Value("${web.service.url.transactionByTransaction}")
    private String transactionByTransactionWebServiceURL;
    
    @Getter
    @Value("${web.service.url.transactionByTransactionView}")
    private String transactionByTransactionViewWebServiceURL;

    @Getter
    @Value("${web.service.url.saveTransaction}")
    private String saveTransactionWebServiceURL;
    
    @Getter
    @Value("${web.service.url.getParentFields}")
    private String findParentFieldsWebServiceURL;
    
    @Getter
    @Value("${web.service.url.scenarioTransactions}")
    private String findScenarioTransactionsWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.massTransactions}")
    private String findMassTransactionsWebSerivceURL;

    @Getter
    @Value("${web.service.url.transactionFindById}")
    private String transactionFindByIdWebServiceURL;

    @Getter
    @Value("${web.service.url.findTransactionTypes}")
    private String findTransactionTypes;

    @Getter
    @Value("${web.service.url.findTransactionFormats}")
    private String findTransactionFormats;

    @Getter
    @Value("${web.service.url.findTransactionByName}")
    private String findTransactionByName;
    
    @Getter
    @Value("${web.service.url.findFieldsType}")
    private String findFieldsType;
    
    @Getter
    @Value("${web.service.url.testScenarioSearch}")
    private String testScenarioSearch;
    
    @Getter
    @Value("${web.service.url.testScenarioFindById}")
    private String testScenarioFindById;
    
    @Getter
    @Value("${web.service.url.testScenarioSave}")
    private String testScenarioSave;
    
    @Getter
    @Value("${web.service.url.testScenarioDelete}")
    private String testScenarioDelete;
    
    @Getter
    @Value("${web.service.url.testScenarioFindByTransaction}")
    private String testScenarioFindByTransaction;
    
    @Getter
    @Value("${web.service.url.testMassFindByTestMass}")
    private String testMassFindByTestMass;
    
    @Getter
    @Value("${web.service.url.testMassFindFieldList}")
    private String testMassFindFieldList;
    
    @Getter
    @Value("${web.service.url.testMassFindById}")
    private String testMassFindById;
    
    @Getter
    @Value("${web.service.url.testMassSave}")
    private String testMassSave;
    
    @Getter
    @Value("${web.service.url.testMassDelete}")
    private String testMassDelete;

    @Getter
    @Value("${web.service.url.findParents}")
    private String findParents;
    
    @Getter
    @Value("${web.service.url.configurationFindByConfiguration}")
    private String configurationByConfigurationWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.configurationSave}")
    private String saveConfigurationWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.configurationFindById}")
    private String configurationFindByIdWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.configurationDelete}")
    private String configurationDeleteWebServiceURL;

    @Getter
    @Value("${web.service.url.findMechanisms}")
    private String comboMechanismWebServiceURL;
    
    @Getter
    @Value("${web.service.url.findInputList}")
    private String exerciseMassFindInputListWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.routingTransactions}")
    private String routingTransactionsWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.findRouterList}")
    private String exerciseMassFindRouterListWebSerivceURL;
    
    @Getter
    @Value("${web.service.url.deleteTransaction}")
    private String transactionDeleteWebServiceURL;
    
    @Getter
    @Value("${web.service.url.executeTransaction}")
    private String executeTransactionWebSerivceURL;
    
}
