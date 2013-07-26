app.factory("Users", ["Restangular", function (Restangular) {

  function list () {

  }

  function findById(userId) {
    return Restangular.one("users", userId).get();
  }

  function listBooks(userId) {
    return Restangular.one("users", userId).all("books").getList();
  }

  return {
    list: list,
    findById: findById,
    listBooks: listBooks
  };

}]);
