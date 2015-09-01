<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=gb2312">
<title>container console</title>
<style type="text/css">
.container {
	font-family: "Courier New";
	width: 100%;
	height: 650px;
	overflow: auto;
	border: 1px solid black;
}
</style>

<script src="js/jquery-min.js" type="text/javascript"></script>
<script src="js/index.js" type="text/javascript"></script>

</head>
<body>
	
	<form id="form1" runat="server">
		<div id="outerContainer" class='container'>
		
		<span id='LogContainer' >
		</span>
			<input type="text" id="DataToSend" style="width: 60%;"/>
		</div>
		<br />

	</form>
</body>
<script>
$(function(){
	var id="<%=request.getParameter("id") %>";
	document.onkeydown=fnKeyDown;
	Init(id);
});
</script>
</html>

