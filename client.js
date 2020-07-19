(function() {
    var timestampInit = null,
        initInterval = null;

    $(function() {
        Client.loading();
    });

    Client = {
        alerts: 0,
        videos: 0,
        clientWordsInterval: null,
        notifications: 0,

        init: function() {
            var self = this;

            $.post('/api/get/socket.php', {
                hash: clientvars['user.hash']
            }, function(data) {
                for (i in data.orders) {
                    var action = data.orders[i].action,
                        type = data.orders[i].type,
                        label = data.orders[i].label;

                    if (action !== 'undefined') {
                        if (action === 'alert' && type !== null) {
                            self.alert(type, label);
                        }

                        if (action === 'roomvideo') {
                            self.video(label);
                        }

                        if (action === 'mention') {
                            self.notification(label);
                        }
                    }
                }
            });
        },

        alert: function(type, string) {
            if (typeof string !== 'undefined') {
                var self = this,
                    label = string.split(';');

                this.alerts += 1;

                if (type == 'eha') {
                    $('.webview-client').append(
                        '<div class="event-alert ' + label[0] + ' flex-column padding-max center-container-top-bottom" data-alert="' + self.alerts + '">' + '\n' +
                        '<div class="event-alert-image"></div>' + '\n' +
                        '<label class="event-alert-label white flex-column">' + '\n' +
                        '<h3 class="bold margin-bottom-minm uppercase">Evento de ' + label[0] + '</h3>' + '\n' +
                        '<h5 class="fs-14 margin-bottom-max">Está acontecendo um novo evento aqui no <b>Haibbo Hotel</b>!</h5>' + '\n' +
                        '<h5 class="fs-14 margin-bottom-max">O evento da vez é <b class="lowercase">' + label[1] + '</b>, onde você tem suas chances de ganhar gemas, rubis, pontos de eventos e um emblema lindo, não só isso mas também, você pode se destacar no Hall da Fama por ter a maior pontuação de eventos e ganhar certas recompensas!</h5>' + '\n' +
                        '</label>' + '\n' +
                        '<div class="event-alert-infos flex margin-auto-top">' + '\n' +
                        '<div class="event-alert-promoter flex">' + '\n' +
                        '<div class="event-alert-promoter-imager">' + '\n' +
                        '<img alt="' + label[2] + '" src="https://habbo.city/habbo-imaging/avatarimage?figure=' + label[3] + '&headonly=1&size=n&gesture=sml&direction=2&head_direction=2&action=std">' + '\n' +
                        '</div>' + '\n' +
                        '<label class="flex white">' + '\n' +
                        '<h5>Por <b>' + label[2] + '</b></h5>' + '\n' +
                        '</label>' + '\n' +
                        '</div>' + '\n' +
                        '<div class="event-alert-interactions flex margin-auto-left" data-alert-close="' + self.alerts + '">' + '\n' +
                        '<button class="event-alert-interaction-close">Fechar</button>' + '\n' +
                        '<button class="event-alert-interaction-go" event="event:navigator/goto/' + label[4] + '">Jogar</button>' + '\n' +
                        '</div>' + '\n' +
                        '</div>' + '\n' +
                        '</div>'
                    );
                }

                $('.webview-client').find('div[data-alert="' + self.alerts + '"]').css({
                	'left': '-600px'
                }).animate({
                    'left': '0px'
                });
            }
        },

        video: function(string) {
            var self = this,
                videoContainer = $('.webview-client > .habbo-container.youtube')
                videoID = YouTubeURL(string);

            if (!videoID == false) {
	            if (videoContainer.length > 0) {
	                videoContainer.hide();
	                videoContainer.empty().html(
	                    '<div class="habbo-container-header flex">' + '\n' +
	                    '    <label>Video</label>' + '\n' +
	                    '</div>' + '\n' +
	                    '<div class="habbo-container-navigation">' + '\n' +
	                    '    <iframe class="roomvideo-iframe" frameborder="0" allowfullscreen="1" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" src="https://www.youtube.com/embed/' + videoID + '?autoplay=1&amp;fs=0&amp;modestbranding=1&amp;rel=0&amp;enablejsapi=1&amp;origin=https://haibbo.in&amp;widgetid=1"></iframe>' + '\n' +
	                    '</div>' + '\n' +
	                    '<button class="habbo-container-close"></button>'
	                );

	                videoContainer.show();
	            } else {
	                $('.webview-client').append(
	                    '<div class="habbo-container blue youtube">' + '\n' +
	                    '   <div class="habbo-container-header flex">' + '\n' +
	                    '       <label>Video</label>' + '\n' +
	                    '   </div>' + '\n' +
	                    '   <div class="habbo-container-navigation">' + '\n' +
	                    '       <iframe class="roomvideo-iframe" frameborder="0" allowfullscreen="1" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" src="https://www.youtube.com/embed/' + videoID + '?autoplay=1&amp;fs=0&amp;modestbranding=1&amp;rel=0&amp;enablejsapi=1&amp;origin=https://haibbo.in&amp;widgetid=1"></iframe>' + '\n' +
	                    '   </div>' + '\n' +
	                    '   <button class="habbo-container-close"></button>' + '\n' +
	                    '</div>'
	                );

	                $('.habbo-container.youtube').center();
	            }

	            $('.habbo-container.youtube').draggable({
	                stack: 'div.habbo-container',
	                handle: '.habbo-container-header',
	                containment: '.webview-client'
	            });
        	}
        },

        notification: function(label) {
            var self = this,
                label = label.split(';');

            $('.client-notification > .client-notification-area').prepend(
                '<div class="client-notification-mention flex-column" data-notification="' + self.notifications + '">' + '\n' +
                '   <div class="flex">' + '\n' +
                '       <div class="client-notification-mention-imager">' + '\n' +
                '           <img alt="' + label[0] + '" src="https://habbo.city/habbo-imaging/avatarimage?figure=' + label[1] + '&headonly=0&size=n&gesture=sml&direction=2&head_direction=3&action=wav">' + '\n' +
                '       </div>' + '\n' +
                '       <div class="client-notification-content flex-column">' + '\n' +
                '           <div class="client-notification-content-info flex padding-min gray-clear">' + '\n' +
                '               <h5 class="client-notification-content-username">' + label[0] + '</h5>' + '\n' +
                '           </div>' + '\n' +
                '           <div class="client-notification-content-message margin-min">' + '\n' +
                '               <h6 class="fs-12 padding-min"></h6>' + '\n' +
                '           </div>' + '\n' +
                '       </div>' + '\n' +
                '   </div>' + '\n' +
                '   <button class="client-notification-mention-close"></button>' + '\n' +
                '</div>'
            );

            $('.client-notification-content-message').find('h6').text(label[2]).html();

            var container = $('.client-notification-mention[data-notification="' + this.notifications + '"]'),
                height = container.height() + 18;

            container.animate({
                left: '0px'
            }, 250);

            if (this.notifications > 0) {
                if ($('.client-notification-area > div.client-notification-mention').length > 1) {
                    $('button.client-notification-closeall').show();
                }

                $('.client-notification-mention').each(function() {
                    if ($(this).attr('data-notification') != self.notifications) {
                        $(this).animate({
                            top: '+=' + height + 'px'
                        }, 250);
                    }
                });
            }

            this.notifications += 1;
        },

        loading: function() {
            /*var wordsClient = clientvars['client.starting.revolving'],
                clientWords = wordsClient.split('/');

            clientWordsInterval = setInterval(function() {
                $('.loader-client').find('.loader-client-words > strong').fadeOut(500, function() {
                    $(this).text(clientWords[Math.floor(Math.random() * clientWords.length)]).fadeIn(500);
                });
            }, 5000);*/

            $('.client-disconnected').hide();
        },

        loaded: function() {
            var self = this;

            /*clearInterval(this.clientWordsInterval);
            this.clientWordsInterval = null;*/

            setTimeout(function() {
                /*$('.loader-client').fadeOut(500, function() {
                    $('.loader-client').remove();
                });*/

              	initInterval = setInterval(function() {
              		self.init();
              	}, 1500);

            }, 5000);
        },

        disconnected: function() {
            /*var self = this;

            clearInterval(this.clientWordsInterval);
            this.clientWordsInterval = null;*/

            //$('.loader-client').remove();
            clearInterval(initInterval);
            $('.webview-client > .habbo-container.youtube').remove();
            $('.client-disconnected').show();
        }
    }

    function YouTubeURL(url) {
    	var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
    	var match = url.match(regExp);

    	if (match && match[7].length == 11) {
    		var b = match[7];

    		return b;
    	} else {
    		return false;
    	}
    }

    $('body').on('click', '.event-alert-interaction-close', function() {
    	CloseEventAlert(Client.alerts);
    });

    $('body').on('click', '.event-alert-interaction-go', function() {
    	GoTo($(this).attr('event'));
    	CloseEventAlert(Client.alerts);
    });

    function CloseEventAlert(id) {
    	Client.alerts -= 1;

    	$('.event-alert[data-alert="' + id + '"]').addClass('pointer-none').animate({
    		'left': '-600px'
    	});

    	setTimeout(function() {
    		$('.event-alert[data-alert="' + id + '"]').remove();
    	}, 500);
    }

    $(document).on('click', '.habbo-container.youtube > .habbo-container-close', function() {
    	var container = $(this).parent();

    	container.hide().find('iframe.roomvideo-iframe').attr('src', null);
    });

    function GoTo(e) {
    	e.match(/^event:/) ? flashEvent.openlink(e.replace("event:", "")) : window.open(e);
    }

    $(document).on('click', 'button.client-notification-mention-close', function() {
    	var container = $(this).parent(),
    	height = container.height() + 18;

    	$('.client-notification-mention').each(function() {
    		var id = $(this).attr('data-notification');

    		if (id < container.attr('data-notification')) {
    			$(this).animate({
    				top: '-=' + height + 'px'
    			}, 250);
    		}
    	});

    	container.addClass('pointer-none');
    	container.animate({
    		left: '-350px'
    	}, 250, function() {
    		container.remove();

    		if ($('.client-notification-area > div.client-notification-mention').length <= 1) {
    			$('button.client-notification-closeall').hide();
    		}
    	});
    });

    $(document).on('click', 'button.client-notification-closeall', function() {
    	var container = $('.client-notification-mention');

    	container.addClass('pointer-none');

    	container.animate({
    		left: '-350px'
    	}, 250, function() {
    		$('button.client-notification-closeall').hide();
    		container.remove();
    	});
    });
})();

$(document).on('click', '.set-client-version > button', function() {
    var button = $(this),
        data = {
            order: 'version',
            version: $(this).attr('version')
        }

    $.ajax({
        url: '/api/client',
        type: 'POST',
        data: data,
        dataType: 'json',
        beforeSend: function() {
            $('.set-client-version').addClass('pointer-none');

            button.animate({
                'opacity': '0.8'
            });
        },
        success: function(data) {
            if (data['response']) {
                window.location.reload();
            } else {
                $('.set-client-version').removeClass('pointer-none');

                button.animate({
                    'opacity': '1'
                });
            }
        }
    });
});