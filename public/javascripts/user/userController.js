app.controller("UserCtrl", ["$scope", "$stateParams", "Users",function ($scope, $stateParams, Users) {

  $scope.user = Users.findById($stateParams.id);

}]);
