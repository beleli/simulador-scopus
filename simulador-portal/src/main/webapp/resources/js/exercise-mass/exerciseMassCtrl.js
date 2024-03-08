app.controller('exerciseMassCtrl', function($rootScope, $scope, utilService, $translate, $q) {

	console.log("exerciseMassCtrl working!");

	$scope.bytesInput = 0;
	$scope.bytesOutput = 0;
	$scope.exerciseMass = {};
	$scope.exerciseMass.transactionMechanismId = 1;
	$scope.routerName = null;
	
	$scope.clearFields = function() {
		$scope.selectedTabIndex = 0;

		$scope.exerciseMass = {};
		$scope.exerciseMass.transactionMechanismId = 1;
		$scope.transaction = null;
		$scope.bytesInput = 0;
		$scope.bytesOutput = 0;

		utilService.resetForm($scope.formExerciseMass);
	};
	
	$scope.calculateLayoutBytes = function() {
		$scope.bytesInput = 0;
		if ($scope.exerciseMass.inputList != null)
		for (var i = 0; i < $scope.exerciseMass.inputList.length; i++) {
			$scope.bytesInput += $scope.exerciseMass.inputList[i].size;
		}
		
		$scope.bytesOutput = 0;
		if ($scope.exerciseMass.outputList != null)
			for (var i = 0; i < $scope.exerciseMass.outputList.length; i++) {
				$scope.bytesOutput += $scope.exerciseMass.outputList[i].size;
			}
		
		$scope.bytesRouterInput = 0;
		if ($scope.exerciseMass.routerInputList != null)
		for (var i = 0; i < $scope.exerciseMass.routerInputList.length; i++) {
			$scope.bytesRouterInput += $scope.exerciseMass.routerInputList[i].size;
		}
		
		$scope.bytesRouterOutput = 0;
		if ($scope.exerciseMass.routerOutputList != null)
		for (var i = 0; i < $scope.exerciseMass.routerOutputList.length; i++) {
			$scope.bytesRouterOutput += $scope.exerciseMass.routerOutputList[i].size;
		}
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
	
	$scope.changeTransactionFields = function(layoutOutputTransactionId) {
		if (isNaN(layoutOutputTransactionId)){
			$scope.exerciseMass.inputList = [];
			$scope.exerciseMass.outputList = [];
			$scope.transactionRouters = [];
			return;
		}
		$scope.searchListFields(layoutOutputTransactionId);
		$scope.searchTransactionRouters(layoutOutputTransactionId);
	};
	
	$scope.searchListFields = function(layoutOutputTransactionId) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.exerciseMass = result.content;
				$scope.calculateLayoutBytes();
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		
		$scope.exerciseMass.layoutOutputTransactionId = layoutOutputTransactionId;
		utilService.httpPost($scope, "/exerciseMass/consultaCamposEntrada/", $scope.exerciseMass, success, null, true);	
	};
	
	$scope.searchTransactionRouters = function(layoutOutputTransactionId) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.transactionRouters = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService.httpGet($scope, "/exerciseMass/consultaComboRoteadoras/" + layoutOutputTransactionId, success, null, false);
	};
	
	$scope.routerChange = function() {
		if (isNaN($scope.exerciseMass.transactionRouterId)) {
			$scope.exerciseMass.transactionRouterId = null;
			$scope.routerName = null;
			
		} else {
			$scope.searchRouterListFields();
		}
	};
	
	$scope.searchRouterListFields = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.exerciseMass = result.content;
				$scope.calculateLayoutBytes();
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		
		utilService.httpPost($scope, "/exerciseMass/consultaCamposRoteadora/", $scope.exerciseMass, success, null, true);	
	};
	
	$scope.searchTransactionMechanisms = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.transactionMechanisms = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService.httpGet($scope, "/exerciseMass/consultaComboMecanismo/", success, null, false);
	};
	
	$scope.execute = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.exerciseMass = result.content;
				$scope.calculateLayoutBytes();
				break;
			case -3:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_PARAMETERS_INVALIDS');
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		
		//$scope.exerciseMass.layoutOutputTransactionId = layoutOutputTransactionId;
		utilService.httpPost($scope, "/exerciseMass/executaTransacao/", $scope.exerciseMass, success, null, true);
	};
	
	if ($rootScope.authenticated) {
		$scope.searchTransactionMechanisms();
	}
});