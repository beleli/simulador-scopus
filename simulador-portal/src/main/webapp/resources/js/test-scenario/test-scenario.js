app.controller('testScenarioCtrl', function($rootScope, $scope, utilService, $translate, $q, $mdDialog) {

	console.log("testScenario working!");

	$scope.testScenario = {};
	$scope.testScenarios = [];
	$scope.transaction = null;

	$scope.clearFields = function() {
		$scope.testScenario = {};
		$scope.testScenarioSearch = {};
		$scope.transaction = null;
		$scope.searchText = null;
		utilService.resetForm($scope.formTestScenario);
	};
	
	$scope.search = function() {
		$scope.page = 1;
		$scope.testScenarioSearch = angular.copy($scope.testScenario);
		$scope.searchPage(1, $scope.optionTable.limit);
	};

	$scope.searchPage = function(page, limit) {
		var deferred = $q.defer();
		$scope.promise = deferred.promise;

		var pagedSearch = utilService.createPagedSearch(page, limit,
				$scope.testScenarioSearch);

		var success = function(result) {
			deferred.resolve();
			switch (result.code) {
			case 0:
				$scope.testScenarios = result.content.data;
				$scope.totalRegisters = result.content.totalRegister;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		var error = function(result) {
			deferred.resolve();
		};

		utilService.httpPost($scope, "/testScenario/search", pagedSearch, success, error);

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

		utilService.httpGet($scope, "/testScenario/findTransactionsScenarios/" + identification, success, null, false);

		return deferred.promise;
	};
	

	$scope.save = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				if ($scope.testScenario.id) {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TESTSCENARIO_UPDATE');
				} else {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TESTSCENARIO_REGISTERED');
				}
				$scope.clearFields();
				$scope.search();
				$scope.application = result.item;
				break;

			case -3:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_PARAMETERS_INVALIDS');
				break;
			case -4:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_REQUIRED_FIELD');
				break;
			case -5:
				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_YOU_MUST_CHANGE_ANY_FIELD');
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};
		
		utilService.httpPost($scope, "/testScenario/cadastroCenario", $scope.testScenario, success, null, true);
	};
	
	$scope.changeTransaction = function(id) {
		$scope.testScenario.transactionId = id;	
	}
	
	$scope.edit = function(id) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.testScenario = result.content;
				$scope.transaction = {};
				$scope.transaction.name =  $scope.testScenario.transactionName;
				$scope.transaction.id =  $scope.testScenario.transactionId;
				$scope.searchText = $scope.testScenario.transactionName;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}

		utilService.httpGet($scope, "/testScenario/consultaCenario/" + id, success, null, true);
	};

	$scope.deleteTest = function(ev, id) {
		utilService.dialogConfirm(ev, 'CONFIRMATION', 'MESSAGE_CONFIRMATION_DELETE_SCENARIO', 'YES', 'NO', 
			function() {
			var success = function(result) {
				switch (result.code) {
				case 0:
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TESTSCENARIO_DELETE');
					$scope.clearFields();
					$scope.search();
					$scope.application = result.item;
					break;
				default:
					utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
				}
			}

			utilService.httpGet($scope, "/testScenario/delete/" + id, success, null, true);
		    });
	};
	
	if ($rootScope.authenticated) {
		$scope.search();
	}
});