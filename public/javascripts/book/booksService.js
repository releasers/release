app.factory("Books", ["Restangular", function (Restangular) {
  function search(pattern) {
    return Restangular.one("test/openlibrary/seach", pattern).doGET();
  }

  function add(isbn) {
    return Restangular.all("users").doPOST("book", {}, {}, {isbn: isbn, number: 1} );
  }

  function list () {

  }

  function findById (id) {
    return Restangular.one("books", id).get();

  }

  return {
    list: list,
    findById: findById,
    search: search,
    add: add
  }
}]);
