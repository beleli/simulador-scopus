app.controller('changePassCtrl', 
		function($rootScope, $scope, $state ,$http, $translate, appConfig, utilService, md5, $mdDialog) {

	$scope.credentials = {};
	$scope.auxCredentials = {};
	
	$scope.limparCampos = function() {
		$scope.auxCredentials.password = '';
		$scope.auxCredentials.newPassword = '';
		$scope.auxCredentials.newPasswordConfirm = '';
	};

	$scope.limparCampos();

	$scope.changePassword = function() {
		$scope.credentials.email = $rootScope.user.username;
		$scope.credentials.password = md5.createHash($scope.auxCredentials.password || '');
		$scope.credentials.newPassword = md5.createHash($scope.auxCredentials.newPassword || '');
		
		var success = function(result) {
			switch (result.code) {
			case 0:
				$mdDialog.cancel();
				$scope.limparCampos();
				$rootScope.user.firstAccess = false;
				utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_CHANGE_PASSWORD');
				utilService.home();
				break;
			case -101:
				$scope.limparCampos();
				utilService.messageToast($scope, "error", 'INVALID_USER');
				break;
			case -102:
				$scope.limparCampos();
				utilService.messageToast($scope, "error", 'INVALID_PASSWORD');
				break;
			case -103:
				$scope.limparCampos();
				utilService.messageToast($scope, "error", 'INACTIVE_USER');
				break;
			default:
				$scope.limparCampos();
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};
		
		utilService.httpPost($scope, "/usuario/alterarSenha", $scope.credentials, success, null, true);
	};

	
});
