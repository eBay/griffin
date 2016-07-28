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
    controllers.controller('DataAssetsCtrl', ['$scope', '$http', '$config', '$location', 'toaster', '$timeout', '$route',  function ($scope, $http, $config, $location, toaster, $timeout, $route) {
	    
      var allModels = $config.uri.dataassetlist;

      $scope.paging = function(tableState){
        console.log(tableState);
        // tableState.pagination.numberOfPages = $scope.rowCollection.length/10 + 1;
        var start = tableState.pagination.start || 0;
        var number = tableState.pagination.number || 10;

        if(start == 0 && !$scope.rowCollection){
          $http.get(allModels).success(function(data) {
            $scope.rowCollection = data;
            $scope.rowCollection.sort(function(a,b){
              return (a.assetName<b.assetName?-1:(a.assetName>b.assetName?1:0));
            });

            $scope.displayed = $scope.rowCollection.slice(start, start+number);
            tableState.pagination.numberOfPages = Math.ceil($scope.rowCollection.length/number);
          });
        }else{
          $scope.displayed = $scope.rowCollection.slice(start, start+number);
        }
      }


		$scope.remove = function(row) {
			$scope.selectedRow = row;
			$('#confirm-delete').modal('show');

		};

		$scope.sendDeleteRequest = function() {
			$http.delete($config.uri.deletedataasset+'/'+$scope.selectedRow._id).success(function(data){
				if(data.result == 'success'){
					$('#confirm-delete').modal('hide');
          var index = $scope.rowCollection.indexOf($scope.selectedRow);
          $scope.rowCollection.splice(index, 1);

          index = $scope.displayed.indexOf($scope.selectedRow);
          $scope.displayed.splice(index, 1);

				}else{
					console.log(data);
				}
			})

		}

    $scope.$on('$viewContentLoaded', function() {
        resizeWindow();
        $(window).resize(function() {
            resizeWindow();
        });

    });


    function resizeWindow() {
        if ($route.current.$$route.controller == "DataAssetsCtrl") {
            $timeout(function() {

                $('#assetContainer').css({
                    height: $(window).innerHeight() - $('#assetContainer').offset().top - $('#footerwrap').outerHeight()
                });

            }, 0);
        }
    }
  }]);


});
