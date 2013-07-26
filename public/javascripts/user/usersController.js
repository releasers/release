app.controller("UsersCtrl", ["$scope", "Users", function ($scope, usersService) {

    usersService.list().then(function(users) {
        $scope.users = users
    })

    $scope.orderProp = 'profile.name';
}]);