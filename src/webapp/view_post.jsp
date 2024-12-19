<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Main.java.service.PostService" %>
<%@ page import="Main.java.service.Post" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    // 세션에서 로그인된 사용자 확인
    HttpSession sessionObj = request.getSession(false);
    String userId = (sessionObj != null) ? (String) sessionObj.getAttribute("userId") : null;

    int postId = Integer.parseInt(request.getParameter("postId")); // 전달받은 게시물 ID
    PostService postService = new PostService();
    Post post = postService.getPostById(postId); // 특정 게시물 정보를 가져오는 메서드

    // 비공개 게시물 접근 제한
    if ("비공개".equals(post.getStatus()) && (userId == null || !userId.equals(post.getAuthor()))) {
%>
    <script>
        alert("접근 권한이 없습니다.");
        location.href = "post.jsp";
    </script>
<%
        return;
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시물 상세보기</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { text-align: center; }
        .container { max-width: 600px; margin: auto; }
        .info { margin-top: 20px; }
        .info label { font-weight: bold; display: block; margin-top: 10px; }
        .actions { margin-top: 20px; text-align: center; }
        button { padding: 10px 20px; margin: 5px; border: none; background-color: #007bff; color: white; cursor: pointer; border-radius: 5px; }
        button:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <h2>게시물 상세보기</h2>

    <div class="container">
        <div class="info">
            <label>제목:</label>
            <p><%= post.getTitle() %></p>

            <label>내용:</label>
            <p><%= post.getContent() %></p>

            <label>작성자:</label>
            <p><%= post.getAuthor() %></p>

            <label>작성일:</label>
            <p><%= post.getCreatedAt() %></p>

            <label>상태:</label>
            <p><%= post.getStatus() %></p>
        </div>

        <div class="actions">
            <button onclick="location.href='update_post.jsp?postId=<%= post.getPostId() %>'">수정</button>
            <button onclick="location.href='delete_post.jsp?postId=<%= post.getPostId() %>'">삭제</button>
            <button onclick="location.href='post.jsp'">목록으로</button>
        </div>
    </div>
</body>
</html>
