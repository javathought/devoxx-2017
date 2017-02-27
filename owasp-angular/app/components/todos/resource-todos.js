angular.module('devoxxApp.services').factory('Todos', function ($resource, Common) {

  var API_URI = Common.root_api + '/todos/:id';

  return $resource(API_URI , {id: '@uuid'}, {
    update: {method: 'PUT'},
    fromUser:   {method: 'GET', url: Common.root_api +'/users/current/todos', isArray: true}
  });

});
