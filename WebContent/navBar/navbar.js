const ROOT_HREF_1 = "/PicOnClick/#/";

let NavBarController = function($location, $window, $scope, $rootScope) {
	$scope.logout = function() {
		localStorage.removeItem('token1');
		console.log('redireting to login');
		return $location.path('login');
	};
	$scope.isLoggedIn = !!localStorage.getItem('token1');
	$scope.user = JSON.parse(localStorage.getItem('token1'));
	$scope.isAdmin = function() {
		if ($scope.user) {
			return $scope.user.admin;
		} else {
			return false;
		}
	};
	
	$scope.isOperator = function() {
		if ($scope.user) {
			return $scope.user.admin || $scope.user.operator;
		}
		return false;
	};

	$scope.isSeller = false;

	if ($scope.user) {
		if (!$scope.isOperator() && !$scope.isAdmin()) {
			$scope.isSeller = $scope.user.seller;
		} else {
			$scope.isSeller = true;
		}
	}
	console.log('isAdmin: ', $scope.isAdmin());
};
angular.module('myApp.navBar', [])
	.controller('NavBarController', NavBarController)
	.component('navBar', {
		templateUrl: 'navBar/navbar.html',
		controller: 'NavBarController'
	});

