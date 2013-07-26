app.controller("BooksCtrl", ["$scope", "$location", "$stateParams", "Books", function ($scope, $location, $stateParams, booksService) {
  if($stateParams.pattern) {
    $scope.books = booksService.search($stateParams.pattern)
  }

  $scope.add = function(isbn) {
    alert(isbn)
  }
}]);
