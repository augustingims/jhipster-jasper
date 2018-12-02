(function() {
    'use strict';

    angular
        .module('jasperTestApp')
        .controller('PersonnesDeleteController',PersonnesDeleteController);

    PersonnesDeleteController.$inject = ['$uibModalInstance', 'entity', 'Personnes'];

    function PersonnesDeleteController($uibModalInstance, entity, Personnes) {
        var vm = this;

        vm.personnes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Personnes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
