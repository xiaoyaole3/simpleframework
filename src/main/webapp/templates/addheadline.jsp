<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2022/1/6
  Time: 16:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>addheadline</title>
</head>
<body>
    <table>
        <h2 style="color: red">表单提交：</h2>
        <form id="headlineinfo" method="post" action="/simpleframework/headline/add">
            头条说明：<input type="text" name="lineName"> <br>
            头条链接：<input type="text" name="lineLink"> <br>
            头条图片地址：<input type="text" name="lineImg"> <br>
            优先级：<input type="text" name="priority"> <br>
            结果：<h3>状态码： ${result.code} 信息： ${result.msg}</h3> <br>
            <input type="submit" value="提交">
        </form>
    </table>
</body>
</html>
