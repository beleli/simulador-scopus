app.controller('menuCtrl', function ($scope, $timeout, $mdSidenav, $log, $mdDialog, $state, $compile, utilService) {

	$scope.leftOpen = true;
	
	$scope.navigateTo = function(view) {
		$state.go(view);
	};
	
	$scope.showConfirmExit = function(ev) {
		utilService.dialogConfirm(ev, 'CONFIRMATION', 'MESSAGE_CONFIRMATION_LOGOUT', 'YES', 'NO', 
			function() {
		    	utilService.logout();
		    });
	};	
	
	
  $scope.openAndCloseLeft = function(){
	if ($mdSidenav('left').isOpen()) {
    	closeToggler('left');
    } else {
    	buildToggler('left');
    }
  };
  
  $scope.isOpenLeft = function(){
	  return true;
  };

  function buildToggler(navID) {
    $mdSidenav(navID).toggle().then(function () {$log.debug("toggle " + navID + " is done");});
  }

  function closeToggler(navID) {
    $mdSidenav(navID).close().then(function () {$log.debug("close" + navID + " is done");});
  }
});
