(function() {
    'use strict';
    angular
        .module('jasperTestApp')
        .factory('Personnes', Personnes);

    Personnes.$inject = ['$resource'];

    function Personnes ($resource) {
        var resourceUrl =  'api/personnes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
