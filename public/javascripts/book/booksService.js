app.factory("Books", ["Restangular", function (Restangular) {
  function search(pattern) {
    return Restangular.one("test/openlibrary", "ISBN:" + pattern).doGET()
  }

  function list () {

  }

  function findById (id) {

  }

  return {
    list: list,
    findById: findById,
    search: search
  }
}]);
