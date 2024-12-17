<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
    //세션에서 로그인된 사용자 확인
    HttpSession sessionObj = request.getSession(false);
    String userId = (sessionObj != null) ? (String) sessionObj.getAttribute("userId") : null;
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 20px; 
            text-align: center;
        }
        h1 { 
            color: #333; 
        }
        nav ul {
            list-style: none;
            padding: 0;
            display: flex;
            justify-content: center;
        }
        nav ul li {
            margin: 0 15px;
        }
        nav ul li a {
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
        }
        nav ul li a:hover {
            color: #0056b3;
        }
    </style>
</head>
<body>
    <h1>Welcome to the OKsite</h1>

    <nav>
        <ul>
            <%
                if (userId == null) {
            %>
                <!-- 미로그인 상태 -->
                <li><a href="register.html">회원가입</a></li>
                <li><a href="login.jsp">로그인</a></li>
                <li><a href="board.jsp">게시판</a></li>
            <%
                } else {
            %>
                <!-- 로그인 상태 -->
                <li><a href="logout">로그아웃</a></li>
                <li><a href="board.jsp">게시판</a></li>
            <%
                }
            %>
        </ul>
    </nav>

    <%
        if (userId != null) {
    %>
        <!-- 로그인된 상태의 환영 메시지 -->
        <p>환영합니다, <strong><%= userId %></strong>님!</p>
    <%
        } else {
    %>
        <!-- 미로그인 상태 안내 -->
        <p>사이트의 게시판을 조회할 수 있지만, 게시물을 작성하시려면 <a href="login.jsp">로그인</a> 또는 <a href="register.html">회원가입</a>을 해주세요.</p>
    <%
        }
    %>
</body>
</html>
