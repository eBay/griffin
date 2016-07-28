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
    controllers.controller('HealthCtrl', ['$scope', '$http', '$config', '$location','$timeout', '$route', function ($scope, $http, $config, $location, $timeout, $route) {
      console.log('health controller');
      // var url="/js/controllers/heatmap.json";
      var url = $config.uri.heatmap;


    //treemap reference

    var data=[];
    //https://websafecolors.info/color-chart
    var colors = ["#006699", "#009933", "#00ff66", "#336600", "#33cc00", "#669966", "#ccffcc", "#6666ff"];
    $http.get(url).success(function(res){
  		console.log(res);
      var sysId = 0;
      var metricId = 0;
      /*res.forEach(function(sys){
        data.push({
          id: 'id_' + sysId,
          name: sys.name,// + '(' + sys.dq + '%)',
          sysName: sys.name,
          // value: sys.dq ,
          // value:1,
          // color: sys.dq<90?'#cc6633':'#006633'
            // color: sys.dq<90?'#ff0000':colors[sysId]
          color: '#000033'
        });


        sys.metrics.forEach(function(metric){
          data.push({
            id: 'id_' + sysId + '_' + metricId,
            name: metric.name + '(' + metric.dq.toFixed(2) + '%)',
            // value: metric.dq,
            value: 1,
            parent: 'id_' + sysId,
            color: metric.dq<90?'#cc6633':'#006633'
            // color: metric.dq<90?'#ff0000':colors[sysId]
          });
            metricId++;
        });

        sysId ++;
      });*/
      angular.forEach(res,function(sys,key){
        console.log(sys)
        data.push({
          id: 'id_' + sysId,
          name: sys.name,// + '(' + sys.dq + '%)',
          sysName: sys.name,
          // value: sys.dq ,
          // value:1,
          // color: sys.dq<90?'#cc6633':'#006633'
            // color: sys.dq<90?'#ff0000':colors[sysId]
          color: '#000033'
        });

        angular.forEach(sys.metrics,function(metric,key){
          data.push({
            id: 'id_' + sysId + '_' + metricId,
            name: metric.name,// + '(' + metric.dq + '%)',
            // value: metric.dq,
            value: 1,
            parent: 'id_' + sysId,
            color: metric.dqfail>0?'#cc6633':'#006633'
            // color: metric.dq<90?'#ff0000':colors[sysId]
          });
            metricId++;
        })
        sysId ++;
      })

      $scope.chartConfig ={

        options: {
             chart: {
                 type: 'treemap',
                //  height: 800,
                //  height: $(window).innerHeight() - $('.bs-component').offset().top - $('.bs-component').outerHeight() - $('#footerwrap').outerHeight(),
                 backgroundColor: null,
                 plotBackgroundColor: 'none'//,
             },

             tooltip: {
                enabled: false
            },
         },
        series: [{
            layoutAlgorithm: 'squarified',
            allowDrillToNode: true,
            dataLabels: {
                enabled: false,
                allowOverlap: true
            },
            levelIsConstant: false,
            levels: [{
                level: 1,
                dataLabels: {
                    enabled: true,
                    align: 'left',
                    verticalAlign: 'top',
                    style: {
                        fontSize: '26px',
                        fontWeight: 'bold',
                        color: 'black',
                        fontFamily: 'Arial'
                    }
                },
                borderWidth: 5,
                borderColor: '#000000'
            },
            {
                level: 2,
                dataLabels: {
                    useHTML: true,
                    enabled: true,
                    // maxStaggerLines: 1,
                    rotation: -45,
                    style: {
                      // rotation: -45,
                        fontSize: '16px',
                        // wordWrap: 'break-word'
                    },
                    formatter:function(){
                      // console.log(this.key + ': ' + JSON.stringify(this.point.shapeArgs));
                      return this.key;
                    }
                }
            }
          ],
          events:{
            click: function(e){
              // console.log(e);
              // console.log('system id is: ' + e.target.point.id);
              $location.path('/metrics/' + e.target.point.sysName);
              $scope.$apply();
              return false;
            }
          },
            data: data
        }],
        // subtitle: {
        //     text: 'Click points to drill down. Source: <a href="http://apps.who.int/gho/data/node.main.12?lang=en">WHO</a>.'
        // },
        title: {
            text: 'Data Quality Metrics Heatmap',
            style:{
              color: '#ffffff'
            }
        }
      };
  	});

    $scope.$on('$viewContentLoaded', function(){

      resizeWindow();

      $(window).resize(function(){
        resizeWindow();
      });
      // $('.formStep').css({height: 800});
    });

    function resizeWindow(){
      if($route.current.$$route.controller == "HealthCtrl"){
        $timeout(function() {
          $('#chart1').height( $(window).innerHeight() - $('.bs-component').offset().top - $('.bs-component').outerHeight() - $('#footerwrap').outerHeight());

        }, 10);
        // $('.formula-text-mid').css({'padding-top': (($('.formula-text-up').height() - $('.formula-text-mid').height()) + $('.formula-text-mid').height()/2) + 'px'});

      }
    }



    }]);
});
