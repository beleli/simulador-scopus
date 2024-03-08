app.service('utilService', function($mdToast, $translate, $state, $rootScope, $http, $mdDialog, $timeout, appConfig, growl) {
	
	
	var _createPagedSearch = function (page, limit, object) {
		var pagedSearch = {"page": page, "limit": limit, "item": object};
		return pagedSearch;
	} 
	
	var _messageToast = function ($scope, css, text) {

		
		
		/*$translate(text).then(function (tranlated) {
			var toast = $mdToast.simple()
								.textContent(tranlated)
								.position('top right')
								.hideDelay(6000);
			
			$mdToast.show(toast);
		});*/
		
		
		
		$translate(text).then(function (tranlated) {
	     text = tranlated
		});
		
		if (css === "info") {
			growl.info(text);
		} else if (css == "success") {
			growl.success(text);
		} else if (css == "warn") {
			growl.warning(text);
		} else if (css == "error") {
			growl.error(text);
		}
	}
	
	var _resetForm = function(form) {
	    form.$setUntouched();
	    form.$setPristine();
	    /* Não utilizar, usar o ng-if="formTransactions.acTransactionFieldsOutput.$touched"
	     * 
		var msgsContainer = document.getElementsByClassName("msgsError");
		for(var indexContainer = 0; indexContainer < msgsContainer.length; indexContainer++) {
			for(var indexMsg = 0; indexMsg < msgsContainer[indexContainer].children.length; indexMsg++) {
				var element = msgsContainer[indexContainer].children[indexMsg];
				element.style.opacity = 0;
			}
		}*/
	}
	
	var _dialogConfirm = function (ev, title, message, msgButtonOk, msgButtonCancel, callbackOk, callbackCancel) {

		$translate([title, message, msgButtonOk, msgButtonCancel]).then(function (translations) {
			var confirm = $mdDialog.confirm()
			.title(translations[title])
		    .textContent(translations[message])
		    .ariaLabel('Lucky day')
		    .targetEvent(ev)
		    .ok(translations[msgButtonOk])
		    .cancel(translations[msgButtonCancel]);
			
		    $mdDialog.show(confirm).then(function() {
		    	if (callbackOk)
		    		callbackOk();
		    }, function() {
		    	if (callbackCancel)
		    		callbackCancel();
		    });
		});
	}
	
	var _logout = function() {
		console.log("Realizando logout...");
		$state.go('logar').finally(function() {$window.location.reload();});
		$http.post('/' + appConfig.contextName + '/logout', {}).success(function() {
			console.log("Logout realizado com sucesso");
		}).error(function(data) {
			console.log("Logout falhou");
		});
		$rootScope.authenticated = false;
	};
	
	var _home = function() {
		console.log("Direcinando para a home...");
		$state.go('home').finally(function() {$window.location.reload();});
	};
	
	var _httpGet = function($scope, path, successCallback, errorCallback, withSpinner, config){
		if (withSpinner){
			_dialogProgressShow($scope);
		}
		_trataRetorno($scope, $http.get('/' + appConfig.contextName + "/rest" + path, config), successCallback, errorCallback, withSpinner);
	};
	
	var _httpPost = function($scope, path, params, successCallback, errorCallback, withSpinner){
		if (withSpinner){
			_dialogProgressShow($scope);
		}
		_trataRetorno($scope, $http.post('/' + appConfig.contextName + "/rest" + path, params), successCallback, errorCallback, withSpinner);
	};
	
	var _httpPostWithConfig = function($scope, path, params, config, successCallback, errorCallback, withSpinner){
		if (withSpinner){
			_dialogProgressShow($scope);
		}
		_trataRetorno($scope, $http.post('/' + appConfig.contextName + "/rest" + path, params, config), successCallback, errorCallback, withSpinner);
	};

	var _httpPut = function($scope, path, params, successCallback, errorCallback, withSpinner){
		if (withSpinner){
			_dialogProgressShow($scope);
		}
		_trataRetorno($scope, $http.put('/' + appConfig.contextName + "/rest" + path, params), successCallback, errorCallback, withSpinner);
	};
	
	var _trataRetorno = function ($scope, requestReturn, successCallback, errorCallback, withSpinner) {
		
		requestReturn.success(function(data){
			if (withSpinner){
				_dialogProgressHide();
			}

			if (successCallback) {
				successCallback(data);
			}
		}).error(function(data, status){
			if (withSpinner){
				_dialogProgressHide();
			}
			if (errorCallback) {
				errorCallback(data, status);
			}
			
			switch (status) {
			case 401:
				$state.go('/');
				break;
			case 403:
				_messageToast($scope, "error", 'MESSAGE_ERROR_403');
				break;
			default:
				_messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
				break;
			}
		});
	}
	
	_showChangePassDialog  = function($scope) {
		$mdDialog.show({
	      controller: 'changePassCtrl',
	      templateUrl: 'pages/restricted/login/dialog-change-password.html',
	      parent: angular.element(document.body),
	      clickOutsideToClose:false
	    }) .then(function(answer) {
	        alert(answer);
	    }, function() {
	    	_logout();
	    });
	};
	
	var progress;
	_dialogProgressShow  = function($scope) {
	    progress = $mdDialog.show({
	      templateUrl: 'pages/open/dialog/dialog-progress.html',
	      parent: angular.element(document.body),
	      clickOutsideToClose:false,
	      scope: $scope,
	      fullscreen: $scope.customFullscreen,
	      preserveScope: true,
	      skipHide: true
	    });
	};
	
	_dialogProgressHide  = function($scope) {
		$timeout(function(){$mdDialog.hide(); progress = undefined;}, 100);
	}
	
	return {
		messageToast: _messageToast,
		logout: _logout, 
		home: _home,
		httpGet: _httpGet,
		httpPost: _httpPost,
		httpPostWithConfig: _httpPostWithConfig,
		httpPut: _httpPut,
		dialogProgressShow : _dialogProgressShow,
		dialogProgressHide: _dialogProgressHide,
		showChangePassDialog: _showChangePassDialog,
		dialogConfirm: _dialogConfirm,
		createPagedSearch: _createPagedSearch,
		resetForm: _resetForm
	};
});