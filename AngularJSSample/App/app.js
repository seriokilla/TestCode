var app = angular.module('myApp', ['ngRoute'])
.config(['$routeProvider', function ($routeProvider) {
	$routeProvider
		.when('/', {
			templateUrl: 'views/home.html',
			controller: 'homeController'
		})
		.when('/about', {
			templateUrl: 'views/about.html',
			controller: 'aboutController'
		})
		.otherwise({
			redirectTo: '/'
		});
}])
.controller('mainController', function ($scope) {
	$scope.message = "Main Content";
	
	var fruits = [
		{ name: "apple", color: "red" },
		{ name: "bananna", color: "yellow" },
		{ name: "grape", color: "purple" },
		{ name: "orage", color: "orange" }
	];

	$scope.fruits = fruits;

	$scope.removeFruit = function (fruit) {
		var idx = $scope.fruits.indexOf(fruit);

		console.log("remove fruit " + fruit.name + " at idx " + idx);
	}
});;