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
$(function() {
  function setWindowSize(){
    width = window.innerWidth;
    height = window.innerHeight;
    $('#containerDiv').width(width).height(height);
    $("#content").offset({
        top: height * 0.1
    }).width(width * 0.8).height(height * 0.8);
    $('#circular').height($("#content").height() * 0.6).width($('#circular').height()).
    css({
        "margin-top": $("#content").height() * 0.15,
        "margin-left": $("#content").height() * 0.5 * 0.2
    });
    $('#explaination').height($("#content").height() * 0.7).
    css({
        "margin-top": $("#content").height() * 0.15
    });
  }

    setWindowSize();
    $(window).resize(function(){
      setWindowSize();
    });


    //$('[data-toggle="tooltip"]').tooltip();
    $('input:eq(1)').keyup(function(evt){
      if(evt.which == 13){//enter
        evt.preventDefault();
        $('button').click();
        $('button').focus();
      }
    });

    $('input:eq(1)').focus(function(evt){
      $('#loginMsg').hide();
    });

    $('button').click(function() {

        var name = $('input:eq(0)').val();
        var password = $('input:eq(1)').val();


        // var loginUrl = '/api/v1/login/authenticate';
        var loginUrl = 'http://localhost:8080/api/v1/login/authenticate'; //dev env

        showLoginSpinner();
				$.ajax({
					type: 'POST',
					url: loginUrl,
					data: JSON.stringify({username:name, password:password}),
					contentType: 'application/json',
					dataType: 'json',
					success: function(data){
						console.log(data);
						if(data.status == 0){//logon success
              //console.log($('input:eq(3)').val());
              if($('input:eq(2)').prop('checked')){
							  setCookie('ntAccount', data.ntAccount, 30);
                setCookie('fullName', data.fullName, 30);
              }else{
                setCookie('ntAccount', data.ntAccount);
                setCookie('fullName', data.fullName);
              }

							window.location.replace('/');
						}else{
							showLoginFailed();
						}
					},
					error: function(jqXHR, textStatus, errorThrown){showLoginFailed();}

				});

    });

		function setCookie(name, value, days){
			if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        expires = "; expires=" + date.toGMTString();
	    } else {
	        expires = "";
	    }
	    document.cookie = encodeURIComponent(name) + "=" + encodeURIComponent(value) + expires + "; path=/";
		}

		function getCookie(key) {
        var keyValue = document.cookie.match('(^|;) ?' + key + '=([^;]*)(;|$)');
        return keyValue ? keyValue[2] : null;
    }

    function showLoginSpinner(){
      $('#loginMsg').show()
        .text('logging in......')
        .css('color', '#888888');
    }

    function showLoginFailed(){
      $('#loginMsg').show()
        .text('Login failed. Try again.')
        .css('color', '#f00');
    }

})
