<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%><!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<style type="text/css">
table,table tr th, table tr td { border:1px solid grey;padding:3px;}
table {border-collapse: collapse;}
</style>
<script src="js/jquery.js"></script>
<script>
var empty = function(str) {
    return str == null || "" == str;
};

var downloadSource = function(){
	var sourceUrl = $.trim($("#sourceUrl").val());
	if(empty(sourceUrl)) {
		alert("请输入下载链接");
		return;
	}
	$.ajax({
		type : "POST",
		cache : false,
		url : "fileServlet",
		dataType : "json",
		data : {
			"action" : "fileDownload",
			"sourceUrl" : sourceUrl
		},
		success : function(data) {
			if(data.code == 100) {
				listAllDownload();
			} else {
				alert(data.msg);
			}
		}
	});
}

var listAllDownload = function(){
	$.ajax({
        type : "POST",
        cache : false,
        url : "fileServlet",
        dataType : "json",
        data : {
        	"action" : "getAllDownload"
        },
        success : function(data) {
            if(data.code == 100) {
            	var list = data.result;
            	var str = '';
            	for(var i=0; i<list.length; i++) {
            		var obj = list[i];
            		str += '<tr class="contentTr">';
            		str += '<td>' + obj.sourceUrl + '</td>';
            		str += '<td>' + obj.downloadStatus + '</td>';
            		str += '<td>' + (obj.filePath == null ? '' : obj.filePath) + '</td>';
            		str += '<td>' + (obj.serverUrl == null ? '' : obj.serverUrl) + '</td>';
            		str += '</tr>';
            	}
            	$("tr.contentTr").remove();
            	$("table").append(str);
            }
        }
    });
}
$(document).ready(function(){
	listAllDownload();
});
</script>
</head>
<body>
<div><input type="text" id="sourceUrl" style="width:600px;">&nbsp;<input type="button" value="下载" onclick="downloadSource()"></div>
<br>
<table>
    <tr>
        <th>资源链接</th>
        <th>下载状态</th>
        <th>文件路径</th>
        <th>url路径</th>
    </tr>
    
</table>
</body>
</html>