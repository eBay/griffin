/*
	Copyright (c) 2016 eBay Software Foundation.
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
define(['./module'], function(controllers) {
    'use strict';
    controllers.controller('MetricsCtrl', ['$scope', '$http', '$config', '$location', '$routeParams', '$timeout', '$compile', '$route', '$barkChart', function($scope, $http, $config, $location, $routeParams, $timeout, $compile, $route, $barkChart) {
        console.log('Route parameter: ' + $routeParams.sysName);
        var url_dashboard = $config.uri.dashboard + ($routeParams.sysName?('/'+$routeParams.sysName):'');

        var redraw = function(data) {
            angular.forEach(data, function(sys) {
                angular.forEach(sys.metrics, function(metric, index) {
                    metric.chartId = $barkChart.genConfigThum(metric, {options:{chart:{width: ($(window).innerWidth() - $('#rightbar').outerWidth())/4.2,height: ($(window).innerWidth() - $('#rightbar').outerWidth())/5.6}}});

                });
            });
        };

        $scope.assetOptions = [];

        $http.get(url_dashboard, {cache:true}).success(function(res) {
            $scope.dashboard = res;
            angular.forEach(res, function(sys) {
              angular.forEach(sys.metrics, function(metric) {
                var chartData = metric.details;
                chartData.sort(function(a, b){
                  return a.timestamp - b.timestamp;
                });

              });
            });
            $scope.originalData = angular.copy(res);
            redraw($scope.dashboard);
        });

        $scope.changeOrg = function() {
          $scope.selectedAssetIndex = undefined;
          $scope.assetOptions = [];
          $scope.dashboard = [];
          if($scope.selectedOrgIndex == ""){
            $scope.dashboard = angular.copy($scope.originalData);
          } else {
            var org = angular.copy($scope.originalData[$scope.selectedOrgIndex]);
            $scope.dashboard.push(org);
            angular.forEach(org.metrics, function(metric, index) {
              if($scope.assetOptions.indexOf(metric.assetName) == -1) {
                $scope.assetOptions.push(metric.assetName);
              }
            });
          }
          redraw($scope.dashboard);
        };

        $scope.changeAsset = function() {
          $scope.dashboard = [];
          if($scope.selectedOrgIndex == ""){
            $scope.dashboard = angular.copy($scope.originalData);
          } else {
            var org = angular.copy($scope.originalData[$scope.selectedOrgIndex]);
            $scope.dashboard.push(org);
          }
          if($scope.selectedAssetIndex != undefined && $scope.selectedAssetIndex != ''){
            var asset = $scope.assetOptions[$scope.selectedAssetIndex];
            angular.forEach($scope.dashboard, function(sys) {
              var oldMetrics = sys.metrics;
              sys.metrics = [];
              angular.forEach(oldMetrics, function(metric, index) {
                if(metric.assetName == asset) {
                  sys.metrics.push(metric);
                }
              });
            });
          }
          redraw($scope.dashboard);
        };

        $scope.$on('$viewContentLoaded', function() {
            resizedash();
            $(window).resize(function() {
                resizedash();
            });
        });

        function resizedash() {
            if ($route.current.$$route.controller == "MetricsCtrl") {
              $timeout(function() {
                  $('#dashboard').css({
                      height: $(window).innerHeight() - $('#dashboard').offset().top - $('#footerwrap').outerHeight()
                  });
                  if($scope.dashboard){
                    redraw($scope.dashboard);
                  }
              }, 0);
            }
        };

        /*click the chart to be bigger*/
        $scope.showBig = function(t){
          var metricDetailUrl = $config.uri.metricdetail + '/' + t.name;
          // var metricDetailUrl = '/js/mock_data/anom.json';
          $http.get(metricDetailUrl).success(function (data){
            var newDirective = angular.element('<big-chart/>');
            angular.element('body').append(newDirective);
            $compile(newDirective)($scope);

            var config = $barkChart.genConfigBig(data);

            $scope.chartConfig = config;

          });

        };

        $scope.getSample = function(item) {
          $scope.selectedModelName = item.name;
          var sampleUrl = $config.uri.metricsample + '/' + item.name;
          $http.get(sampleUrl).success(function(data){
            $scope.sample = data;
            $('#download-sample').modal('show');
            $('#viewsample-content').css({
                'max-height': $(window).innerHeight() - $('.modal-content').offset().top - $('.modal-header').outerHeight() - $('.modal-footer').outerHeight() - 250
            });

          });
        };

        $scope.downloadUrl = $config.uri.metricdownload;

    }
    ]);
});
