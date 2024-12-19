<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="Main.java.service.PostService" %>

<%
    // 세션에서 로그인된 사용자 확인
    HttpSession sessionObj = request.getSession(false); // 세션 객체 선언
    String userId = (sessionObj != null) ? (String) sessionObj.getAttribute("userId") : null;

    // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 요청으로부터 postId 가져오기
    String postId = request.getParameter("postId");
    if (postId == null) {
        response.sendRedirect("post.jsp");
        return;
    }

    // DB에서 게시물의 작성자 확인
    PostService postService = new PostService();
    String postAuthor = postService.getPostAuthor(Integer.parseInt(postId));

    // 작성자가 아니거나 관리자가 아닐 경우 에러 메시지 표시
    if (postAuthor == null || (!userId.equals(postAuthor) && !"admin".equals(userId))) {
    %>
    <script>
        alert("삭제 권한이 없습니다.");
        location.href = "post.jsp";
    </script>
    <%
        return;
    }

    // 삭제 처리
    postService.deletePost(Integer.parseInt(postId), userId);
%>
<script>
    alert("게시물이 성공적으로 삭제되었습니다.");
    location.href = "post.jsp";
</script>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시물 삭제</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { text-align: center; }
        form { max-width: 600px; margin: auto; text-align: center; }
        button { margin: 10px; padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer; }
        button:hover { background-color: #0056b3; }
        .cancel-btn { background-color: #f44336; }
        .cancel-btn:hover { background-color: #d32f2f; }
    </style>
</head>
<body>
    <h2>게시물 삭제</h2>
    <p>이 게시물을 삭제하시겠습니까?</p>
    <form action="<%= request.getContextPath() %>/post" method="post">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="postId" value="<%= postId %>">
        <button type="submit">삭제</button>
        <button type="button" class="cancel-btn" onclick="location.href='post.jsp';">취소</button>
    </form>
</body>
</html>
