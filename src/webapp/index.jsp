<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
    // 세션에서 로그인된 사용자 확인
    HttpSession sessionObj = request.getSession(false);
    String userId = (sessionObj != null) ? (String) sessionObj.getAttribute("userId") : null;
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>메인 페이지</title>
</head>
<body>
    <h1>Welcome to the OKsite</h1>

    <!-- userId 확인 로그 출력
    <%
        System.out.println("index.jsp에서 세션 userId: " + userId);
    %>
    <p>세션에 저장된 userId: <strong><%= (userId != null) ? userId : "세션에 userId 없음" %></strong></p>
    -->

    <nav>
        <ul>
            <%
                if (userId == null) {
            %>
                <!-- 미로그인 상태 -->
                <li><a href="register.html">회원가입</a></li>
                <li><a href="login.jsp">로그인</a></li>
                <li><a href="post.jsp">게시판</a></li>
            <%
                } else {
            %>
                <!-- 로그인 상태 -->
                <li><a href="logout">로그아웃</a></li>
                <li><a href="post.jsp">게시판</a></li>
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
        <p>로그인 또는 회원가입 후 더 많은 기능을 이용할 수 있습니다.</p>
    <%
        }
    %>
</body>
</html>
