app.controller("BooksCtrl", ["$scope", "$location", "$stateParams", "Books", function ($scope, $location, $stateParams, booksService) {
  if($stateParams.pattern) {
    $scope.books = booksService.search($stateParams.pattern)
    $scope.pattern = $stateParams.pattern
  }

  $scope.search = function() {
    $location.search("pattern", $scope.pattern)
  }

  $scope.add = function(isbn) {
    alert(isbn)
  }
}]);
