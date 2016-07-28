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
define(['./module'], function (controllers) {
    'use strict';
    controllers.controller('ViewRuleCtrl', ['$filter', '$scope', '$http', '$config', '$location', 'toaster', '$timeout', '$routeParams', '$barkChart', '$route',  function ($filter, $scope, $http, $config, $location, toaster, $timeout, $routeParams, $barkChart, $route) {

	  	var getModelUrl = $config.uri.getModel+"/"+$routeParams.modelname;
		  $http.get(getModelUrl).success(function(data){
			  $scope.ruleData = data;
		  }).error(function(data){
        // errorMessage(0, 'Save model failed, please try again!');
        toaster.pop('error', data.message);
      });

      $scope.anTypes = ['', 'History Trend Detection', 'Bollinger Bands Detection', 'Deviation Detection'];

      var url= $config.uri.rulemetric+"/"+$routeParams.modelname;

      $http.get(url).success(function(res){
					$scope.modelresultData = res;
  				var chartData = res.details;

          if (chartData) {
            if ($('#viewrule-chart').position()) {
              var height = $('#viewrule-definition').height() - $('#viewrule-chart').position().top;
              var heightChart = $('#viewrule-definition').height() + 10 - $('#viewrule-chart').position().top;
            }
            $scope.metric = $barkChart.genConfigSide(res,{options:{chart:{ height: heightChart?heightChart:170}}});
          }

      });

      $scope.$on('$viewContentLoaded', function(){
        resizeWindow();
        $(window).resize(function(){
          resizeWindow();
        });
      });



      $scope.confirmDeploy = function() {
            if($scope.ruleData.basic.owner != null && $scope.ruleData.basic.owner == $scope.ntAccount){
              var enableModel = $config.uri.enableModel + "/" + $scope.ruleData.basic.name;
              $http.get(enableModel).success(function(data) {
                $scope.ruleData.basic.status = 2;
              });
            }

        };

      function resizeWindow(){
        if ($route.current.$$route.controller == "ViewRuleCtrl") {
          $timeout(function(){

            var h1 = $('#viewruleDefinition').height();
            var h2 = $('#viewTestResult').height();
            var height = Math.max(h1, h2);

            $('#viewruleDefinition').height(height);
            $('#viewTestResult').height(height);

            $('#viewruleContent').height($(window).innerHeight() - $('#viewruleContent').offset().top - $('#footerwrap').outerHeight());
          });
        }

      }
    }]);
});
