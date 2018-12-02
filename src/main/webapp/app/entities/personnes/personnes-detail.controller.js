(function() {
    'use strict';

    angular
        .module('jasperTestApp')
        .controller('PersonnesDetailController', PersonnesDetailController);

    PersonnesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Personnes'];

    function PersonnesDetailController($scope, $rootScope, $stateParams, previousState, entity, Personnes) {
        var vm = this;

        vm.personnes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jasperTestApp:personnesUpdate', function(event, result) {
            vm.personnes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
