/*-
 * Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

define(['./module'], function (controllers) {
    'use strict';
    controllers.controller('DetailCtrl', ['$scope', '$http', '$config', '$location','$timeout', '$route', '$barkChart', '$rootScope','$routeParams', function ($scope, $http, $config, $location, $timeout, $route, $barkChart, $rootScope,$routeParams) {
      console.log('detail controller');
      // var url="/js/controllers/heatmap.json";

        var echarts = require('echarts');
        var formatUtil = echarts.format;

        var showBig = function(metricName){
          var metricDetailUrl = $config.uri.metricdetail + '/' + metricName;
          $http.get(metricDetailUrl).success(function (data){
            $rootScope.showBigChart($barkChart.getOptionBig(data));
          });
        }

        pageInit();

        function pageInit() {
            $scope.$emit('initReq');
            showBig($routeParams.modelname);
        }

        $scope.$on('resizeHandler', function(e) {
            if($route.current.$$route.controller == 'HealthCtrl'){
                console.log('health resize');
                resizeTreeMap();
                $scope.myChart.resize();
            }
        });

        function resizeTreeMap() {
            $('#chart1').height( $('#mainWindow').height() - $('.bs-component').outerHeight() );
        }

    }]);
});
