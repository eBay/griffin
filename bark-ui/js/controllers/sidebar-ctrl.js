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
    controllers.controller('SideBarCtrl', ['$scope', '$http', '$config', '$filter', '$timeout', '$compile', '$routeParams', '$barkChart', function($scope, $http, $config, $filter, $timeout, $compile, $routeParams, $barkChart) {
        var url = $config.uri.statistics;


        $http.get(url).success(function(res) {
            $scope.datasets = res.assets;
            $scope.metrics = res.metrics;
            $scope.pieConfig = $barkChart.genConfigPie(res.status);

        });

        sideBarList();

        $scope.$watch(function(){return $routeParams.sysName;}, function(value){
          console.log('Watched value: ' + value);
          if(value){
            sideBarList(value);
          }else{
            $scope.briefmetrics = $scope.backup_metrics;
          }
        });

        $scope.draw = function(metric, parentIndex, index) {
            var chartDivId = $scope.briefmetrics[parentIndex].name + index;
            if(angular.element('#' + chartDivId).children().length == 0){

            // small chart
              metric.chartId = $barkChart.genConfigSide(metric, {options:{chart:{width: $('.panel-heading').innerWidth() - 20, height: 150}}});

              var newDirective = angular.element('<div class="panel-body" style="cursor:pointer;">chart content</div>');
              angular.element('#' + chartDivId).append(newDirective);
              $compile(newDirective)($scope);
          }
        };

        $scope.showBig = function(metric){
          var metricDetailUrl = $config.uri.metricdetail + '/' + metric.name;
          $http.get(metricDetailUrl).success(function (data){
            var newDirective = angular.element('<big-chart/>');
            angular.element('body').append(newDirective);
            $compile(newDirective)($scope);

            $scope.chartConfig = $barkChart.genConfigBig(data);

          });
        }

        function sideBarList(sysName){
          var url_briefmetrics = $config.uri.briefmetrics + (sysName?('/'+ sysName):'');
          $http.get(url_briefmetrics, {cache:true}).success(function(res) {
              $scope.briefmetrics = res;

              angular.forEach(res, function(sys) {
                if(sys.metrics && sys.metrics.length > 0){
                  sys.metrics.sort(function(a, b){
                    if(a.dqfail == b.dqfail){ //If it's green, sort by timestamp
                      return b.timestamp - a.timestamp;
                    }else{  //sort by dq
                      return -(a.dqfail - b.dqfail);
                    }
                  });
                }
              });

              if(!sysName){
                $scope.backup_metrics = angular.copy(res);
              }
          });
        }


        resizeSidebar();

        $(window).resize(function() {
            resizeSidebar();
        });

        function resizeSidebar() {
            $timeout(function() {

                if($('#side-bar-metrics').offset()){//if the element is rendered
                  $('#side-bar-metrics').css({
                      height: $(window).innerHeight() - $('#side-bar-metrics').offset().top
                  });

                  //redraw statistics chart
                  if($scope.pieConfig){
                    $scope.pieConfig.options.chart.width = $('#side-bar-stats').width() * 7 /12;
                  }
                  //need to redraw the charts
                  angular.forEach($scope.briefmetrics, function(sys){
                    angular.forEach(sys.metrics, function(metric){
                      if(metric.chartId){
                        metric.chartId.options.chart.width = $('.panel-heading').innerWidth() - 20;
                      }
                    });

                  });
                }else{
                  resizeSidebar();
                }
            }, 0);
        }
    }
    ]);
});
