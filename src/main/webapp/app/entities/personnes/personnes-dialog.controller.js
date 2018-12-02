(function() {
    'use strict';

    angular
        .module('jasperTestApp')
        .controller('PersonnesDialogController', PersonnesDialogController);

    PersonnesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Personnes'];

    function PersonnesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Personnes) {
        var vm = this;

        vm.personnes = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.personnes.id !== null) {
                Personnes.update(vm.personnes, onSaveSuccess, onSaveError);
            } else {
                Personnes.save(vm.personnes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jasperTestApp:personnesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
