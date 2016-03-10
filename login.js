var app = angular.module('myApp',[]);
app.controller('PostController',function($scope, $http) {
		
		this.postForm = function() {
		$http.post("check-login.php",{'usernames': $scope.usernames, 'passwords': $scope.passwords})
			.success(function(data, status, headers, config) {
				console.log(data);
				if ( data.trim() === 'correct') {
					window.location.href = 'test1.html';
				} else {
					$scope.errorMsg = "Login not correct";
				}
			})
			.error(function(data, status, headers, config) {
				$scope.errorMsg = 'Unable to submit form';
			})
		}
		
	});
 app.controller('MyController', function($scope,$http) {
	$scope.insertdata=function(){
    $http.post("iot_register.php",{'frist': $scope.frist, 'last': $scope.last, 'mobile': $scope.mobile, 'user': $scope.user, 'password': $scope.password})
        .success(function(data, status, headers, config){
            console.log("inserted Successfully");
			console.log("$data");
        });
	}
}); 
 