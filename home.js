var app = angular.module('myApp',[]);
app.controller('MyController', function($scope,$http) {
	$scope.insertdata=function(){
    $http.post("test1.php",{'devicename': $scope.devicename, 'devicetype': $scope.devicetype, 'selectedclient': $scope.selectedclient, 'devicenumber': $scope.devicenumber, 'nodename': $scope.nodename,'nodekey': $scope.nodekey,'nodenumber': $scope.nodenumber,'rulename': $scope.rulename,'maxtem': $scope.maxtem,'mintem': $scope.mintem,'alarmname': $scope.alarmname,'alarmtype': $scope.alarmtype})
        .success(function(data, status, headers, config){
            console.log("inserted Successfully");
			console.log("$data");
        });
	}
	$scope.items = ['one','two','three','four'];
});
app.controller('Myalarm', function($scope, $http) {
    $http.get("test_get.php")
    .success(function(cursor) {
        $scope.artists = cursor;
    });
});