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
			var results = data.result;
			var result = results[0];
			if(result.code == 100) {
				$("#progressallSpan").html(result.msg);
				$("#resultDiv").html(result.msg + "：" + result.result.serverUrl);
			} else {
			    $("#resultDiv").html(result.msg);
			}
		}
	});
});
</script>
</head>
<body>
<div><input id="uploadInput" type="file" name="file">&nbsp;<span id="progressallSpan"></span></div>
<div id="resultDiv"></div>
<div><a href="downloadQueue.jsp" target="_blank">执行一个下载任务</a></div>
</body>
</html>