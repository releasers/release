app.controller("BooksCtrl", ["$scope", "$location", "$stateParams", "Books", function ($scope, $location, $stateParams, booksService) {
  if($stateParams.pattern) {
    booksService.search($stateParams.pattern).then(function(book) {
      console.log(book);
        $scope.books =  [{
          isbn: $stateParams.pattern,
          titre: book.title,
          resume: "",
          imageUrl: book.cover.medium,
          authors: book.authors.map(function(a) { return a.name })
        }]
    })
  }

  $scope.add = function(isbn) {
    alert(isbn)
  }
}]);
