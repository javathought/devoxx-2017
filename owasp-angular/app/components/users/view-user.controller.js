angular.module('devoxxApp.controllers').controller('ViewUserController', ['$scope', '$http', '$log', '$stateParams', '$state', 'Users',
      function ($scope, $http, $log, $stateParams ,$state, Users) {


        // $scope.users = User.query();

         $scope.state = "new";

            // Check if we have a ressource
         	if ($stateParams.id) {
         		$scope.state = "update";
         		$scope.user = Users.get({id: $stateParams.id});
         	} else {
            $scope.user = {name: ""}
          }
         	$scope.getBtnLabel = function() {
         		return ($scope.state == "new") ? "Créer" : "Modifier";
         	};
         	$scope.saveUser = function() {
         		if ($scope.state === "new") {
                    Users.save($scope.user,
                      function(success) {
                         $scope.alerts.push({type: 'success', msg: 'Utilisateur ' + success.name + ' enregistré'});
                         $state.go('users');
                      },
                      function(error) {
                          $scope.alerts.push({type: 'danger', msg: 'Impossible d\'enregistrer l\'utilisateur ' + '  : ' + error.status + "-" + error.statusText, details: error.data});
                       });
         		} else {
         	        Users.update($scope.user,
                      function(success) {
                         $scope.alerts.push({type: 'success', msg: 'Utilisateur ' + success.name + ' modifié'});
                         $state.go('users');
                      },
                      function(error) {
                         $scope.alerts.push({type: 'danger', msg: 'Impossible d\'enregistrer les modifications de l\'utilisateur '  +' : ' + error.status + "-" + error.statusText, details: error.data});
                      });
         		}

         	}

    }]);

    angular.module('devoxxApp.controllers').controller('ViewProfilController', ['$scope', '$http', '$log', '$stateParams', '$state', 'Users',
          function ($scope, $http, $log, $stateParams ,$state, Users) {


              // Check if we have a ressource
           		$scope.user = Users.current();  //({id: $stateParams.id});
              $scope.getBtnLabel = "Modifier";

             	$scope.saveUser = function() {
         	        Users.saveCurrent($scope.user,
                      function(success) {
                         $scope.alerts.push({type: 'success', msg: 'Utilisateur ' + success.name + ' modifié'});
                         $state.go('users');
                      },
                      function(error) {
                         $scope.alerts.push({type: 'danger', msg: 'Impossible d\'enregistrer les modifications de l\'utilisateur '  +' : ' + error.status + "-" + error.statusText, details: error.data});
                      });
             	}

        }]);
