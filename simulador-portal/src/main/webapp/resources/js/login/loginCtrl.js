app.controller('loginCtrl', function($rootScope, $scope, $state ,$http, $translate, $mdDialog, $timeout, $templateCache, appConfig, utilService, $window) {

	console.log("loginCtrl working!");	
	
	$scope.credentials = {};
	$scope.email = "";
	
	$scope.limparCampos = function() {
		$scope.credentials.password = '';
		utilService.resetForm($scope.formLogin);
	};
	
	$scope.recoverPassword = function() {
		var success = function(result) { 
			switch (result.code) {
			case 0:
				utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_RECOVER_PASSWORD');
				$scope.cancel();
				break;
				
			case -4:
				utilService.messageToast($scope, "success", 'MESSAGE_ERROR_REQUIRED_FIELD');
				$scope.limparCampos();
				break;
				
			case -101:
				utilService.messageToast($scope, "success", 'MESSAGE_ERROR_INCORRECT_USER');
				$scope.limparCampos();
				break;
			
			case -103:
				utilService.messageToast($scope, "success", 'MESSAGE_ERROR_USER_DESABLE');
				break;
				
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		utilService.httpGet($scope, "/login/recuperarSenha/" + $scope.email + "/", success, null, false);
	};

	$scope.login = function() {
		$http({
            method: 'POST',
            url: '/' + appConfig.contextName + '/login',
            data: "username=" + $scope.credentials.username + "&password=" + $scope.credentials.password,
            headers: {
            	"Content-Type": "application/x-www-form-urlencoded",
            	"X-Login-Ajax-call": "true" }
        }).then(
        		function successCallback(response) {
        			
        			if (response.data.codigoRetorno == 'SUCESSO' || response.data.codigoRetorno == 'SUCCESS') {
        				$rootScope.authenticated = true;
        				console.log("Login realizado com sucesso");
        				$state.go('home').finally(function() {$window.location.reload();});
        				
        			} else {
        				$rootScope.authenticated = false;
        				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
        			}
        			
        		}, 
        		function errorCallback(response) {
        			
        			$rootScope.authenticated = false;
        			if(response.status == 401){
        				$scope.limparCampos();
        				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_INCORRECT_USER_PASS');
        			}else{
        				console.log("Login falhou");
            			$scope.limparCampos();
            			utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
        			}
        		});
	};

	$scope.showForgotDialog  = function(ev) {
	    $mdDialog.show({
	      controller: 'loginCtrl',
	      templateUrl: 'pages/open/login/dialog-forgot-password.html',
	      parent: angular.element(document.body),
	      clickOutsideToClose:false,
	      targetEvent: ev,
	      scope: $scope,
	      fullscreen: $scope.customFullscreen,
	      preserveScope: true,
	      skipHide: true
	    })
	};
	
	$scope.cancel = function() {
	    $mdDialog.cancel();
	};
	
	$scope.esqueciMinhaSenha = {};
	
});
