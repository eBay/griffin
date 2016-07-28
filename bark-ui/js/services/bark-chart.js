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
define(['./module'], function (services) {
  services.factory('$barkChart', function(){

    return {
      genConfigBig: genConfigBig,
      genConfigThum: genConfigThum,
      genConfigSide: genConfigSide,
      genConfigPie: genConfigPie
    };
  });

  //Functions for $barkChart service
  function retrieveDataFromMetric(metric){
    var data = [];
    var chartData = metric.details;
    for(var i = 0; i < chartData.length; i++){
        var val  = chartData[i].value>100 ? chartData[i].value : parseFloat(chartData[i].value.toFixed(2));
        data.push([chartData[i].timestamp, val]);
    }

    data.sort(function(a, b){
      return a[0] - b[0];
    });
    return data;
  }

  function retrieveDataFromMetricTrend(metric){
    metric.details.sort(function(a, b){
      return a.timestamp - b.timestamp;
    });

    var newData=[], oldData=[];
    angular.forEach(metric.details, function(item){
      newData.push([item.timestamp, item.value]);
      oldData.push([item.timestamp, item.comparisionValue]);
    });

    return [newData, oldData];
  }

  function retrieveDataFromMetricBollinger(metric){
    metric.details.sort(function(a, b){
      return a.timestamp - b.timestamp;
    });

    var ranges = [], averages = [], realtime = [];
    angular.forEach(metric.details, function(item){
      ranges.push([item.timestamp, item.bolling.lower, item.bolling.upper]);
      averages.push([item.timestamp, item.bolling.mean]);
      realtime.push([item.timestamp, item.value]);

    });

    return [ranges, averages, realtime];

  }

  function retrieveDataFromMetricMad(metric){
    metric.details.sort(function(a, b){
      return a.timestamp - b.timestamp;
    });

    var ranges = [], realtime = [];
    angular.forEach(metric.details, function(item){
      ranges.push([item.timestamp, item.mad.lower, item.mad.upper]);
      realtime.push([item.timestamp, item.value]);

    });

    return [ranges, realtime];

  }


  function genConfigBig(dataAsset){
    var newConfig = {
      useHighStocks: true,
      options: {
          chart: {
              type: 'spline',
              backgroundColor: 'none',
              plotBackgroundColor: 'none',
              borderWidth: "0",
              borderColor: "white",
              spacing: [10, 15, 40, 15],
          },
          rangeSelector: {
              inputEnabled: false,
              buttons:[ {type: 'day', count: 1, text: '1d'},
                        {type: 'month', count: 1, text: '1m'},
                        {type: 'month', count: 3, text: '3m'},
                        {type: 'year', count: 1, text: '1y'},
                        {type: 'all', text: 'All'}],
              buttonTheme:{
                r: 8,
                states:{
                  select: {
                    fill: '#039',
                    style: {
                      color: 'white'
                    }
                  }
                }
              },
              selected: 1
          },
          navigator: {
              enabled: true
          },
          credits: {
              enabled: false
          },
          legend: {
              enabled: false
          },
          tooltip: {
              valueSuffix: dataAsset.dq>100?'':'%'
          }
      },
      series: [{
          name: 'dq',
          color: '#0066FF',
          data: retrieveDataFromMetric(dataAsset)
      }],
      title: {
          text: '',
      },
      subtitle: {
          useHTML: true,
          text: '<a href="/#/viewrule/' + dataAsset.name + '/">' + dataAsset.name + '</a>',
          // text: '<a href="http://www.bing.com">Link</a>',
          style: {
              fontSize: '20px',
              color: 'yellow'
          }
      },
      xAxis: {
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          type: 'datetime',
      },
      yAxis: {
          offset: 30,
          gridLineDashStyle: 'ShortDash',
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          title: {
              text: dataAsset.dq>100?'dq':'dq(%)',
              style: {
                  color: 'yellow'
              }
          }
      }
    };

    //TODO:mockup data, should determine by the type
    if(dataAsset.metricType == 'Bollinger'){
        newConfig = createBollingerBig(dataAsset);
    }else if(dataAsset.metricType == 'Trend'){
      newConfig = createTrendBig(dataAsset);
    }else if(dataAsset.metricType == 'MAD'){
      newConfig = createMadBig(dataAsset);
    }else if(dataAsset.metricType == 'Count'){//totcal count
      newConfig.tooltip.valueSuffix = undefined;
      newConfig.tooltip.pointFormat = '<span style="color:{point.color}">\u25CF</span> Total Count: <b>{point.y:,.0f}</b><br/>';
      newConfig.yAxis.title.text = 'Total Count';
    }



    return newConfig;

  }

  function genConfigThum(dataAsset, config){

    var newConfig = {
        options: {
            chart: {
                type: 'spline',
                backgroundColor: 'none',
                plotBackgroundColor: 'none',
                borderWidth: "1",
                borderColor: "white"
            },
            credits: {
                enabled: false
            },
            legend: {
                enabled: false
            },
            tooltip: {
                valueSuffix: dataAsset.dq>100?'':'%'
            }
        },
        series: [{
            name: 'dq',
            color: '#0066FF',
            marker: {enabled:false},
            data: retrieveDataFromMetric(dataAsset)
        }],
        title: {
            text: '',
        },
        subtitle: {
            text: dataAsset.name,
            style: {
                fontSize: '14px',
                color: 'yellow'
            }
        },
        xAxis: {
            labels: {
                style: {
                    color: 'yellow'
                }
            },
            type: 'datetime',
        },
        yAxis: {
            gridLineDashStyle: 'ShortDash',
            labels: {
                style: {
                    color: 'yellow'
                }
            },
            title: {
                text: dataAsset.dq>100?'dq':'dq(%)',
                style: {
                    color: 'yellow'
                }
            }
        }
    };

    if(dataAsset.metricType == 'Bollinger'){
      newConfig = createBollinger(dataAsset);
    }else if(dataAsset.metricType == 'Trend'){
      newConfig = createTrend(dataAsset);
    }else if(dataAsset.metricType == 'MAD'){
      newConfig = createMad(dataAsset);
    }else if(dataAsset.metricType == 'Count'){//totcal count
      newConfig.tooltip.valueSuffix = undefined;
      newConfig.tooltip.pointFormat = '<span style="color:{point.color}">\u25CF</span> Total Count: <b>{point.y:,.0f}</b><br/>';
      newConfig.yAxis.title.text = 'Total Count';
    }

    return mergeConfig(newConfig, config);


  }

  //The configuration for side bar
  function genConfigSide(dataAsset, config){

    var newConfig = {
        options: {
            chart: {
                type: 'spline',
                backgroundColor: null ,
                plotBackgroundColor: 'none'
            },
            credits: {
                enabled: false
            },
            legend: {
                enabled: false
            },
            tooltip: {
                valueSuffix: dataAsset.dq>100?'':'%'
            }
        },
        title: {
            text: '',
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            labels: {
                style: {
                    color: 'yellow'
                }
            },
            type: 'datetime',
        },
        yAxis: {
            gridLineDashStyle: 'ShortDash',
            labels: {
                style: {
                    color: 'yellow'
                }
            },
            title: {
                text: dataAsset.dq>100?'dq':'dq(%)',
                style: {
                    color: 'yellow'
                }
            }
        },
        series: [{
            name: 'dq',
            data: retrieveDataFromMetric(dataAsset)
        }]
    };

    if(dataAsset.metricType == 'Bollinger'){
      newConfig = createBollinger(dataAsset);
      newConfig.options.chart = {
            type: 'spline',
            backgroundColor: 'none',
            plotBackgroundColor: 'none'
      }
    }else if(dataAsset.metricType == 'Trend'){
      newConfig = createTrend(dataAsset);
      newConfig.options.chart = {
            type: 'spline',
            backgroundColor: 'none',
            plotBackgroundColor: 'none'
      }
    }else if(dataAsset.metricType == 'MAD'){
      newConfig = createMad(dataAsset);
      newConfig.options.chart = {
            type: 'spline',
            backgroundColor: 'none',
            plotBackgroundColor: 'none'
      }
    }else if(dataAsset.metricType == 'Count'){//totcal count
      newConfig.tooltip.valueSuffix = undefined;
      newConfig.tooltip.pointFormat = '<span style="color:{point.color}">\u25CF</span> Total Count: <b>{point.y:,.0f}</b><br/>';
      newConfig.yAxis.title.text = 'Total Count';
    }

    return mergeConfig(newConfig, config);


  }

  function genConfigPie(status){

    var smallarry;
    var largearry = [];
    for (var key in status) {
        smallarry = [];
        smallarry.push(key);
        smallarry.push(status[key]);

        if (key == 'health') {
            largearry.push({
                name: key,
                y: status[key],
                sliced:true
            })
        } else {
            largearry.push(smallarry);
        }
    }

    var pieConfig = {
        options: {
            chart: {
                type: 'pie',
                backgroundColor: null ,
                plotBackgroundColor: 'none',
                spacing: [0, 0, 0, 0],
                options3d: {
                    enabled: true,
                    reflow: true,
                    alpha: 45,
                    beta: 0
                },
            },
            tooltip: {
                pointFormat: '<b>{point.y}</b>({point.percentage:.2f}%)'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    depth: 15,
                    dataLabels: {
                        enabled: false
                    }
                }
            },
            colors: ['#006633',  '#006633', '#FF3333'],
            credits: {
                enabled: false
            }
        },
        series: [{
            name: 'share',
            data: largearry,
            dataLabels: {
                enabled: true,
                formatter: function() {
                    return this.point.percentage > 60 ? this.point.percentage.toFixed(2) + '%' : '';
                },
                // distance: 10,
                distance: -50,
                style: {
                    fontWeight: 'bold',
                    color: 'white',
                    textShadow: '0px'
                }
            }
        }],
        title: {
            text: ''
        },
        loading: false,
    };
    return pieConfig;
  }

  function genConfigBollinger(){
    return createBollinger();
  }

  function createBollinger(dataAsset){
    var data = retrieveDataFromMetricBollinger(dataAsset);
    var ranges=data[0],
        averages=data[1],
        realtime=data[2];
    /*
    var ranges = [
        [1246406400000, 14.3, 27.7],
        [1246492800000, 14.5, 27.8],
        [1246579200000, 15.5, 29.6],
        [1246665600000, 16.7, 30.7],
        [1246752000000, 16.5, 25.0],
        [1246838400000, 17.8, 25.7],
        [1246924800000, 13.5, 24.8],
        [1247011200000, 10.5, 21.4],
        [1247097600000, 9.2, 23.8],
        [1247184000000, 11.6, 21.8],
        [1247270400000, 10.7, 23.7],
        [1247356800000, 11.0, 23.3],
        [1247443200000, 11.6, 23.7],
        [1247529600000, 11.8, 20.7],
        [1247616000000, 12.6, 22.4],
        [1247702400000, 13.6, 19.6],
        [1247788800000, 11.4, 22.6],
        [1247875200000, 13.2, 25.0],
        [1247961600000, 14.2, 21.6],
        [1248048000000, 13.1, 17.1],
        [1248134400000, 12.2, 15.5],
        [1248220800000, 12.0, 20.8],
        [1248307200000, 12.0, 17.1],
        [1248393600000, 12.7, 18.3],
        [1248480000000, 12.4, 19.4],
        [1248566400000, 12.6, 19.9],
        [1248652800000, 11.9, 20.2],
        [1248739200000, 11.0, 19.3],
        [1248825600000, 10.8, 17.8],
        [1248912000000, 11.8, 18.5],
        [1248998400000, 10.8, 16.1]
    ],
    averages = [
        [1246406400000, 21.5],
        [1246492800000, 22.1],
        [1246579200000, 23],
        [1246665600000, 23.8],
        [1246752000000, 21.4],
        [1246838400000, 21.3],
        [1246924800000, 18.3],
        [1247011200000, 15.4],
        [1247097600000, 16.4],
        [1247184000000, 17.7],
        [1247270400000, 17.5],
        [1247356800000, 17.6],
        [1247443200000, 17.7],
        [1247529600000, 16.8],
        [1247616000000, 17.7],
        [1247702400000, 16.3],
        [1247788800000, 17.8],
        [1247875200000, 18.1],
        [1247961600000, 17.2],
        [1248048000000, 14.4],
        [1248134400000, 13.7],
        [1248220800000, 15.7],
        [1248307200000, 14.6],
        [1248393600000, 15.3],
        [1248480000000, 15.3],
        [1248566400000, 15.8],
        [1248652800000, 15.2],
        [1248739200000, 14.8],
        [1248825600000, 14.4],
        [1248912000000, 15],
        [1248998400000, 13.6]
    ],
    realtime = [
        [1246406400000, 24.5],
        [1246492800000, 21.1],
        [1246579200000, 26],
        [1246665600000, 29.8],
        [1246752000000, 19.4],
        [1246838400000, 16.3],
        [1246924800000, 28.3],
        [1247011200000, 25.4],
        [1247097600000, 13.4],
        [1247184000000, 19.7],
        [1247270400000, 20.5],
        [1247356800000, 27.6],
        [1247443200000, 27.7],
        [1247529600000, 26.8],
        [1247616000000, 27.7],
        [1247702400000, 13.3],
        [1247788800000, 16.8],
        [1247875200000, 18.9],
        [1247961600000, 17.8],
        [1248048000000, 18.4],
        [1248134400000, 23.7],
        [1248220800000, 13.7],
        [1248307200000, 24.6],
        [1248393600000, 25.3],
        [1248480000000, 12.3],
        [1248566400000, 12.8],
        [1248652800000, 11.2],
        [1248739200000, 24.8],
        [1248825600000, 28.4],
        [1248912000000, 25],
        [1248998400000, 23.6]
    ];*/
    return {
      options: {
          chart: {
              type: 'spline',
              backgroundColor: 'none',
              plotBackgroundColor: 'none',
              borderWidth: "1",
              borderColor: "white",

          },
          credits: {
              enabled: false
          },
          legend: {
              enabled: false
          },
          tooltip: {
              // valueSuffix: '%'
              shared: true
          }
      },
      series:  [{
                  name: 'Average',
                  data: averages,
                  zIndex: 1,
                  dashStyle: 'ShortDash',
                  marker: {
                    enabled: false
                      //fillColor: 'white',
                     // lineWidth: 2,

                  }
              },
              {
                    name: 'Realtime',
                    data: realtime,
                    zIndex: 1,
                    color: '#0066FF',
                    marker: {
                      enabled: true
                      //   fillColor: 'white',
                      //  lineWidth: 2,

                    },
                    dataLabels: {
                        enabled: false,
                        allowOverlap: true
                    }
              },
              {
                  name: 'Bands',
                  data: ranges,
                  type: 'arearange',
                  lineWidth: 0,
                  linkedTo: ':previous',


                  fillOpacity: 0.3,
                  zIndex: 0
              }],
      title: {
          text: '',
      },
      subtitle: {
          //text: dataAsset.name,
          text: dataAsset.name, //::TODO temperary
          style: {
              fontSize: '14px',
              color: 'yellow'
          }
      },
      xAxis: {
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          type: 'datetime',
      },
      yAxis: {
         gridLineDashStyle: 'ShortDash',
          // gridLineWidth: 0,
          // minorGridLineWidth: 0,
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          title: {
              text: 'Total Count',
              style: {
                  color: 'yellow'
              }
          }
      }

    }
  }

  function createMad(dataAsset){
    var data = retrieveDataFromMetricMad(dataAsset);
    var ranges=data[0],
        realtime=data[1];

    return {
      options: {
          chart: {
              type: 'spline',
              backgroundColor: 'none',
              plotBackgroundColor: 'none',
              borderWidth: "1",
              borderColor: "white",

          },
          credits: {
              enabled: false
          },
          legend: {
              enabled: false
          },
          tooltip: {
              // valueSuffix: '%'
              shared: true
          }
      },
      series:  [{
                    name: 'Realtime',
                    data: realtime,
                    zIndex: 1,
                    color: '#0066FF',
                    marker: {
                      enabled: true

                    },
                    dataLabels: {
                        enabled: false,
                        allowOverlap: true
                    }
              },
              {
                  name: 'Bands',
                  data: ranges,
                  type: 'arearange',
                  lineWidth: 0,
                  linkedTo: ':previous',

                  fillOpacity: 0.3,
                  zIndex: 0
              }],
      title: {
          text: '',
      },
      subtitle: {
          //text: dataAsset.name,
          text: dataAsset.name, //::TODO temperary
          style: {
              fontSize: '14px',
              color: 'yellow'
          }
      },
      xAxis: {
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          type: 'datetime',
      },
      yAxis: {
         gridLineDashStyle: 'ShortDash',
          // gridLineWidth: 0,
          // minorGridLineWidth: 0,
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          title: {
              text: 'Total Count',
              style: {
                  color: 'yellow'
              }
          }
      }

    }
  }

  function createTrend(dataAsset){
    var data = retrieveDataFromMetricTrend(dataAsset);

    return {
      options: {
          chart: {
              type: 'spline',
              backgroundColor: 'none',
              plotBackgroundColor: 'none',
              borderWidth: "1",
              borderColor: "white",

          },
          credits: {
              enabled: false
          },
          legend: {
              enabled: false
          },
          tooltip: {
              // valueSuffix: '%'
              shared: true
          }
      },
      series:  [{
                  name: 'Yesterday',
                  data: data[1],
                  zIndex: 1,
                  // dashStyle: 'ShortDash',
                  marker: {
                    enabled: false
                      //fillColor: 'white',
                     // lineWidth: 2,

                  }
              },
              {
                    name: 'Today',
                    data: data[0],
                    zIndex: 1,
                    color: '#0066FF',
                    marker: {
                      enabled: false
                    }
              }],
      title: {
          text: '',
      },
      subtitle: {
          text: dataAsset.name,
          style: {
              fontSize: '14px',
              color: 'yellow'
          }
      },
      xAxis: {
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          type: 'datetime',
      },
      yAxis: {
         gridLineDashStyle: 'ShortDash',
          // gridLineWidth: 0,
          // minorGridLineWidth: 0,
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          title: {
              text: 'Total Count',
              style: {
                  color: 'yellow'
              }
          }
      }

    }
  }

  function formatLabels(ranges, realtime){
    // return realtime;
    var marker = {
      symbol: 'circle',
      fillColor: '#f00',
      radius: 6
    };
    var dataLabels = {
        enabled: true,

        allowOverlap: true,
        useHTML: true,
        color: '#fff',
        padding: 5,
        style:{
          fontSize: '16px',
          textShadow: 'none'
        },
        formatter: function(){
          if(this.series.name != 'Realtime'){
            return null;
          }
          var ranges=[];
          angular.forEach(this.series.chart.series, function(ser){
            if(ser.name == 'Bands'){
              ranges = ser.yData;
              return;
            }
          });
          // var ranges = this.series.chart.series[1].yData;
          var realValue = this.point.y;
          var index = this.series.xData.indexOf(this.x);


         var delta = 0;

         if(ranges[index]){
           if(realValue == 11.2){
             debugger;
           }
           if(realValue > ranges[index][1]){  //upper data
            //  delta = realValue - ranges[index][1];
            //  return '<div class="fa fa-long-arrow-up">' + Math.round(delta/1000) + 'K (exceed upper band)</div>';
            // return '<div class="fa fa-long-arrow-up">' + delta + '</div>';
           }else if(realValue < ranges[index][0]){  //lower data
             delta = ranges[index][0] - realValue;
             return '<div><li class="fa fa-long-arrow-down" style="color:red;"></li>' + Math.round(delta/1000) + 'K (below lower band)</div>';
            // return '<div class="fa fa-long-arrow-down">' + delta + '</div>';
           }
         }
        //  return 'a';
        }

    };


    for(var i = 0; i < realtime.length; i ++){
      var real = realtime[i];
      var ran = ranges[i];
      var obj = {};
      if(real[1] < ran[1]){
        obj.x = real[0];
        obj.y = real[1];
        obj.dataLabels = dataLabels;
        obj.marker = marker;
        realtime[i] = obj;
      }

    }
    return realtime;
  }

  function createBollingerBig(dataAsset){

    var data = retrieveDataFromMetricBollinger(dataAsset);
    var ranges=data[0],
        averages=data[1],
        realtime=data[2];

/*
    var ranges = [
        [1246406400000, 14.3, 27.7],
        [1246492800000, 14.5, 27.8],
        [1246579200000, 15.5, 29.6],
        [1246665600000, 16.7, 30.7],
        [1246752000000, 16.5, 25.0],
        [1246838400000, 17.8, 25.7],
        [1246924800000, 13.5, 24.8],
        [1247011200000, 10.5, 21.4],
        [1247097600000, 9.2, 23.8],
        [1247184000000, 11.6, 21.8],
        [1247270400000, 10.7, 23.7],
        [1247356800000, 11.0, 23.3],
        [1247443200000, 11.6, 23.7],
        [1247529600000, 11.8, 20.7],
        [1247616000000, 12.6, 22.4],
        [1247702400000, 13.6, 19.6],
        [1247788800000, 11.4, 22.6],
        [1247875200000, 13.2, 25.0],
        [1247961600000, 14.2, 21.6],
        [1248048000000, 13.1, 17.1],
        [1248134400000, 12.2, 15.5],
        [1248220800000, 12.0, 20.8],
        [1248307200000, 12.0, 17.1],
        [1248393600000, 12.7, 18.3],
        [1248480000000, 12.4, 19.4],
        [1248566400000, 12.6, 19.9],
        [1248652800000, 11.9, 20.2],
        [1248739200000, 11.0, 19.3],
        [1248825600000, 10.8, 17.8],
        [1248912000000, 11.8, 18.5],
        [1248998400000, 10.8, 16.1]
    ],
    averages = [
        [1246406400000, 21.5],
        [1246492800000, 22.1],
        [1246579200000, 23],
        [1246665600000, 23.8],
        [1246752000000, 21.4],
        [1246838400000, 21.3],
        [1246924800000, 18.3],
        [1247011200000, 15.4],
        [1247097600000, 16.4],
        [1247184000000, 17.7],
        [1247270400000, 17.5],
        [1247356800000, 17.6],
        [1247443200000, 17.7],
        [1247529600000, 16.8],
        [1247616000000, 17.7],
        [1247702400000, 16.3],
        [1247788800000, 17.8],
        [1247875200000, 18.1],
        [1247961600000, 17.2],
        [1248048000000, 14.4],
        [1248134400000, 13.7],
        [1248220800000, 15.7],
        [1248307200000, 14.6],
        [1248393600000, 15.3],
        [1248480000000, 15.3],
        [1248566400000, 15.8],
        [1248652800000, 15.2],
        [1248739200000, 14.8],
        [1248825600000, 14.4],
        [1248912000000, 15],
        [1248998400000, 13.6]
    ],
    realtime = [
       [1246406400000, 29.5], //marker:{symbol: 'circle'}},
      //  {x:1246406400000, y:24.5, marker:{symbol: 'circle', radius: 6}},
        [1246492800000, 13.1],
        [1246579200000, 26],
        [1246665600000, 29.8],
        [1246752000000, 19.4],
        [1246838400000, 16.3],
        [1246924800000, 28.3],
        [1247011200000, 25.4],
      //  {x:1247011200000, y:25.4, marker:{symbol: 'circle', radius: 6}, dataLabels:{enabled:true}},
        [1247097600000, 13.4],
        [1247184000000, 19.7],
        [1247270400000, 20.5],
        [1247356800000, 27.6],
        [1247443200000, 27.7],
        [1247529600000, 26.8],
        [1247616000000, 27.7],
        [1247702400000, 13.3],
        [1247788800000, 16.8],
        [1247875200000, 18.9],
        [1247961600000, 17.8],
        [1248048000000, 18.4],
        [1248134400000, 23.7],
        [1248220800000, 13.7],
        [1248307200000, 24.6],
        [1248393600000, 25.3],
        [1248480000000, 12.3],
        [1248566400000, 12.8],
        [1248652800000, 11.2],
        [1248739200000, 24.8],
        [1248825600000, 28.4],
        [1248912000000, 25],
        [1248998400000, 23.6]
    ];
*/
   realtime = formatLabels(ranges, realtime);

    return {
      useHighStocks: true,
      options: {
          chart: {
              type: 'spline',
              backgroundColor: 'none',
              plotBackgroundColor: 'none',
              borderWidth: "0",
              spacing: [10, 15, 40, 15]
          },
          rangeSelector: {
              inputEnabled: false,
              buttons:[ {type: 'day', count: 1, text: '1d'},
                        {type: 'month', count: 1, text: '1m'},
                        {type: 'month', count: 3, text: '3m'},
                        {type: 'year', count: 1, text: '1y'},
                        {type: 'all', text: 'All'}],
              buttonTheme:{
                r: 8,
                states:{
                  select: {
                    fill: '#039',
                    style: {
                      color: 'white'
                    }
                  }
                }
              },
              selected: 1
          },
          navigator: {
              enabled: true
          },
          credits: {
              enabled: false
          },
          legend: {
              enabled: true
          },
          tooltip: {
              // valueSuffix: '%'
          }
      },
      series:  [{
                  name: 'Average',
                  data: averages,
                  zIndex: 1,
                  dashStyle: 'ShortDash',
                  marker: {
                    enabled: false
                      //fillColor: 'white',
                     // lineWidth: 2,
                  }
              },
              {
                    name: 'Realtime',
                    data: realtime,
                    zIndex: 1,
                    color: '#0066FF',
                    marker: {
                      enabled: true

                      //   fillColor: 'white',
                      //  lineWidth: 2,

                    },
                    turboThreshold:10000
                    /*
                    dataLabels: {
                        enabled: true,

                        allowOverlap: true,
                        useHTML: true,
                        color: '#F00',
                        padding: 5,
                        style:{
                          fontSize: '14px',
                          textShadow: 'none'
                        },
                        formatter: function(){
                          var ranges = this.series.chart.series[2].yData;
                          var realValue = this.point.y;
                         var index = this.series.points.indexOf(this.point);


                         var delta = 0;

                         if(ranges[index]){
                           if(realValue > ranges[index][1]){  //upper data
                             delta = realValue - ranges[index][1];
                             return '<div class="fa fa-long-arrow-up">' + delta.toFixed(2) + ' (exceed upper band)</div>';
                           }else if(realValue < ranges[index][0]){  //lower data
                             delta = ranges[index][0] - realValue;
                             return '<div class="fa fa-long-arrow-down">' + delta.toFixed(2) + ' (below lower band)</div>';
                           }
                         }
                        //  return 'a';
                        }

                    }
                    */
              },
              {
                  name: 'Bands',
                  data: ranges,
                  type: 'arearange',
                  lineWidth: 0,
                  linkedTo: ':previous',


                  fillOpacity: 0.3,
                  zIndex: 0
              }],
      title: {
          text: '',
      },
      subtitle: {
          useHTML: true,
          text: '<a href="/#/viewrule/' + dataAsset.name + '/">' + dataAsset.name + '</a>',
          // text: '<a href="http://www.bing.com">Link</a>',
          style: {
              fontSize: '20px',
              color: 'yellow'
          }
      },
      xAxis: {
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          type: 'datetime',
      },
      yAxis: {
          offset: 30,
          gridLineDashStyle: 'ShortDash',
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          title: {
              text: 'Total Count',
              style: {
                  color: 'yellow'
              }
          }
      }
    };
  }

  function createMadBig(dataAsset){

    var data = retrieveDataFromMetricMad(dataAsset);
    var ranges=data[0],
        realtime=data[1];

   realtime = formatLabels(ranges, realtime);

    return {
      useHighStocks: true,
      options: {
          chart: {
              type: 'spline',
              backgroundColor: 'none',
              plotBackgroundColor: 'none',
              borderWidth: "0",
              spacing: [10, 15, 40, 15]
          },
          rangeSelector: {
              inputEnabled: false,
              buttons:[ {type: 'day', count: 1, text: '1d'},
                        {type: 'month', count: 1, text: '1m'},
                        {type: 'month', count: 3, text: '3m'},
                        {type: 'year', count: 1, text: '1y'},
                        {type: 'all', text: 'All'}],
              buttonTheme:{
                r: 8,
                states:{
                  select: {
                    fill: '#039',
                    style: {
                      color: 'white'
                    }
                  }
                }
              },
              selected: 1
          },
          navigator: {
              enabled: true
          },
          credits: {
              enabled: false
          },
          legend: {
              enabled: true
          },
          tooltip: {
              // valueSuffix: '%'
          }
      },
      series:  [
              {
                    name: 'Realtime',
                    data: realtime,
                    zIndex: 1,
                    color: '#0066FF',
                    marker: {
                      enabled: true
                    },
                    turboThreshold:10000

              },
              {
                  name: 'Bands',
                  data: ranges,
                  type: 'arearange',
                  lineWidth: 0,
                  linkedTo: ':previous',


                  fillOpacity: 0.3,
                  zIndex: 0
              }],
      title: {
          text: '',
      },
      subtitle: {
          useHTML: true,
          text: '<a href="/#/viewrule/' + dataAsset.name + '/">' + dataAsset.name + '</a>',
          style: {
              fontSize: '20px',
              color: 'yellow'
          }
      },
      xAxis: {
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          type: 'datetime',
      },
      yAxis: {
          offset: 30,
          gridLineDashStyle: 'ShortDash',
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          title: {
              text: 'Total Count',
              style: {
                  color: 'yellow'
              }
          }
      }
    };
  }

  function createTrendBig(dataAsset){

    var data = retrieveDataFromMetricTrend(dataAsset);

    return {
      useHighStocks: true,
      options: {
          chart: {
              type: 'spline',
              backgroundColor: 'none',
              plotBackgroundColor: 'none',
              borderWidth: "0",
              spacing: [10, 15, 40, 15]
          },
          rangeSelector: {
              inputEnabled: false,
              buttons:[ {type: 'day', count: 1, text: '1d'},
                        {type: 'month', count: 1, text: '1m'},
                        {type: 'month', count: 3, text: '3m'},
                        {type: 'year', count: 1, text: '1y'},
                        {type: 'all', text: 'All'}],
              buttonTheme:{
                r: 8,
                states:{
                  select: {
                    fill: '#039',
                    style: {
                      color: 'white'
                    }
                  }
                }
              },
              selected: 1
          },
          navigator: {
              enabled: true
          },
          credits: {
              enabled: false
          },
          legend: {
              enabled: true
          },
          tooltip: {
              // pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y:,.0f}</b><br/>',
              // footerFormat: 'Day over Day: <span style="weight:bold">{points.length}<span>',
              formatter: function(){

              }
          }
      },

      series:  [
                {
                      name: 'This week',
                      data: data[0],
                      zIndex: 1,
                      color: '#0066FF',
                      marker: {
                        enabled: true,
                        radius: 6
                      }
                },
                {
                  name: 'Last week',
                  data: data[1],
                  zIndex: 1,
                  // dashStyle: 'ShortDash',
                  marker: {
                    enabled: true,
                    radius: 6
                      //fillColor: 'white',
                     // lineWidth: 2,

                  }
              }
              ],
      title: {
          text: '',
      },
      subtitle: {
          useHTML: true,
          text: '<a href="/#/viewrule/' + dataAsset.name + '/">' + dataAsset.name + '</a>',
          style: {
              fontSize: '20px',
              color: 'yellow'
          }
      },
      xAxis: {
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          type: 'datetime',
      },
      yAxis: {
          offset: 30,
          gridLineDashStyle: 'ShortDash',
          labels: {
              style: {
                  color: 'yellow'
              }
          },
          title: {
              text: 'Total Count',
              style: {
                  color: 'yellow'
              }
          }
      }
    };
  }

  //copy the properties from b to a if not existing in a
  function mergeConfig(a, b){
    if(b && Object.getOwnPropertyNames(b).length > 0){
      for(var prop in b){
        if(!a[prop]){
          a[prop] = b[prop];
        }else{
          mergeConfig(a[prop], b[prop]);
        }
      }
    }

    return a;
  }
});
