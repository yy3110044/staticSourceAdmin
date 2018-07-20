<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<script src="js/jquery.js"></script>
<script src="js/jquery.ui.widget.js"></script>
<script src="js/jquery.fileupload.js"></script>
<script src="js/jquery.iframe-transport.js"></script>
<script>
$(document).ready(function(){
	$("#uploadInput").fileupload({
		url : "fileUpload",
		type : "POST",
		dataType : "json",
		add : function(e, data) {
			data.submit();
		},
		progressall : function(e, data) {
			var rate = parseInt(data.loaded / data.total * 100, 10);
		    $("#progressallSpan").html(rate + "%");
		},
		done : function(e, data) {
			if(data.result.code == 100) {
				$("#progressallSpan").html(data.result.msg);
				$("#resultDiv").html(data.result.msg + "：" + data.result.result.serverUrl);
			} else {
			    $("#resultDiv").html(data.result.msg);
			}
		}
	});
});
</script>
</head>
<body>
<div><input id="uploadInput" type="file" name="file">&nbsp;<span id="progressallSpan"></span></div>
<div id="resultDiv"></div>
<!--
<form action="upload" method="post" enctype="multipart/form-data">
<input type="file" name="file"><br>
<input type="submit" value="上传">
</form>
-->
</body>
</html>