app.run(run);

app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', 
	function($stateProvider, $urlRouterProvider , $httpProvider){

		$urlRouterProvider.otherwise('/');

		$stateProvider
			.state('logar',{
				url:'/',
				templateUrl:'pages/open/login/login.html',
			})
			.state('home',{
				url:'/home',
				templateUrl:'pages/restricted/home/home.html'
			})
			.state('users',{
				url:'/users',
				templateUrl:'pages/restricted/users/users.html',
				controller:'userCtrl'
			})
			.state('projetcs',{
				url:'/projects',
				templateUrl:'pages/restricted/projects/projects.html',
				controller:'projectsCtrl'
			})
			.state('transactions',{
				url:'/transactions',
				templateUrl:'pages/restricted/transactions/transactions.html',
				controller:'transactionsCtrl'
			})
			.state('test-scenario',{
				url:'/test-scenario',
				templateUrl:'pages/restricted/test-scenario/test-scenario.html',
				controller:'testScenarioCtrl'
			})	
			.state('test-mass', {
				url:'/test-mass',
				templateUrl:'pages/restricted/test-mass/test-mass.html',
				controller:'testMassCtrl'
			})
			.state('change-password',{
				url:'/change-password',
				templateUrl:'pages/restricted/change-password/change-password.html',
				controller:'changePassCtrl'
			})
			.state('configurations',{
				url:'/configurations',
				templateUrl:'pages/restricted/configurations/configurations.html',
				controller:'configurationsCtrl'
			})
			.state('exercise-mass',{
				url:'/exercise-mass',
				templateUrl:'pages/restricted/exercise-mass/exercise-mass.html',
				controller:'exerciseMassCtrl'
			});
	}
]);

app.config(['$translateProvider', function ($translateProvider) {
	
	$translateProvider.preferredLanguage('pt_BR');
	
	$translateProvider.useStaticFilesLoader({
	  prefix: './././resources/i18n/',
	  suffix: '.json'
	});
}]);

app.config(function($mdThemingProvider) {
	$mdThemingProvider.theme('default')
    .primaryPalette('blue',{
      'default': '800', // use shade 200 for default, and keep all other shades the same
    });
});


run.$inject = ['$rootScope', '$log', '$state', '$location', 'utilService'];
function run ($rootScope, $log, $state, $location, utilService) { 
	  $rootScope.$on( "$stateChangeStart", function(event, next, current) {
		if(next.name != "logar") {
			utilService.httpGet($rootScope, "/login/user", 
				function (data){
					if (typeof data === 'string'){
						$rootScope.user=null;
						$rootScope.authenticated = false;
					} else {
						$rootScope.user = data; 
						$rootScope.authenticated = true;
						
						if (data.firstAccess) {
							utilService.showChangePassDialog($rootScope);
						}
					}
					
					if (!$rootScope.authenticated) {
						$state.go("logar", {}, {reload: true});
					}
				});
		} else {
			$rootScope.authenticated = false;
		}
	});
};

app.directive('nxEqual', function() {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, model) {
            if (!attrs.nxEqual) {
                console.error('nxEqual expects a model as an argument!');
                return;
            }
            scope.$watch(attrs.nxEqual, function (value) {
                model.$setValidity('nxEqual', value === model.$viewValue);
            });
            model.$parsers.push(function (value) {
                var isValid = value === scope.$eval(attrs.nxEqual);
                model.$setValidity('nxEqual', isValid);
                return isValid ? value : undefined;
            });
        }
    };
});

app.directive('nxValidadePassword', function() {
    return {
    	require: 'ngModel',
        link: function (scope, elem, attrs, model) {

            scope.$watch(attrs.nxValidadePassword, function (value) {
                model.$setValidity('nxValidadePassword', /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&&]{8,}$/.test(value));
            });
        }
    };

});



