   <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>  
      <%@ taglib prefix="s" uri="/struts-tags"%>  
  <%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
 <head>
 <sj:head compressed="false" useJqGridPlugin="false" jqueryui="false"  locale="zh-CN" defaultIndicator="myDefaultIndicator"/>
    <script type="text/javascript" src="<%= request.getContextPath() %>/struts/js/jquery.cookie.js"></script>
 <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/org/cometd.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/org/cometd/AckExtension.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/org/cometd/ReloadExtension.js"></script>
    <!-- script type="text/javascript" src="<%= request.getContextPath() %>/jquery/jquery-1.3.2.js"></script-->
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery.json-2.2.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery.cometd.js"></script>
    <script type="text/javascript" src="<%= request.getContextPath() %>/scripts/jquery.cometd-reload.js"></script>
    <script type="text/javascript">
    var config = {
 			contextPath: '${pageContext.request.contextPath}'
		};
    </script>
 </head>
 
 <div id="chat_container">
  <div id="members">
   
  </div>
   <input id="leaveButton" type="button" value="leave"/>
  <div id="chat_right">
    <div id="chat"></div>
    <div id="msg">
      <input id="phrase" type="text" size="60" maxlength="85"/><input id="sendButton" type="button" value="send"/>
    </div>
  </div>
 </div>
 <script type="text/javascript">
 

 $(document).ready(function()
		    {
		        // Check if there was a saved application state
		        var stateCookie = org.cometd.COOKIE.get('org.cometd.demo.state');
		        var state = stateCookie ? org.cometd.JSON.fromJSON(stateCookie) : null;
		        var chat = new Chat(state);

		        // Setup UI
		      
		       
		        //$('#altServer').attr('autocomplete', 'off');
		        //$('#joinButton').click(function() { chat.join($('#username').val()); });
		         chat.join("${sessionScope.user_info.employeeId}");
		        $('#sendButton').click(chat.send);
		        $('#leaveButton').click(chat.leave);
		        /*$('#username').attr('autocomplete', 'off').focus();
		        $('#username').keyup(function(e)
		        {
		            if (e.keyCode == 13)
		            {
		                chat.join();
		            }
		        });*/
		        $('#phrase').attr('autocomplete', 'off');
		        $('#phrase').keyup(function(e)
		        {
		            if (e.keyCode == 13)
		            {
		                chat.send();
		            }
		        });
		    });

		    function Chat(state)
		    {
		        var _self = this;
		        var _wasConnected = false;
		        var _connected = false;
		        var _username;
		        var _lastUser;
		        var _disconnecting;
		        var _chatSubscription;
		        var _membersSubscription;

		        this.join = function(username)
		        {
		            _disconnecting = false;
		            _username = username;
		            if (!_username)
		            {
		                alert('Please enter a username');
		                return;
		            }

		            var cometdURL = location.protocol + "//" + location.host + config.contextPath + "/cometd";
		            

		            $.cometd.configure({
		                url: cometdURL,
		                logLevel: 'debug'
		            });
		            $.cometd.handshake();
		            _connectionEstablished();
		            //$('#join').hide();
		            //$('#joined').show();
		            $('#phrase').focus();
		            
		        };

		        this.leave = function()
		        {
		            $.cometd.batch(function()
		            {
		                $.cometd.publish('/chat/demo', {
		                    user: _username,
		                    chat: _username + ' has left'
		                });
		                _unsubscribe();
		            });
		            $.cometd.disconnect();

		           
		            _username = null;
		            _lastUser = null;
		            _disconnecting = true;
		        };

		        this.send = function()
		        {
		            var phrase = $('#phrase');
		            var text = phrase.val();
		            phrase.val('');

		            if (!text || !text.length) return;

		            var colons = text.indexOf('::');
		            if (colons > 0)
		            {
		                $.cometd.publish('/service/privatechat', {
		                    room: '/chat/demo',
		                    user: _username,
		                    chat: text.substring(colons + 2),
		                    peer: text.substring(0, colons)
		                });
		            }
		            else
		            {
		                $.cometd.publish('/chat/demo', {
		                    user: _username,
		                    chat: text
		                });
		            }
		        };

		        this.receive = function(message)
		        {
		            var fromUser = message.data.user;
		            var membership = message.data.membership;
		            var text = message.data.chat;

		            if (!membership && fromUser == _lastUser)
		            {
		                fromUser = '...';
		            }
		            else
		            {
		                _lastUser = fromUser;
		                fromUser += ':';
		            }

		            var chat = $('#chat');
		            if (membership)
		            {
		                chat.append('<span class=\"membership\"><span class=\"from\">' + fromUser + '&nbsp;</span><span class=\"text\">' + text + '</span></span><br/>');
		                _lastUser = null;
		            }
		            else if (message.data.scope == 'private')
		            {
		                chat.append('<span class=\"private\"><span class=\"from\">' + fromUser + '&nbsp;</span><span class=\"text\">[private]&nbsp;' + text + '</span></span><br/>');
		            }
		            else
		            {
		                chat.append('<span class=\"from\">' + fromUser + '&nbsp;</span><span class=\"text\">' + text + '</span><br/>');
		            }

		            // There seems to be no easy way in jQuery to handle the scrollTop property
		            chat[0].scrollTop = chat[0].scrollHeight - chat.outerHeight();
		        };

		        /**
		         * Updates the members list.
		         * This function is called when a message arrives on channel /chat/members
		         */
		        this.members = function(message)
		        {
		            var list = '';
		            $.each(message.data, function()
		            {
		                list += this + '<br/>';
		            });
		            $('#msg>.dbx-content').html(list);
		        };

		        function _unsubscribe()
		        {
		            if (_chatSubscription)
		            {
		                $.cometd.unsubscribe(_chatSubscription);
		            }
		            _chatSubscription = null;
		            if (_membersSubscription)
		            {
		                $.cometd.unsubscribe(_membersSubscription);
		            }
		            _membersSubscription = null;
		        }

		        function _subscribe()
		        {
		            _chatSubscription = $.cometd.subscribe('/chat/demo', _self.receive);
		            _membersSubscription = $.cometd.subscribe('/chat/members', _self.members);
		        }

		        function _connectionEstablished()
		        {
		            _self.receive({
		                data: {
		                    user: 'system',
		                    chat: 'Connection to Server Opened'
		                }
		            });
		            $.cometd.batch(function()
		            {
		                _unsubscribe();
		                _subscribe();
		                $.cometd.publish('/service/members', {
		                    user: _username,
		                    room: '/chat/demo'
		                });
		                $.cometd.publish('/chat/demo', {
		                    user: _username,
		                    membership: 'join',
		                    chat: _username + ' has joined'
		                });
		            });
		        }

		        function _connectionBroken()
		        {
		            _self.receive({
		                data: {
		                    user: 'system',
		                    chat: 'Connection to Server Broken'
		                }
		            });
		            $('#members').empty();
		        }

		        function _connectionClosed()
		        {
		            _self.receive({
		                data: {
		                    user: 'system',
		                    chat: 'Connection to Server Closed'
		                }
		            });
		        }

		        function _metaConnect(message)
		        {
		            if (_disconnecting)
		            {
		                _connected = false;
		                _connectionClosed();
		            }
		            else
		            {
		                _wasConnected = _connected;
		                _connected = message.successful === true;
		                if (!_wasConnected && _connected)
		                {
		                    _connectionEstablished();
		                }
		                else if (_wasConnected && !_connected)
		                {
		                    _connectionBroken();
		                }
		            }
		        }

		        //$.cometd.addListener('/meta/connect', _metaConnect);

		        // Restore the state, if present
		        /*if (state)
		        {
		            _connected = state.connected === true;
		            if (_connected)
		            {
		                setTimeout(function()
		                {
		                    _self.join(state.username);
		                    _connectionEstablished();
		                }, 0);
		            }
		        }*/
				var thiz=this;
		        $(window).unload(function()
		        {
		             //$.cometd.reload();
		            //if(!_disconnecting)
		            	thiz.leave();
		            // Save the application state only if the user was chatting
		            /*if (_wasConnected && _username)
		            {
		                org.cometd.COOKIE.set('org.cometd.demo.state', org.cometd.JSON.toJSON({
		                    connected: _wasConnected,
		                    username: _username
		                }), { 'max-age': 100 });
		            }*/
		        });
		    };
		 
 </script>