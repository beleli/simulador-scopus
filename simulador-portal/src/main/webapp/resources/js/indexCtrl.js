app.controller('indexCtrl', function($scope, $rootScope, $translate) {
	
	function changeLabelPagination() {
		$translate(['PAGE', 'OF']).then(function (translations) {
			$scope.labelPagination= {page: translations['PAGE'], of: translations['OF']};
		});
	}
	
	changeLabelPagination();
	
	$scope.promise;
		
	$scope.optionTable = {limit: 5, pageSelect: true, boundaryLinks: true};
	
	$scope.changeLanguage = function(language) {
		$translate.use(language);
		changeLabelPagination();
	};
	
	$scope.verifyLanguage = function(language) {
		return $translate.use()==language;
	};
	
	
	$scope.hasRole = function(role) {
		var authority = false
		if ($rootScope.user) {
			$rootScope.user.authorities.forEach(function (value) {
				if (value.authority===role) {
					authority = true;
				}
			});
		}
		
		return authority;
	};
	
	$scope.loggedUserName = function() {
		if ($rootScope.user && $rootScope.user.name) {
			return $rootScope.user.name;
		}
		return '';
	}

	$scope.loggedProfileUserName = function() {
		if ($rootScope.user && $rootScope.user.nameProfile) {
			return $rootScope.user.nameProfile;
		}
		return '';
	}

});
