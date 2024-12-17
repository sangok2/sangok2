<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; text-align: center; }
        .error { color: red; margin-bottom: 10px; }
    </style>
</head>
<body>
    <h1>로그인</h1>

    <%
        // 에러 메시지 표시
        String error = request.getParameter("error");
        if ("true".equals(error)) {
    %>
        <p class="error">로그인에 실패했습니다. 아이디 또는 비밀번호를 확인하세요.</p>
    <%
        }
    %>
    
    <form action="login" method="post">
        <label for="userId">아이디:</label>
        <input type="text" id="userId" name="userId" required><br><br>
        
        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit">로그인</button>
    </form>
</body>
</html>
