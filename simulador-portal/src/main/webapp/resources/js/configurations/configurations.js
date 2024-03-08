app.controller('configurationsCtrl', function($rootScope, $scope, utilService, $translate, $q) {

	console.log("configurationsCtrl working!");

	$scope.configuration = {};
	$scope.configurations = [];
	$scope.configurationSearch = {};

	$scope.forceClearFields = function(id) {
		var s = document.getElementById(id);
		s.value = null;
	};
	
	$scope.clearFields = function() {
		$scope.configuration = {};
		$scope.configurationSearch = {};
		
		$scope.forceClearFields("name");
		$scope.forceClearFields("port");
		$scope.forceClearFields("timeout");
		$scope.forceClearFields("bytesAccess");
	
		utilService.resetForm($scope.formConfigurations);
	};

	$scope.searchTransactionTypes = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.transactionTypes = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService.httpGet($scope, "/transacao/findTransactionTypes/", success, null, false);
	};

	$scope.search = function() {
		$scope.page = 1;
		$scope.configurationSearch = angular.copy($scope.configuration);
		$scope.searchPage(1, $scope.optionTable.limit);
	};
	
	$scope.searchPage = function(page, limit) {
		var deferred = $q.defer();
		$scope.promise = deferred.promise;

		var pagedSearch = utilService.createPagedSearch(page, limit, $scope.configurationSearch);

		var success = function(result) {
			deferred.resolve();
			switch (result.code) {
			case 0:
				$scope.configurations = result.content.data;
				$scope.totalRegisters = result.content.totalRegister;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		var error = function(result) {
			deferred.resolve();
		};

		utilService.httpPost($scope, "/configuracao/search", pagedSearch, success, error);

	};

	$scope.save = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				if ($scope.configuration.id) {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_CONFIGURATION_UPDATE');
				} else {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_CONFIGURATION_REGISTERED');
				}
				$scope.clearFields();
				$scope.search();
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

		utilService.httpPost($scope, "/configuracao/cadastroConfiguracao/", $scope.configuration, success, null, true);
	};
	
	
	$scope.deleteConfiguration = function(ev, id) {
		utilService.dialogConfirm(ev, 'CONFIRMATION', 'MESSAGE_CONFIRMATION_DELETE_CONFIGURATION', 'YES', 'NO', 
			function() {
			var success = function(result) {
				switch (result.code) {
				case 0:
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_CONFIGURATION_DELETE');
					$scope.clearFields();
					$scope.search();
					$scope.application = result.item;
					break;
				default:
					utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
				}
			}

			utilService.httpGet($scope, "/configuracao/excluirConfiguracao/" + id, success, null, true);
		    });
	};

	
	$scope.edit = function(id) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.configuration = result.content;
				$scope.configuration.beginDate = new Date($scope.configuration.beginDate);
				$scope.configuration.endDate = new Date($scope.configuration.endDate);
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}

		utilService.httpGet($scope, "/configuracao/consultaConfiguracao/" + id, success, null, true);
	};

	if ($rootScope.authenticated) {
		$scope.searchTransactionTypes();
		$scope.search();
	}
});