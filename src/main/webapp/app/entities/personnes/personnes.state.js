(function() {
    'use strict';

    angular
        .module('jasperTestApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('personnes', {
            parent: 'entity',
            url: '/personnes?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Personnes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personnes/personnes.html',
                    controller: 'PersonnesController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('personnes-detail', {
            parent: 'personnes',
            url: '/personnes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Personnes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personnes/personnes-detail.html',
                    controller: 'PersonnesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Personnes', function($stateParams, Personnes) {
                    return Personnes.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'personnes',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('personnes-detail.edit', {
            parent: 'personnes-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnes/personnes-dialog.html',
                    controller: 'PersonnesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Personnes', function(Personnes) {
                            return Personnes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personnes.new', {
            parent: 'personnes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnes/personnes-dialog.html',
                    controller: 'PersonnesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                noms: null,
                                prenoms: null,
                                sexe: null,
                                telephone: null,
                                pays: null,
                                nationalite: null,
                                dateNaissance: null,
                                lieuNaissance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('personnes', null, { reload: 'personnes' });
                }, function() {
                    $state.go('personnes');
                });
            }]
        })
        .state('personnes.edit', {
            parent: 'personnes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnes/personnes-dialog.html',
                    controller: 'PersonnesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Personnes', function(Personnes) {
                            return Personnes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personnes', null, { reload: 'personnes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personnes.delete', {
            parent: 'personnes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personnes/personnes-delete-dialog.html',
                    controller: 'PersonnesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Personnes', function(Personnes) {
                            return Personnes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('personnes', null, { reload: 'personnes' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
