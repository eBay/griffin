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
define(['./module'], function(services) {
    'use strict';
    //version
    services.value('version', '0.1');

    services.factory('$config', function() {


        var BACKEND_SERVER = '';
        //var BACKEND_SERVER = 'http://localhost:8080'; //dev env
        var ROLLUP_SERVER = '';
        //var ROLLUP_SERVER = 'http://localhost:18090';
        var ROLLUP_UNIT = '30'; // in days

        var API_ROOT_PATH = '/api/v1';

        var config = {
            job_preorderset: { //error if begin with a num
                job5812_AmazonUSPriceRefresh: 1,
                job5813_AmazonUKPriceRefresh: 2,
                job5814_AmazonDEPriceRefresh: 3,
                job6448_AmazonFRPriceRefresh: 4,
                job6449_AmazonESPriceRefresh: 5,
                job6450_AmazonITPriceRefresh: 6,
                job5688_AmazonUSDiscovery: 7,
                job5689_AmazonUKDiscovery: 8,
                job5691_AmazonDEDiscovery: 9,
                job6902_WalmartUSDiscovery: 10,
                job6451_AmazonFRDiscovery: 11,
                job6452_AmazonESDiscovery: 12,
                job6453_AmazonITDiscovery: 13,
                job6903_WalmartUSPriceRefresh: 14,
                job6747_AmazonUSBestSeller: 15,
                job6748_AmazonUKBestSeller: 16,
                job6750_AmazonFRBestSeller: 16,
                job6749_AmazonDEBestSeller: 18,
                job6751_AmazonITBestSeller: 19,
                job6752_AmazonESBestSeller: 20
            },
            // URI paths, always have a trailing /
            uri: {
                base: BACKEND_SERVER + API_ROOT_PATH,

                //mock data
                // statistics:'/js/mock_data/statistics.json',
                //briefmetrics:'/js/mock_data/briefmetrics.json',
                //heatmap: '/js/mock_data/briefmetrics.json',
                // dbtree: '/js/mock_data/dbtree1.json',
                // schemadefinition: '/js/mock_data/schemadefinition.json',
                // rulemetric: '/js/mock_data/rulemetric.json',
                // dashboard: '/js/mock_data/briefmetrics.json' ,
                // allModels: "http://localhost:8080/" + API_ROOT_PATH + '/model/allModels',
                // newModel: "http://localhost:8080/" + API_ROOT_PATH + '/model/newModel' ,
                // deleteModel: "http://localhost:8080" + API_ROOT_PATH + '/model/deleteModel' ,

                //real data
                //data asset
                dbtree: BACKEND_SERVER + API_ROOT_PATH + '/dataassets/metadata',
                schemadefinition: BACKEND_SERVER + API_ROOT_PATH + '/dataassets',
                dataassetlist: BACKEND_SERVER + API_ROOT_PATH + '/dataassets',
                adddataasset: BACKEND_SERVER + API_ROOT_PATH + '/dataassets',
                updatedataasset: BACKEND_SERVER + API_ROOT_PATH + '/dataassets',
                getdataasset: BACKEND_SERVER + API_ROOT_PATH + '/dataassets',
                deletedataasset: BACKEND_SERVER + API_ROOT_PATH + '/dataassets',

                //mydashboard
                getmydashboard: ROLLUP_SERVER + API_ROOT_PATH + '/crawler/metrics/' + ROLLUP_UNIT, //BACKEND_SERVER + API_ROOT_PATH + '/metrics/mydashboard/',
                getsubscribe: BACKEND_SERVER + API_ROOT_PATH + '/subscribe/',
                newsubscribe: BACKEND_SERVER + API_ROOT_PATH + '/subscribe',

                //metrics
                statistics: BACKEND_SERVER + API_ROOT_PATH + '/metrics/statics',
                briefmetrics: BACKEND_SERVER + API_ROOT_PATH + '/metrics/briefmetrics',
                heatmap: BACKEND_SERVER + API_ROOT_PATH + '/metrics/heatmap',
                metricdetail: BACKEND_SERVER + API_ROOT_PATH + '/metrics/complete',
                rulemetric: BACKEND_SERVER + API_ROOT_PATH + '/metrics/brief',
                dashboard: ROLLUP_SERVER + API_ROOT_PATH + '/crawler/metrics/' + ROLLUP_UNIT, //BACKEND_SERVER + API_ROOT_PATH + '/metrics/dashboard',

                metricsample: BACKEND_SERVER + API_ROOT_PATH + '/metrics/sample',
                metricdownload: BACKEND_SERVER + API_ROOT_PATH + '/metrics/download',

                //rollup service:/metrics/Crawler/{days}

                rollupmetric: ROLLUP_SERVER + API_ROOT_PATH + '/crawler/metrics/' + ROLLUP_UNIT,

                //Models
                allModels: BACKEND_SERVER + API_ROOT_PATH + '/models',
                deleteModel: BACKEND_SERVER + API_ROOT_PATH + '/models',
                getModel: BACKEND_SERVER + API_ROOT_PATH + '/models',
                enableModel: BACKEND_SERVER + API_ROOT_PATH + '/models/enableModel',

                newAccuracyModel: BACKEND_SERVER + API_ROOT_PATH + '/models',
                newValidityModel: BACKEND_SERVER + API_ROOT_PATH + '/models',
                newAnomalyModel: BACKEND_SERVER + API_ROOT_PATH + '/models',
                newPublishModel: BACKEND_SERVER + API_ROOT_PATH + '/models',
                // newAccuracyModel: BACKEND_SERVER + API_ROOT_PATH + '/models/newAccuracyModel' ,
                // newValidityModel: BACKEND_SERVER + API_ROOT_PATH + '/model/newValidityModel' ,
                // newAnomalyModel: BACKEND_SERVER + API_ROOT_PATH + '/model/newAnomalyModel' ,
                // newPublishModel: BACKEND_SERVER + API_ROOT_PATH + '/model/newPublishModel' ,
                // getAccuracyModel: BACKEND_SERVER + API_ROOT_PATH + '/model/getAccuracyModel',
                // getValidityModel: BACKEND_SERVER + API_ROOT_PATH + '/model/getValidityModel',
                // getPublishModel: BACKEND_SERVER + API_ROOT_PATH + '/model/getPublishModel',
                // getAnomalyModel: BACKEND_SERVER + API_ROOT_PATH + '/model/getAnomalyModel',

                //Notification
                getnotifications: BACKEND_SERVER + API_ROOT_PATH + '/notifications',
            }

        };

        return config;
    });
});