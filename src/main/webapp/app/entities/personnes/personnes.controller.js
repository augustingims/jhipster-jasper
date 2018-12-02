(function() {
    'use strict';

    angular
        .module('jasperTestApp')
        .controller('PersonnesController', PersonnesController);

    PersonnesController.$inject = ['$state', 'Personnes', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$http'];

    function PersonnesController($state, Personnes, ParseLinks, AlertService, paginationConstants, pagingParams,$http) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.listePersonnes = listePersonnes;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll () {
            Personnes.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.personnes = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function  listePersonnes () {

            $http.get('/api/reporting/listePersonnes').success(
                function(response) {

                    vm.reportingPrinter = response;

                    vm.url = "downloadDocs/"
                        + vm.reportingPrinter.directorie;

                    window.open(vm.url, '_blank');

                }).error(function(reason) {
                    console.log(reason);

                });


        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
