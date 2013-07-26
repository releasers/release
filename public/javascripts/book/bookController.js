app.controller("BookCtrl", ["$scope", "Books", "$stateParams", function ($scope, Books, $stateParams) {

  $scope.book = Books.findById($stateParams.id);

}]);
