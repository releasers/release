app.controller("UserCtrl", ["$scope", function ($scope) {
  
    $scope.user = 
        { _id : "51f23e903dfb9380006ce169", profile : 
            { id : "107176102057999375218", email : "bor@zenexity.com", verifiedEmail : "true", 
              name : "Benoit Orihuela", link : "https://plus.google.com/107176102057999375218", 
              gender : "male", birthday : "1976-01-23", locale : "fr",
              picture : "https://lh6.googleusercontent.com/-RPheNCyuzhU/AAAAAAAAAAI/AAAAAAAAATA/P34dWR25TRo/photo.jpg"},
          books : [ 
            { isbn: "9781934356319", addedDate: "2013-01-01T00:00:00.000", borrowerId: "", borrowedSince: ""},
            { isbn: "9780981531601", addedDate: "2012-01-01T00:00:00.000", borrowerId: "107176102057999375218", borrowedSince: "2012-05-10T09:00:00.000"}
          ],
          suggestions : [
            { isbn: "9781934356319", addedDate: "2013-01-01T00:00:00.000", approved : 
                [ { who: "51f23e903dfb9380006ce169", when: "2012-01-20T00:00:00.000"}, 
                  { who: "51f23e903dfb9380006ce169", when: "2012-01-21T00:00:00.000"}
                ],
              disapproved: 
                [
                  { who: "51f23e903dfb9380006ce169", when: "2012-01-19T00:00:00.000"}
                ]
            }
          ]
      }
}]);