var ws;
var WebSocketsExist = true;
var containerId = "";
var swarmHost = "166.111.143.224:9375";
function startConnection(url) {
	Log("正在建立连接。。。<br/>", "OK");
	try {
		if ("WebSocket" in window) {
			ws = new WebSocket("ws://" + url);
		} else if ("MozWebSocket" in window) {
			ws = new MozWebSocket("ws://" + url);
		}

	} catch (ex) {
		console.info(ex);
		//Log(ex.TypeError, "ERROR");
		return;
	};
	//console.info(ws.readyState);
	ws.onopen = WSonOpen;
	ws.onmessage = WSonMessage;
	ws.onclose = WSonClose;
	ws.onerror = WSonError;

};

function WSonOpen() {

	Log("连接建立成功，可以开始输入shell命令了<br/>", "OK");

	//ws.send("login:" + document.getElementById("txtName").value);
};

function WSonMessage(event) {
	//console.info(event);
	Log(event.data);
};

function WSonClose() {
	Log("失去连接", "ERROR");
};

function WSonError() {

	Log("远程连接中断。", "ERROR");
};

function SendDataClicked(mes) {
	try {
		//console.info("click"+(ws.readyState));
		if(ws.readyState==2||ws.readyState==3){
			Log("连接不可用<br/>", "ERROR");
			return 0;
		}
			
		//if (document.getElementById("DataToSend").value.trim() != "") {
			ws.send(mes + "\n");
			document.getElementById("DataToSend").value = "";
		//}
	} catch (ex) {
		console.info(ex);
		//Log(ex, "ERROR");
		return;
	}

};

function Log(Text, MessageType) {
	//console.info("before"+Text);
	Text = Text.replace(/[\r]/g, "<br />");
	Text = Text.replace(/\[[\d;]+m/g, "");
	Text = Text.replace(/\]0;/g, "");
	//Text="(B[mfdsfdsfds(B[m";(B[m  (B[m
	
	//top command
	Text = Text.replace(/\(B\[m/g, "");
	Text = Text.replace(/\[\?1h=\[\?25l/g, "");
	Text = Text.replace(/\[H\[2J/g, "");
	Text = Text.replace(/\[K/g, "");
	Text = Text.replace(/\[J/g, "");
	Text = Text.replace(/\[H/g, "");
	
	
	
//	console.info("after"+Text);
	if (MessageType == "OK")
		Text = "<span style='color: green;'>" + Text + "</span>";
	if (MessageType == "ERROR")
		Text = "<span style='color: red;'>" + Text + "</span>";
	document.getElementById("LogContainer").innerHTML = document
			.getElementById("LogContainer").innerHTML
			+ Text;
	var LogContainer = document.getElementById("outerContainer");
	LogContainer.scrollTop = LogContainer.scrollHeight;
};

Init=function(id) {
			
			containerId = id;
			var url = swarmHost + "/containers/" + containerId
					+ "/attach/ws?logs=0&stream=1&stdin=1&stdout=1&stderr=1";
            //console.info(url);
			try {
				var dummy = new WebSocket("ws://" + url);
				//dummy.close();
			} catch (ex) {
				//Log(ex, "ERROR");
				try {
					webSocket = new MozWebSocket("ws://" + url);
					webSocket.close();
				} catch (ex) {
					WebSocketsExist = false;
				}
			}

			if (WebSocketsExist) {
				//Log("您的浏览器支持WebSocket. 您可以尝试连接到聊天服务器!", "OK");
				//document.getElementById("Connection").value =url ;
				;
			} else {
				Log("您的浏览器不支持WebSocket。请选择其他的浏览器再尝试连接服务器。", "ERROR");
				//document.getElementById("ToggleConnection").disabled = true;
			}
			startConnection(url);

//			$("#DataToSend").keypress(function(evt) {
//				if (evt.keyCode == 13) {
//					SendDataClicked();
//					evt.preventDefault();
//				}
//				if (evt.keyCode == 38) {
//					//SendDataClicked();
//					
//					evt.preventDefault();
//				}
//			});
		};
		function fnKeyDown(evt) {
			
			evt = (evt) ? evt : ((window.event) ? window.event : "");
			var key = evt.keyCode ? evt.keyCode : evt.which;
			if (key == 38) {
				//console.info("upup");
//				SendDataClicked("0X38");
//				evt.preventDefault();
//				test = "你好abc" ;
//				str = "" ;
//				for( i=0;    i<test.length; i++ )      {  
//					temp = test.charCodeAt(i).toString(16); 
//					str    += "\\u"+ new Array(5-String(temp).length).join("0") +temp; 
//					}      
//				document.write (str) ;
				
				SendDataClicked(String.fromCharCode(key));
				//console.info("\0x38");
				//evt.preventDefault();
			}
			else if (key == 13) {
				SendDataClicked(document.getElementById("DataToSend").value.trim());
				evt.preventDefault();
			}
			else	if (evt.keyCode == 67 && evt.ctrlKey) {  
				SendDataClicked("\3");
            } 
		}