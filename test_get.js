var app = angular.module('myApp', []);
app.controller('Myalarm', function($scope, $http) {
    $http.get("test_get.php")
    .success(function(cursor) {
        $scope.artists = cursor;
    });
});