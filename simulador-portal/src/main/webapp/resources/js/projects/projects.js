app.controller('projectsCtrl', function($rootScope, $scope, utilService, $translate, $q) {

	console.log("projectsCtrl working!");

	$scope.project = {};
	$scope.beginDate = null;
	$scope.endDate = null;
	$scope.projects = [];
	$scope.projectSearch;
	$scope.projectUser = null;

	$scope.clearFields = function() {
		$scope.project = {};
		$scope.beginDate = null;
		$scope.endDate = null;
		$scope.projectUser = null;
		utilService.resetForm($scope.formProjects);
	};

	$scope.addUser = function(user, event) {
		if (user == undefined || user.id == undefined) {
			utilService.messageToast($scope, "error", 'MESSAGE_SELECT_USER');
			return;
		}
		if ($scope.project.users == undefined) {
			$scope.project.users = new Array();
		}
		for (var i = 0; i < $scope.project.users.length; i++)
			if ($scope.project.users[i].id == user.id) {
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_PROJECT_USER_EXITS');
				return;
			}
		$scope.project.users.push(user);
		$scope.searchText = ''; 
		$scope.projectUser = null;
		
	};

	$scope.removeUser = function(user, event) {
		for (var i = 0; i < $scope.project.users.length; i++)
			if ($scope.project.users[i].id == user.id) {
				$scope.project.users.splice(i, 1);
				break;
			}
	};

	$scope.searchComboUsers = function(nome) {
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

		utilService.httpPost($scope, "/usuario/buscaComboUsuarios", nome, success, error);

		return deferred.promise;
	};

	$scope.search = function() {
		$scope.page = 1;
		$scope.projectSearch = angular.copy($scope.project);
		$scope.searchPage(1, $scope.optionTable.limit);
	};
	
	$scope.searchPage = function(page, limit) {
		var deferred = $q.defer();
		$scope.promise = deferred.promise;

		var pagedSearch = utilService.createPagedSearch(page, limit, $scope.projectSearch);

		var success = function(result) {
			deferred.resolve();
			switch (result.code) {
			case 0:
				$scope.projects = result.content.data;
				$scope.totalRegisters = result.content.totalRegister;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		var error = function(result) {
			deferred.resolve();
		};

		utilService.httpPost($scope, "/projeto/search", pagedSearch, success, error);

	};

	$scope.save = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				if ($scope.project.id) {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_PROJECT_UPDATE');
				} else {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_PROJECT_REGISTERED');
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
			case -201:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_DATE_INTERVAL');
				break;
			case -202:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_PROJECT_REGISTERED');
				break;

			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		if ($scope.project.endDate < $scope.project.beginDate) {
			utilService.messageToast($scope, "error", 'MESSAGE_ERROR_DATE_INTERVAL');
			return;
		}

		utilService.httpPost($scope, "/projeto/cadastroProjeto/", $scope.project, success, null, true);
	};
	
	
	$scope.deleteProject = function(ev, id) {
		utilService.dialogConfirm(ev, 'CONFIRMATION', 'MESSAGE_CONFIRMATION_DELETE_PROJECT', 'YES', 'NO', 
			function() {
			var success = function(result) {
				switch (result.code) {
				case 0:
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_PROJECT_DELETE');
					$scope.clearFields();
					$scope.search();
					$scope.application = result.item;
					break;
				default:
					utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
				}
			}

			utilService.httpGet($scope, "/projeto/excluirProjeto/" + id, success, null, true);
		    });
	};

	
	$scope.edit = function(id) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.project = result.content;
				$scope.project.beginDate = new Date($scope.project.beginDate);
				$scope.project.endDate = new Date($scope.project.endDate);
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}

		utilService.httpGet($scope, "/projeto/consultaProjeto/" + id, success, null, true);
	};

	if ($rootScope.authenticated) {
		$scope.search();
	}

});