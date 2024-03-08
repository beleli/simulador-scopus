app.controller('testMassCtrl', function($rootScope, $scope, utilService, $translate, $q) {

	console.log("testMassCtrl working!");

	$scope.resetTestMass = function() {
		$scope.testMass = {};
		$scope.testMass.timeout = 0;
		$scope.testMass.errorAverage = 0;
		$scope.testMass.occurrences = 1;
		$scope.testMass.returnCode = 0;
	};
	
	$scope.clearFields = function() {
		$scope.resetTestMass();
		$scope.selectedTabIndex = 0;
		$scope.testMassSearch = {};
		$scope.transaction = null;
		$scope.searchText = null;
		$scope.qtdBytesInput = 0;
		$scope.qtdBytesOutput = 0;
		
		utilService.resetForm($scope.formTestMass);
	};
	
	$scope.projectChange = function() {
		if (isNaN($scope.testMass.projectId)) {
			$scope.testMass.projectId = null;
		}
	};

	$scope.search = function() {
		$scope.page = 1;
		$scope.testMassSearch = angular.copy($scope.testMass);
		$scope.searchPage(1, $scope.optionTable.limit);
	};

	$scope.searchPage = function(page, limit) {
		var deferred = $q.defer();
		$scope.promise = deferred.promise;
		var pagedSearch = utilService.createPagedSearch(page, limit, $scope.testMassSearch);

		var success = function(result) {
			deferred.resolve();
			switch (result.code) {
			case 0:
				$scope.testMassList = result.content.data;
				$scope.totalRegisters = result.content.totalRegister;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		var error = function(result) {
			deferred.resolve();
		};

		utilService.httpPost($scope, "/testMass/consultaMassa/", pagedSearch, success, error);
	};
	
	$scope.save = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				if ($scope.testMass.id) {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TEST_MASS_UPDATE');
				} else {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TEST_MASS_REGISTERED');
				}
				$scope.clearFields();
				$scope.search();
				$scope.application = result.item;
				break;

			case -3:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_PARAMETERS_INVALIDS');
				break;
			case -4:
				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_REQUIRED_FIELD');
				break;
			case -5:
				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_YOU_MUST_CHANGE_ANY_FIELD');
				break;
			case -401:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_INVALID_PERMISSION');
				break;
			case -402:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_INVALID_INPUT_LIST');
				break;

			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};
		$scope.testMass.userId = $rootScope.user.id;
		utilService.httpPost($scope, "/testMass/cadastroMassa/", $scope.testMass, success, null, true);
	};
	
	$scope.isNullOrEmpty = function(item){
		return item == undefined || item == null || item == "";
	};
	
	$scope.deleteTestMass = function(ev, id) {
		utilService.dialogConfirm(ev, 'CONFIRMATION', 'MESSAGE_CONFIRMATION_DELETE_TEST_MASS', 'YES', 'NO',
			function() {
			var success = function(result) {
				switch (result.code) {
				case 0:
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TEST_MASS_DELETE');
					$scope.clearFields();
					$scope.search();
					$scope.application = result.item;
					break;
				case -3:
					utilService.messageToast($scope, "error", 'MESSAGE_ERROR_PARAMETERS_INVALIDS');
					break;
				case -401:
					utilService.messageToast($scope, "error", 'MESSAGE_ERROR_INVALID_PERMISSION');
					break;
				default:
					utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
				}
			}
			
			var aux = {};

			aux.userId = $rootScope.user.id;
			aux.id = id;
			utilService.httpPost($scope, "/testMass/excluirMassa/", aux, success, null, true);
		    });
	};

	$scope.searchProjects = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.projects = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService.httpGet($scope, "/testMass/consultaComboProjeto/", success, null, false);
	};
	
	$scope.searchTransaction = function(identification) {
		var deferred = $q.defer();
		var success = function(result) {
			switch (result.code) {
			case 0:
				deferred.resolve(result.content);
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		var error = function(result) {
			deferred.resolve();
		}
		utilService.httpGet($scope, "/testMass/consultaTransacoesMassa/" + identification, success, null, false);
		return deferred.promise;
	};
	
	$scope.changeTransactionFields = function(transactionId) {
		if (isNaN(transactionId)){
			$scope.scenarios = [];
			$scope.testMass.inputList = [];
			$scope.testMass.outputList = [];
			$scope.testMass.testScenarioId = null;
			$scope.qtdBytesInput = 0;
			$scope.qtdBytesOutput = 0;
			return;
		}
		$scope.searchTestScenario(transactionId);
		$scope.searchListFields(transactionId);
	};

	$scope.searchTestScenario = function(transactionId) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.scenarios = result.content;
				$scope.scenarioRequired = true;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		
		utilService.httpGet($scope, "/testMass/consultaComboCenarioTeste/" + transactionId, success, null, false);	
	};                                             
	
	$scope.searchListFields = function(transactionId) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.testMass = result.content;
				$scope.calculateLayoutBytes();
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		
		$scope.testMass.layoutOutputTransactionId = transactionId;
		utilService.httpPost($scope, "/testMass/consultaCamposMassa/", $scope.testMass, success, null, true);	
	};
	
	$scope.calculateLayoutBytes = function() {
		$scope.qtdBytesInput = 0;
		for (var i = 0; i < $scope.testMass.inputList.length; i++) {
			$scope.qtdBytesInput += $scope.testMass.inputList[i].size;
		}
		$scope.qtdBytesOutput = 0;
		for (var i = 0; i < $scope.testMass.outputList.length; i++) {
			$scope.qtdBytesOutput += $scope.testMass.outputList[i].size;
		}
	};

	$scope.edit = function(id) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.scenarioRequired = false;
				$scope.testMass = result.content;
				$scope.transaction = {};
				$scope.transaction.id = $scope.testMass.layoutOutputTransactionId;
				$scope.transaction.name = $scope.testMass.transactionName;
				$scope.selectedTabIndex = 0;
				 
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}

		utilService.httpGet($scope, "/testMass/consultaMassa/" + id, success, null, true);
	};

	$scope.resetTestMass();
	$scope.testMassList = [];
	$scope.testMassSearch = {};
	$scope.qtdBytesInput = 0;
	$scope.qtdBytesOutput = 0;
	$scope.scenarioRequired = true;

	if ($rootScope.authenticated) {
		$scope.searchProjects();
		$scope.search();
	}
});
