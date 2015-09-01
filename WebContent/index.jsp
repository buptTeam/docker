<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=gb2312">
<title>Web sockets test</title>
<script src="js/jquery-min.js" type="text/javascript"></script>
<script src="js/index.js" type="text/javascript"></script>

</head>
<body>
	
<form  method="post" action="http://166.111.143.224:8080/docker_new/api/image/uploadDockerfile"  enctype="multipart/form-data">
      <label ><font size="2" color="red">上传文件：</font></label>
      <input name="file" type="file" size="10"  >

	<input  type="submit" name="submit" value="提交" >
    <input  type="reset" name="reset" value="重置" >

	</form>
</body>
<script>
$(function(){
	var id="<%=request.getParameter("id") %>";
	Init(id);
});
</script>
</html>