app.directive('mdAutocompleteSelect', ['$mdUtil', function ($mdUtil) {

	  function postLink(scope, element, attrs, autoComplete) {
	    let isSelected = function(item) {
	      if (item && item.id) {
	    	  autoComplete.scope.selectedItem = item;
	    	  return true;
	      } else {
	    	  return false;
	      }
	    };

	    $mdUtil.nextTick(function() {
	      let ngModel = element.find('input').controller('ngModel');

	      autoComplete.registerSelectedItemWatcher( function (selectedItem) {
	        ngModel.$setValidity('mdAutocompleteSelect', isSelected(selectedItem));
	      });

	      ngModel.$viewChangeListeners.push(function() {
	        $mdUtil.nextTick(function() {
	        	if (autoComplete.scope.searchText) {
	        		ngModel.$setValidity('mdAutocompleteSelect', isSelected(autoComplete.scope.selectedItem));
	        	} else {
	        		ngModel.$setValidity('mdAutocompleteSelect', true);
	        	}
	        });
	      });
	    });
	  };

	  return {
	    link: postLink,
	    require: 'mdAutocomplete',
	    restrict: 'A'
	  };
}]);

app.directive('apsUploadFile', function () {
	return  directive = {
	    restrict: 'E',
	    template: '<input id="fileInput" type="file" class="ng-hide" file-model="application.appleCertificateFile"> <md-input-container class="md-block flex md-input-has-value"> <label for="textInput">{{ "APPLE_APNS_CERTIFICATE" | translate }}</label> <input id="textInput" style="color: black !important" class="required" ng-model="application.appleCertificateFile.name" type="text" ng-readonly="true"> <div class="msgsError" ng-messages="formApplication.name.$error"><div ng-message="required">{{ "MESSAGE_ERROR_REQUIRED_FIELD" | translate }}</div><div ng-message="maxlength">{{ "MESSAGE_ERROR_MAX_LENGTH" | translate:"{ maxlength: "60" }" }}</div></div> </md-input-container>',
	    link: function (scope, element, attrs) {
    	  var input = $(element[0].querySelector('#fileInput'));
     	  var textInput = $(element[0].querySelector('#textInput'));

    	  if (input.length && textInput.length) {
    		  textInput.click(function(e) {
    	      textInput.val(null)
      	      input.val(null);
      	      scope.application.appleCertificateFile = null;
      	      input.click();
    	    });
    	  }
	  }
	}
});


app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            element.bind('change', function(){
                scope.$apply(function(){
                	scope.application.appleCertificateFile = null;//element[0].files[0];
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

//Diretiva para colocar o input do DatePicker readOnly e adicionar o evento para abrir o calendario.
app.directive('datepickerClick', function(){
    return{
        restrict: 'A',
        link: function(scope, ele, attr){
        	ele.find(".md-datepicker-button").each(function(){
                var el = this;
                var ip = angular.element(el).parent().find("input").bind('click', function(e){
                    angular.element(el).click();
                });
                angular.element(el).parent().find("input").prop('readonly', true);
                angular.element(el).parent().find("input").prop('style', "cursor:pointer");
            });
        } 									 
   };
});


//Diretiva para colocar o input do DatePicker readOnly e adicionar o evento para abrir o calendario.
app.directive('validDisabled', function(){
    return{
        restrict: 'A',
        link: function(scope, ele, attr){
        	ele.find(".md-datepicker-button").each(function(){
                var el = this;
                var ip = angular.element(el).parent().find("input").bind('click', function(e){
                    angular.element(el).click();
                });
                angular.element(el).parent().find("input").prop('readonly', true);
                angular.element(el).parent().find("input").prop('style', "cursor:pointer");
            });
        } 									 
   };
});



app.directive('maxLength', ['$compile', '$log', function($compile, $log) {
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function (scope, elem, attrs, ctrl) {
			attrs.$set("ngTrim", "false");
            var maxlength = parseInt(attrs.maxLength, 10);
            ctrl.$parsers.push(function (value) {
            	var str = value.toString();
                if (str.length > maxlength)
                {
                    str = str.substr(0, maxlength);
                    ctrl.$setViewValue(str);
                    ctrl.$render();
                    return str;
                }
                return value;
            });
		}
	};
}]);


app.directive('appendIcon', ['$timeout', '$compile', function($timeout, $compile) {
	  return {
		    restrict: 'A',
		    link: function(scope, elem, attrs) {
		      var vm = this;
		      $timeout(function() {
		        var label = angular.element(elem[0].querySelector('label'));
		        if (label.length > 0) {
			        var textName = label[0].innerHTML;
			        label[0].innerHTML = "";
			        var icon = $compile('<md-icon md-svg-src="'+ attrs['appendIcon'] + '"></md-icon>')(scope);
			        label.append(icon);
			        label.append(textName);
		        }
		      });
		    }
		  };	
}]);