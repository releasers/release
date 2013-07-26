app.factory("Books", ["Restangular", function (Restangular) {
  function search(pattern) {
    return [{
      isbn: "1234",
      titre: "Bilbo le Hobbit",
      resume: "Super Saga",
      imageUrl: "http://static.decitre.fr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/9/7/8/2/0/1/2/0/9782012035614FS.gif"
    }, {
      isbn: "1234",
      titre: "Bilbo le Hobbit",
      resume: "Super Saga",
      imageUrl: "http://static.decitre.fr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/9/7/8/2/0/1/2/0/9782012035614FS.gif"
    }, {
      isbn: "1234",
      titre: "Bilbo le Hobbit",
      resume: "Super Saga",
      imageUrl: "http://static.decitre.fr/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/9/7/8/2/0/1/2/0/9782012035614FS.gif"
    }]
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
