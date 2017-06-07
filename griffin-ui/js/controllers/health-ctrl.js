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
    controllers.controller('HealthCtrl', ['$scope', '$http', '$config', '$location', '$timeout', '$route', '$barkChart', '$rootScope', function($scope, $http, $config, $location, $timeout, $route, $barkChart, $rootScope) {
        console.log('health controller');
        // var url="/js/controllers/heatmap.json";

        var echarts = require('echarts');
        var formatUtil = echarts.format;

        pageInit();

        function pageInit() {
            $scope.$emit('initReq');
            var url = $config.uri.heatmap;
            $http.get(url).success(function(res) {
                //sort by priority
                angular.forEach(res, function(sys) {
                    if (sys.metrics && sys.metrics.length > 0) {
                        sys.metrics.sort(function(a, b) {
                            // If compareFunction(a, b) is less than 0, sort a to a lower index than b.
                            var important_a = $config.job_preorderset["job" + a.name],
                                important_b = $config.job_preorderset["job" + b.name],
                                ret;
                            if (important_a && !important_b) {
                                ret = -1;
                            } else if (important_b && !important_a) {
                                ret = 1;
                            } else if (important_a && important_b) {
                                ret = important_a - important_b;
                            } else {
                                ret = 0;
                            } // keep original order if neither a or b is important

                            return (ret);
                        });
                    }
                });
                //set dq to be rollup- the latest item value

                var url_rollupmetrics = $config.uri.rollupmetric; //use the rollup 
                var dic = {};
                $http.get(url_rollupmetrics).success(function(res_rollup) {
                    angular.forEach(res_rollup, function(sys_rollup) {
                        if (sys_rollup.metrics && sys_rollup.metrics.length > 0) {
                            sys_rollup.metrics.forEach(function(tmp) {
                                dic[tmp.name] = { 'dq': tmp.details[0].value, 'dqfail': tmp.details[0].value > 95 ? 0 : 1, 'timestamp': tmp.details[0].timestamp };
                            });
                        }
                    });

                    if (Object.keys(dic).length > 0) {
                        if (res[0].metrics && res[0].metrics.length > 0) {
                            res[0].metrics.forEach(function(theArray) {
                                if (theArray.name in dic) {
                                    theArray.dq = dic[theArray.name].dq;
                                    theArray.dqfail = dic[theArray.name].dqfail;
                                    theArray.timestamp = dic[theArray.name].timestamp;
                                }
                            });
                        }
                    }

                    renderTreeMap(res);

                }).error(function(err) {
                    console.log(err);
                });
            });
        }

        function renderTreeMap(res) {


            function parseData(data) {
                var sysId = 0;
                var metricId = 0;
                var result = [];
                angular.forEach(res, function(sys, key) {
                    console.log(sys);

                    var item = {};
                    item.id = 'id_' + sysId;
                    item.name = sys.name;

                    if (sys.metrics != undefined) {
                        item.children = [];
                        angular.forEach(sys.metrics, function(metric, key) {
                            var itemChild = {
                                id: 'id_' + sysId + '_' + metricId,
                                name: metric.name, // + '(' + metric.dq + '%)',
                                // value: metric.dq,
                                value: 1,
                                dq: metric.dq,
                                sysName: sys.name,

                                itemStyle: {
                                    normal: {
                                        color: '#4c8c6f',
                                        textStyle: {
                                            fontsize: 32
                                        }
                                    }
                                }
                            };
                            if (metric.dqfail == 1) {
                                itemChild.itemStyle.normal.color = '#ae5732';
                            } else {
                                itemChild.itemStyle.normal.color = '#005732';
                            }
                            item.children.push(itemChild);
                            metricId++;
                        });
                    }

                    result.push(item);

                    sysId++;
                });
                return result;
            }

            var data = parseData(res);
            console.log(data);

            function getLevelOption() {
                return [{
                        itemStyle: {
                            normal: {
                                borderWidth: 0,
                                gapWidth: 6,
                                borderColor: '#000'
                            }
                        }
                    },

                    {
                        itemStyle: {
                            normal: {
                                gapWidth: 1,
                                borderColor: '#fff'
                            }
                        }
                    }
                ];
            }

            var option = {

                title: {
                    text: 'Data Quality Metrics Heatmap',
                    left: 'center'
                },

                backgroundColor: 'transparent',

                tooltip: {
                    formatter: function(info) {
                        var dqFormat = info.data.dq > 100 ? '' : '%';
                        return [
                            '<span style="font-size:1.8em;">' + formatUtil.encodeHTML(info.data.sysName) + ' &gt; </span>',
                            '<span style="font-size:1.5em;">' + formatUtil.encodeHTML(info.data.name) + '</span><br>',
                            '<span style="font-size:1.5em;">dq : ' + info.data.dq.toFixed(2) + dqFormat + '</span>'
                        ].join('');
                    }
                },

                series: [{
                    name: 'System',
                    type: 'treemap',
                    itemStyle: {
                        normal: {
                            borderColor: '#fff'
                        }
                    },
                    levels: getLevelOption(),
                    breadcrumb: {
                        show: false
                    },
                    roam: false,
                    nodeClick: false,
                    data: data,
                    // leafDepth: 1,
                    width: '95%',
                    bottom: 0
                }]
            };

            resizeTreeMap();
            $scope.myChart = echarts.init(document.getElementById('chart1'), 'dark');
            $scope.myChart.setOption(option);

            $scope.myChart.on('click', function(param) {
                // if (param.data.sysName) {
                //     $location.path('/metrics/' + param.data.sysName);
                //     $scope.$apply();
                //     return false;
                // }
                // param.event.event.preventDefault();
                if (param.data.name) {

                    showBig(param.data.name);
                    // return false;
                }
            });

        }

        var showBig = function(metricName) {
            var metricDetailUrl = $config.uri.metricdetail + '/' + metricName;
            $http.get(metricDetailUrl).success(function(data) {
                $rootScope.showBigChart($barkChart.getOptionBig(data));
            });
        }

        $scope.$on('resizeHandler', function(e) {
            if ($route.current.$$route.controller == 'HealthCtrl') {
                console.log('health resize');
                resizeTreeMap();
                $scope.myChart.resize();
            }
        });

        function resizeTreeMap() {
            $('#chart1').height($('#mainWindow').height() - $('.bs-component').outerHeight());
        }

    }]);
});