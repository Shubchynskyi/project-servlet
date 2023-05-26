<%@ page import="com.tictactoe.entity.Sign" %>
<%@ page contentType="text/html;charset=UTF-8" %>
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
    <div class="formContainer">
        <label for="difficulty" class="labelFont18">Select Difficulty:</label>
        <select name="difficulty" id="difficulty" class="selectDifficulty labelFont16">
            <option value="easy" ${selectedDifficulty eq 'easy' ? 'selected' : ''}>Easy</option>
            <option value="medium" ${selectedDifficulty eq 'medium' ? 'selected' : ''}>Medium</option>
            <option value="hard" ${selectedDifficulty eq 'hard' ? 'selected' : ''}>Hard</option>
            <option value="hard" ${selectedDifficulty eq 'god' ? 'selected' : ''}>God</option>
        </select>

        <label for="sign" class="labelFont18">Select Sign:</label>
        <select name="sign" id="sign" class="labelFont16">
            <option value="cross" ${selectedSign eq 'cross' ? 'selected' : ''}>Cross</option>
            <option value="nought" ${selectedSign eq 'nought' ? 'selected' : ''}>Nought</option>
        </select>
    </div>
    <button type="submit" class="newGameButton">New Game</button>
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