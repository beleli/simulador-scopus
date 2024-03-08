app.controller('userCtrl', function($rootScope, $scope, utilService, $translate, $q, $log) {

	console.log("applicationCtrl working!");

	$scope.user = {};
	$scope.profiles = [];
	$scope.users = [];

	$scope.clearFields = function() {
		$scope.user.id = "";
		$scope.user.name = "";
		$scope.user.email = "";
		$scope.user.idProfile = "3";
		$scope.searchText = "";
		$scope.user.enabled = true;
		utilService.resetForm($scope.formUsers);
	};

	$scope.save = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				if ($scope.user.id) {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_USER_UPDATE');
				} else {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_USER_REGISTERED');
				}
				$scope.clearFields();
				$scope.search();
				$scope.application = result.content;
				break;

			case -4:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_REQUIRED_FIELD');
				break;
			case -5:
				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_YOU_MUST_CHANGE_ANY_FIELD');
				break;
			case -107:
				utilService.messageToast($scope, "error", 'MESSAGE_MAIL_ALREADY_USED');
				break;
			case -108:
				utilService.messageToast($scope, "error", 'MESSAGE_MAIL_ALREADY_USED');
				break;

			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		utilService.httpPost($scope, "/usuario/cadastroUsuario/", $scope.user, success, null, true);
	};

	$scope.edit = function(id) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.searchText = "";
				$scope.user = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}

		utilService.httpGet($scope, "/usuario/consultaUsuario/" + id, success, null, true);
	};

	$scope.searchProfiles = function() {

		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.profiles = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService.httpGet($scope, "/usuario/getPerfis/", success, null, false);

	};

	$scope.search = function() {
		$scope.page = 1;
		$scope.userSearch = angular.copy($scope.user);
		$scope.searchPage(1, $scope.optionTable.limit);
	};

	$scope.searchPage = function(page, limit) {
		var deferred = $q.defer();
		$scope.promise = deferred.promise;

		var pagedSearch = utilService.createPagedSearch(page, limit, $scope.userSearch);

		var success = function(result) {
			deferred.resolve();
			switch (result.code) {
			case 0:
				$scope.users = result.content.data;
				$scope.totalRegisters = result.content.totalRegister;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		var error = function(result) {
			deferred.resolve();
		};

		utilService.httpPost($scope, "/usuario/consultaUsuario/", pagedSearch, success, error);
	}

	if ($rootScope.authenticated) {
		$scope.searchProfiles();
		$scope.search();
		
		// default values		
		$scope.user.idProfile = "3";
		$scope.user.enabled = true;
	}

});
