<%@ page import="com.tictactoe.Sign" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link href="static/main.css" rel="stylesheet">
    <title>Tic-Tac-Toe</title>
</head>
<body>
<h1 class="mainHeader">Tic-Tac-Toe</h1>

<table>
    <tr>
        <td class="td" onclick="window.location='/logic?click=0'">${data.get(0).getSign()}</td>
        <td class="td" onclick="window.location='/logic?click=1'">${data.get(1).getSign()}</td>
        <td class="td" onclick="window.location='/logic?click=2'">${data.get(2).getSign()}</td>
    </tr>
    <tr>
        <td class="td" onclick="window.location='/logic?click=3'">${data.get(3).getSign()}</td>
        <td class="td" onclick="window.location='/logic?click=4'">${data.get(4).getSign()}</td>
        <td class="td" onclick="window.location='/logic?click=5'">${data.get(5).getSign()}</td>
    </tr>
    <tr>
        <td class="td" onclick="window.location='/logic?click=6'">${data.get(6).getSign()}</td>
        <td class="td" onclick="window.location='/logic?click=7'">${data.get(7).getSign()}</td>
        <td class="td" onclick="window.location='/logic?click=8'">${data.get(8).getSign()}</td>
    </tr>
</table>

<hr>
<form action="restart" method="post">
    <button type="submit"><img src="img/restart.png" alt="Restart button" width="163" height="76"></button>
</form>

<c:set var="CROSSES" value="${Sign.CROSS}"/>
<c:set var="NOUGHTS" value="${Sign.NOUGHT}"/>

<c:if test="${winner == CROSSES}">
    <h1 class="messageHeader">CROSSES WIN!</h1>
    <script type="text/javascript">
        <%@include file="static/main.js"%>
        removeOnclickFromTd();
    </script>
</c:if>
<c:if test="${winner == NOUGHTS}">
    <h1 class="messageHeader">NOUGHTS WIN!</h1>
    <script type="text/javascript">
        <%@include file="static/main.js"%>
        removeOnclickFromTd();
    </script>
</c:if>
<c:if test="${draw}">
    <h1 class="messageHeader">STANDOFF!</h1>
    <script type="text/javascript">
        <%@include file="static/main.js"%>
        removeOnclickFromTd();
    </script>
</c:if>
<hr>

<script type="text/javascript">
    <%@include file="static/main.js"%>
</script>



</body>
</html>