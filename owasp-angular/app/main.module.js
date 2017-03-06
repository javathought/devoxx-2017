//
angular.module('Authentication', []);

angular.
module('devoxxApp', [
  'ui.bootstrap',
  'ui.router',
  'ngResource',
  'ngMessages',
  'devoxxApp.controllers',
  'devoxxApp.directives',
  'devoxxApp.services',
  'Authentication',
  'ngCookies',
  'ui.validate',
  'ngSanitize'
])
 
.config( function ($stateProvider,$urlRouterProvider,$locationProvider,
  $rootScopeProvider, $httpProvider) {

    $httpProvider.defaults.headers.common['X-Requested-By'] = 'XMLHttpRequest';

    $stateProvider
          .state('home', {
            url         : '/',
            templateUrl : 'app/components/home/home.html',
            controller  : 'HomeController'
          })
          .state('login', {
            url         : '/login',
            templateUrl : 'app/components/login/login.html',
            controller  : 'LoginController'
          })
          .state('todos', {
            url         : '/todos',
            templateUrl : 'app/components/todos/list-todos.html',
            controller  : 'ListTodosController'
          })
					.state('todo', {
            url         : '/todos/:id',
            templateUrl : 'app/components/todos/view-todo.html',
            controller  : 'ViewTodoController'
          })
          .state('users', {
            url         : '/users',
            templateUrl : 'app/components/users/list-users.html',
            controller  : 'ListUsersController',
					})
          .state('user', {
            url         : '/users/:id',
            templateUrl : 'app/components/users/view-user.html',
            controller  : 'ViewUserController'
          })
          .state('profil', {
            url         : '/profil',
            templateUrl : 'app/components/users/view-user.html',
            controller  : 'ViewProfilController'
          });
    $urlRouterProvider.otherwise('/');
		// $locationProvider.html5Mode(true);

})
  
.run(['$rootScope', '$location', '$cookieStore', '$http', 'AuthenticationService',
    function ($rootScope, $location, $cookieStore, $http, AuthenticationService) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.authdata; // jshint ignore:line
						AuthenticationService.CheckRoles($rootScope.globals.currentUser.roles);
        };

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }




        });
    }]);
