app.controller("UsersCtrl", ["$scope", function ($scope) {

    $scope.users = [
        { _id : "51f23e903dfb9380006ce169", profile : 
            { id : "107176102057999375218", email : "bor@zenexity.com", verifiedEmail : "true", 
              name : "Benoit Orihuela", link : "https://plus.google.com/107176102057999375218", 
              gender : "male", birthday : "1976-01-23", locale : "fr"}},
        { _id : "002", profile : 
            { id : "002", email : "aaa@zenexity.com", verifiedEmail : "true", 
              name : "Aaaaaa Dijou", link : "https://plus.google.com/002", 
              gender : "male", birthday : "1986-01-23", locale : "fr"}}
    ]

    $scope.orderProp = 'profile.name';
}]);