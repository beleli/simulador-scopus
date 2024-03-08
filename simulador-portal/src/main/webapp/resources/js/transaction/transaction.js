app.controller('transactionsCtrl', function($rootScope, $scope, utilService, $translate, $q, $mdDialog) {

	console.log("transactionsCtrl working!");

	$scope.transaction = {};
	$scope.transactionSearch = {};
	$scope.transactions = {};
	$scope.transaction.inputFields = [];
	$scope.transaction.outputFields = [];
	$scope.transactionFieldsInput = [];
	$scope.transactionFieldsOutput = [];
	$scope.data = {};
	$scope.transactionFields = null;
	$scope.data.habilitaCampoInput = false;
	$scope.data.habilitaCampoOutput = false;
	$scope.data.copyCampoInput = false;
	$scope.data.copyCampoOutput = false;
	$scope.data.qtdStartFieldsInput = 0;
	$scope.data.qtdStartFieldsOutput = 0;

	$scope.forceClearFields = function(id) {
		var s = document.getElementById(id);
		s.value = null;
	};
	
	$scope.clearFields = function() {
		$scope.selectedTabIndex = 0;
		$scope.transaction = {}
		$scope.transactionSearch = {}
		$scope.transactions = {}
		$scope.transactionFields = null;
		$scope.data.habilitaCampoInput = false;
		$scope.data.habilitaCampoOutput = false;
		$scope.data.copyCampoInput = false;
		$scope.data.copyCampoOutput = false;
		$scope.transaction.inputFields = [];
		$scope.transaction.outputFields = [];
		$scope.transactionFieldsInput = []
		$scope.transactionFieldsOutput = []
		$scope.data.qtdStartFieldsInput = 0
		$scope.data.qtdStartFieldsOutput = 0
		$scope.searchTextOutput = '';
		$scope.searchTextIntput = '';
		$scope.forceClearFields("name");

		$scope.search();
		utilService.resetForm($scope.formTransactions);
	};

	$scope.closeModal = function(closeModal) {
	    $mdDialog.hide(closeModal);
	    $scope.openModal = undefined;
	};
	
	$scope.cancel = function() {
	    $mdDialog.hide();
	    $scope.openModal = undefined;
	};
	
	$scope.criarCampoNovo = function(ordem) {
		var campo = {};
		campo.ordinal = ordem;
		campo.name = "CAMPO " + ordem;
		campo.size =  1;
		campo.fieldTypeId = 1;
		return campo
	};
	
	$scope.createNewVersion = function() {
		$scope.removeAllFieldsOutput();
		$scope.transaction.transactionOutputId = null;
		for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
			$scope.transaction.inputFields[i].value = null;
		}
	};
	
	$scope.removeInput = function(campo){
		for (var i = 0; i < $scope.transaction.inputFields.length; i++)
			if ($scope.transaction.inputFields[i].ordinal == campo) {
				$scope.transaction.inputFields.splice(i, 1);
				break;
			}
		$scope.changeInputOrder();
		$scope.somaByteInput();
	}
	
	$scope.removeOutput = function(campo){
		for (var i = 0; i < $scope.transaction.outputFields.length; i++)
			if ($scope.transaction.outputFields[i].ordinal == campo) {
				$scope.transaction.outputFields.splice(i, 1);
				break;
			}
		$scope.changeOutputOrder();
		$scope.somaByteOutput();
	}
	
	$scope.inserirCampoAbaixoInput = function(campo) {
		var aux = [];
		for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
			aux.push($scope.transaction.inputFields[i]);
			if ($scope.transaction.inputFields[i].ordinal == campo) {
				aux.push($scope.criarCampoNovo(campo + 1));
			}
		}
		$scope.transaction.inputFields = aux;
		$scope.changeInputOrder();
		$scope.somaByteInput();
	}
	
	$scope.inserirCampoAbaixoOutput = function(campo) {
		var aux = [];
		for (var i = 0; i < $scope.transaction.outputFields.length; i++) {
			aux.push($scope.transaction.outputFields[i]);
			if ($scope.transaction.outputFields[i].ordinal == campo) {
				aux.push($scope.criarCampoNovo(campo + 1));
			}
		}
		$scope.transaction.outputFields = aux;
		$scope.changeOutputOrder();
		$scope.somaByteOutput();
	}
	
	$scope.inserirCampoAcimaInput = function(campo) {
		var aux = [];
		for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
			if ($scope.transaction.inputFields[i].ordinal == campo) {
				aux.push($scope.criarCampoNovo(campo));
			}
			aux.push($scope.transaction.inputFields[i]);
		}
		$scope.transaction.inputFields = aux;
		$scope.changeInputOrder();
		$scope.somaByteInput();
	}
	
	$scope.inserirCampoAcimaOutput = function(campo) {
		var aux = [];
		for (var i = 0; i < $scope.transaction.outputFields.length; i++) {
			if ($scope.transaction.inputFields[i].ordinal == campo) {
				aux.push($scope.criarCampoNovo(campo));
			}
			aux.push($scope.transaction.outputFields[i]);
		}
		$scope.transaction.outputFields = aux;
		$scope.changeOutputOrder();
		$scope.somaByteOutput();
	}
	
	$scope.somaByteInput = function() {
		var soma = 0;
		for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
			if (!isNaN($scope.transaction.inputFields[i].size)) {
				soma += $scope.transaction.inputFields[i].size;	
			}
		}
		$scope.transaction.qtdBytesInput = soma;
	}
	
	$scope.somaByteOutput = function() {
		var soma = 0;
		for (var i = 0; i < $scope.transaction.outputFields.length; i++) {
		  if (!isNaN($scope.transaction.outputFields[i].size)) {
			  soma += $scope.transaction.outputFields[i].size;	
			}
		}
		$scope.transaction.qtdBytesOutput = soma;
	}
	
	$scope.inserirCampoInput = function() {
		var qtd;	
		if ($scope.transaction.inputFields == undefined) {
			qtd = 0;
			$scope.transaction.inputFields = []
		} else {
			qtd = $scope.transaction.inputFields.length;
		}
		var aux = [];
		if ($scope.data.copyCampoInput) {
			if ($scope.transactionFieldsInput != null) 
				$scope.transaction.inputFields = $scope.transactionFieldsInput.inputFields;
			for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
				$scope.transaction.inputFields[i].parent = false;
				$scope.transaction.inputFields[i].id = null;
			}
			
			$scope.transactionFieldsInput = null;
			$scope.data.copyCampoInput = false;
			$scope.searchTextInput = ''; 
			$scope.somaByteInput();
			utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_FIELD_INPUT_COPY');
			
		} else if ($scope.data.habilitaCampoInput) {
			if ($scope.data.qtdStartFieldsInput != null || $scope.data.qtdStartFieldsInput > 0) {
				for (i = 1; i <= $scope.data.qtdStartFieldsInput; i++) {
					$scope.transaction.inputFields.push($scope.criarCampoNovo(qtd + i));
				}
				
				$scope.data.habilitaCampoIntput = false
				$scope.data.qtdStartFieldsIntput = null;
				$scope.somaByteInput();
				utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_FIELD_INPUT_CREATE');
				
			} else {
				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_CHECK_BOX_NUMBER_FIELD_TRUE_INPUT');
			}
			
		} else {
			$scope.transaction.inputFields.push($scope.criarCampoNovo(qtd + 1));
			$scope.somaByteInput();
		}
	}
	
	$scope.inserirCampoOutput = function() {
		var qtd;	
		if ($scope.transaction.outputFields == undefined) {
			qtd = 0;
			$scope.transaction.outputFields = []
		} else {
			qtd = $scope.transaction.outputFields.length;
		}
		var aux = [];
		if ($scope.data.copyCampoOutput) {
			if($scope.transactionFieldsOutput != null) 
				$scope.transaction.outputFields = $scope.transactionFieldsOutput.outputFields;
			
			for (var i = 0; i < $scope.transaction.outputFields.length; i++) {
				$scope.transaction.outputFields[i].parent = false;
				$scope.transaction.outputFields[i].id = null;
			}
			
			$scope.transactionFieldsOutput = null;
			$scope.data.copyCampoOutput = false;
			$scope.searchTextOutput = '';
			$scope.somaByteOutput();
			utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_FIELD_INPUT_COPY');
		
		} else if ($scope.data.habilitaCampoOutput) {
			if ($scope.data.qtdStartFieldsOutput != null || $scope.data.qtdStartFieldsOutput > 0) {
				for (i = 1; i <= $scope.data.qtdStartFieldsOutput; i++) {
					$scope.transaction.outputFields.push($scope.criarCampoNovo(qtd + i));
				}
				$scope.data.habilitaCampoOutput = false
				$scope.data.qtdStartFieldsOutput = null;
				$scope.somaByteOutput();
				utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_FIELD_INPUT_CREATE');
			} else {
				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_CHECK_BOX_NUMBER_FIELD_TRUE_OUTPUT');
			}
			
		} else {
			$scope.transaction.outputFields.push($scope.criarCampoNovo(qtd + 1));
			$scope.somaByteOutput();
		}
	}
	
	$scope.changeInputOrder = function() {
		var ordem = 1;
		for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
			$scope.transaction.inputFields[i].ordinal = ordem;
			ordem += 1;
		} 
	}
	
	$scope.changeOutputOrder = function() {
		var ordem = 1;
		for (var i = 0; i < $scope.transaction.outputFields.length; i++) {
			$scope.transaction.outputFields[i].ordinal = ordem;
			ordem += 1;
		} 
	}
	
	$scope.removeAllFieldsInput = function() {
		var aux = [];
		for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
			if ($scope.transaction.inputFields[i].parent)
				aux.push($scope.transaction.inputFields[i]);
		}
		$scope.transaction.inputFields = aux;
		$scope.changeInputOrder();
		$scope.somaByteInput();	
	}
	
	$scope.removeAllFieldsOutput = function() {
		$scope.transaction.outputFields = [];
		$scope.somaByteOutput();
	}
	
	$scope.clearNameTransactionInput = function () {
		if ($scope.data.copyCampoInput) {
			$scope.transactionFieldsInput = null;
			$scope.data.habilitaCampoInput = false
			$scope.searchTextInput = ''; 
		} else {
			 
			$scope.data.qtdStartFieldsInput = null;
		}
	};
	 
	 $scope.clearNameTransactionOutput = function () {
		 if ($scope.data.copyCampoOutput) {
			 $scope.transactionFieldsOutput = null;
			 $scope.data.habilitaCampoOutput = false
			 $scope.searchTextOutput = ''; 
		 } else {
			 $scope.data.qtdStartFieldsOutput = null;
		 }
	 };
	 
	 $scope.disableCopyInput = function() {
		 if ($scope.data.habilitaCampoInput) {
			 $scope.transactionFieldsInput = null;
			 $scope.searchTextInput = ''; 
			 $scope.data.copyCampoInput = false;
		 }else {
			 $scope.data.qtdStartFieldsInput = null;
		 }
	 }
	 
	 $scope.disableCopyOutput = function() {
		 if ($scope.data.habilitaCampoOutput) {
			 $scope.transactionFieldsOutput = null;
			 $scope.searchTextOutput = ''; 
			 $scope.data.copyCampoOutput = false;
		 } else {
			 $scope.data.qtdStartFieldsOutput = null;
		 }
	 }
	 
	$scope.searchTransactionFields = function(nome) {
		var deferred = $q.defer();
		
		var dto = {}
		dto.name = nome;
		
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

		utilService.httpPost($scope, "/transacao/findTransactionByName", dto, success, error);
		return deferred.promise;
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
		utilService
				.httpGet($scope, "/transacao/findTransactionTypes/", success, null, false);
	};
	
	$scope.searchFieldsTypes = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.fieldsTypes = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService
				.httpGet($scope, "/transacao/findFieldsType/", success, null, false);
	};
	
	$scope.searchTransactionFormats = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.transactionFormats = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService.httpGet($scope, "/transacao/findTransactionFormats/", success, null, false);
	};
	
	$scope.searchTransactionParents = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.transactionParents = result.content;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}
		utilService.httpGet($scope, "/transacao/findParents/", success, null, false);
	};
	
	$scope.search = function() {
		$scope.page = 1;
		$scope.transactionSearch = angular.copy($scope.transaction);
		$scope.searchPage(1, $scope.optionTable.limit);
	};

	$scope.searchPage = function(page, limit) {
		var deferred = $q.defer();
		$scope.promise = deferred.promise;

		var pagedSearch = utilService.createPagedSearch(page, limit,
				$scope.transactionSearch);

		var success = function(result) {
			deferred.resolve();
			switch (result.code) {
			case 0:
				$scope.transactions = result.content.data;
				$scope.totalRegisters = result.content.totalRegister;
				$scope.totalRegisterInput = 0;
				$scope.totalRegisterOutput = 0;
				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		var error = function(result) {
			deferred.resolve();
		};

		utilService.httpPost($scope, "/transacao/search", pagedSearch, success, error);
	};
	
	$scope.parentChange = function() {
		if (isNaN($scope.transaction.transactionParentId)) {
			$scope.transaction.transactionParentId = null;
			$scope.removeAllParentFields();
		} else {
			$scope.getInputFields();
		}
	};

	$scope.removeAllParentFields = function() {
		var aux = [];
		for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
			if (!$scope.transaction.inputFields[i].parent)
				aux.push($scope.transaction.inputFields[i]);
		}
		$scope.transaction.inputFields = aux;
		$scope.changeInputOrder();
		$scope.somaByteInput();		
	}
	
	$scope.getInputFields = function() {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.removeAllParentFields();
				var parents = result.content;
				var aux = [];
				for (var i = 0; i < parents; i++) {
					aux.push(parents[i]);
				}
				for (var i = 0; i < $scope.transaction.inputFields.length; i++) {				
					if (i == 0) {
						$scope.transaction.inputFields[i].value = $scope.transaction.name;
					}
					aux.push($scope.transaction.inputFields[i]);
				}
				$scope.transaction.inputFields = aux;
				$scope.changeInputOrder();
				$scope.somaByteInput();

				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}

		var temp = {};
		temp.id = $scope.transaction.transactionParentId;
		
		utilService.httpPost($scope, "/transacao/findParentFields", temp, success, true);
	}
	
	$scope.routerChange = function() {
		if (isNaN($scope.transaction.routerTypeId)) {
			$scope.transaction.routerTypeId = null;
		}
	}

	$scope.save = function() {
		if ($scope.transaction.inputFields.length <= 0) {
			utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_INPUT_FIELD');
			return;
		} else {
			var find = false;
			for (var i = 0; i < $scope.transaction.inputFields.length; i++) {
				if (!$scope.transaction.inputFields[i].parent) {
					find = true;
					break;
				}
			}
			if (!find) {
				utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_INPUT_FIELD');
				return;
			}
		}
		
		if (!($scope.transaction.parent) && $scope.transaction.outputFields.length <= 0) {
			utilService.messageToast($scope, "warn", 'MESSAGE_ERROR_OUTPUT_FIELD');
			return;
		}
				
		var success = function(result) {
			switch (result.code) {
			case 0:
				if ($scope.user.id) {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TRANSACTION_UPDATE');
				} else {
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TRANSACTION_REGISTERED');
				}
				$scope.clearFields();
				$scope.search();
				$scope.application = result.content;
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
			case -301:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_INPUT_FIELD');
				break;
			case -302:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_OUTPUT_FIELD');
				break;
			case -303:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_INVALID_TRANSACTION_VERSION');
				break;	
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		};

		utilService.httpPost($scope, "/transacao/cadastroTransacao", $scope.transaction, success, null, true);
	};
	
	$scope.edit = function(id, layoutOutputId) {
		var success = function(result) {
			switch (result.code) {
			case 0:
				$scope.transaction = result.content;
				$scope.selectedTabIndex = 0;

				break;
			default:
				utilService.messageToast($scope, "error", 'MESSAGE_ERROR_GENERIC');
			}
		}

		var temp = {};
		temp.id = id;
		temp.transactionOutputId = layoutOutputId;
		
		utilService.httpPost($scope, "/transacao/consultaTransacao", temp, success, true);
	};
	
	$scope.deleteTransaction = function(ev, id) {
		utilService.dialogConfirm(ev, 'CONFIRMATION', 'MESSAGE_CONFIRMATION_DELETE_TRANSACTION', 'YES', 'NO', 
			function() {
			var success = function(result) {
				switch (result.code) {
				case 0:
					utilService.messageToast($scope, "success", 'MESSAGE_SUCCESS_TRANSACTION_DELETE');
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
			utilService.httpPost($scope, "/transacao/excluirTransacao/", aux, success, null, true);
		    });
	};

	if ($rootScope.authenticated) {
		$scope.searchTransactionTypes();
		$scope.searchTransactionFormats();
		$scope.searchFieldsTypes();
		$scope.searchTransactionParents();
		$scope.search();
	}
	
	$scope.showModalTransaction = function(ev, inputField ) {
		$mdDialog.show({
		      controller: 'transactionsSearchCtrl',
		      templateUrl: 'modal-grid-transacao.html',
		      parent: angular.element(document.body),
		      targetEvent: ev,
		      skipHide: true,
		      scope: $scope,
		      preserveScope: true,
		      clickOutsideToClose:false,
		      locals: { inputField: inputField}
		}) 
	}
});
