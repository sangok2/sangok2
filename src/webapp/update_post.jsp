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
        response.sendRedirect("post.jsp"); // postId가 없으면 게시판으로 리다이렉트
        return;
    }
    // DB에서 게시물의 작성자 확인
    PostService postService = new PostService();
    String postAuthor = postService.getPostAuthor(Integer.parseInt(postId));

    // 작성자가 아니거나 관리자가 아닐 경우 에러 메시지 표시
    if (!userId.equals(postAuthor) && !"admin".equals(userId)) {
        out.println("<script>alert('본인이 작성한 게시물만 수정할 수 있습니다.'); location.href='post.jsp';</script>");
        return;
    }
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시물 수정</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { text-align: center; }
        form { max-width: 600px; margin: auto; }
        label { display: block; margin-top: 10px; }
        input, textarea, select { width: 100%; padding: 8px; margin-top: 5px; }
        button { margin-top: 15px; padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer; }
        button:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <h2>게시물 수정</h2>

    <form action="<%= request.getContextPath() %>/post" method="post">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="postId" value="<%= postId %>">

        <!-- 제목 입력 -->
        <label for="title">제목</label>
        <input type="text" id="title" name="title" required>

        <!-- 내용 입력 -->
        <label for="content">내용</label>
        <textarea id="content" name="content" rows="5" required></textarea>

        <!-- 공개/비공개 상태 선택 -->
        <label for="status">공개 여부</label>
        <select id="status" name="status">
            <option value="공개">공개</option>
            <option value="비공개">비공개</option>
        </select>

        <!-- 수정 완료 버튼 -->
        <button type="submit">수정 완료</button>
    </form>
</body>
</html>
