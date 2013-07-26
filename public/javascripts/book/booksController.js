app.controller("BooksCtrl", ["$scope", "$location", "$stateParams", "Books", function ($scope, $location, $stateParams, booksService) {
  if($stateParams.pattern) {
    booksService.search($stateParams.pattern).then(function(books) {
        $scope.books = books
    })
  }

  $scope.add = function(isbn) {
    console.log("Add " + isbn);
    booksService.add(isbn);
  }
}]);
