<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="Main.java.service.PostService" %>
<%@ page import="Main.java.service.Post" %>
<%@ page import="Main.java.service.CommentService" %>
<%@ page import="Main.java.service.Comment" %>
<%@ page import="java.util.List" %>

<%
    // 세션에서 로그인된 사용자 확인
    HttpSession sessionObj = request.getSession(false);
    String userId = (sessionObj != null) ? (String) sessionObj.getAttribute("userId") : null;

    // 전달받은 게시물 ID 가져오기
    int postId = Integer.parseInt(request.getParameter("postId"));

    // 게시물 및 댓글 데이터 가져오기
    PostService postService = new PostService();
    Post post = postService.getPostById(postId);

    // 게시물이 없으면 목록 페이지로 이동
    if (post == null) {
        response.sendRedirect("post.jsp"); 
        return;
    }

    CommentService commentService = new CommentService();
    List<Comment> comments = commentService.getCommentsByPostId(postId); 
    // 댓글 가져오기
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
        .comments { margin-top: 30px; }
        .comment { border: 1px solid #ddd; padding: 10px; margin-bottom: 10px; }
        .comment label { font-weight: bold; }
        .comment-form { margin-top: 20px; }
        .comment-form textarea { width: 100%; height: 80px; margin-bottom: 10px; }
        .comment-form button { padding: 10px 20px; background-color: #007bff; color: white; border: none; border-radius: 5px; cursor: pointer; }
        .comment-form button:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <h2>게시물 상세보기</h2>

    <div class="container">
        <!-- 게시물 정보 -->
        <div class="info">
            <label>제목:</label>
            <p><%= post.getTitle() %></p>

            <label>내용:</label>
            <p><%= post.getContent() %></p>

            <label>작성자:</label>
            <p><%= post.getAuthor() %></p>

            <label>작성일:</label>
            <p><%= post.getCreatedAt() %></p>

            <label>수정일:</label>
            <p><%= post.getUpdatedAt() %></p>

            <label>상태:</label>
            <p><%= post.getStatus() %></p>
        
            <%
            // 게시물의 첨부파일 경로
            String filePath = post.getFilePath();
            %>

            <div class="info">
                <label>첨부파일:</label>
                <%
                    if (filePath != null && !filePath.trim().isEmpty()) {
                %>
                <a href="<%= request.getContextPath() %>/download?postId=<%= post.getPostId() %>">
                첨부파일 다운로드</a>
            <%
            } else {
            %>
            <p>첨부파일이 없습니다.</p>
            <%
            }
        %>
            
        </div>
        <div class="actions">
            <button onclick="location.href='update_post.jsp?postId=<%= post.getPostId() %>'">수정</button>
            <button onclick="location.href='delete_post.jsp?postId=<%= post.getPostId() %>'">삭제</button>
            <button onclick="location.href='post.jsp'">목록으로</button>
        </div>

        <!-- 댓글 섹션 -->
        <div class="comments">
            <h3>댓글</h3>
            <%
                if (comments != null && !comments.isEmpty()) {
                    for (Comment comment : comments) {
            %>
                <div class="comment">
                    <label>작성자:</label> <%= comment.getAuthor() %><br>
                    <label>내용:</label> <%= comment.getContent() %><br>
                    <label>작성일:</label> <%= comment.getCreatedAt() %>
                    
                <!-- 댓글 수정/삭제 버튼 (작성자 본인만 표시) -->
                <%
                    if (userId != null && userId.equals(comment.getAuthor())) {
                %>
                    <div class="actions">
                        <form action="<%= request.getContextPath() %>/CommentServlet" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="commentId" value="<%= comment.getCommentId() %>">
                            <textarea name="content" placeholder="수정할 댓글 내용 입력" required></textarea>
                            <button type="submit">수정</button>
                        </form>
                        <form action="<%= request.getContextPath() %>/CommentServlet" method="post" style="display:inline;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="commentId" value="<%= comment.getCommentId() %>">
                            <input type="hidden" name="postId" value="<%= postId %>">
                            <button type="submit">삭제</button>
                        </form>
                    </div>
                <%
                    }
                %>
        </div>
        <%
                    }
                } else {
            %>
                <p>댓글이 없습니다.</p>
            <%
                }
            %>
        </div>

        <!-- 댓글 작성 폼 -->
        <%
            if (sessionObj != null && userId != null) { 
            // 로그인된 사용자만 댓글 작성 가능
        %>
            <div class="comment-form">
                <h3>댓글 작성</h3>
                <form action="<%= request.getContextPath() %>/CommentServlet" method="post">
                    <input type="hidden" name="action" value="create">
                    <input type="hidden" name="postId" value="<%= postId %>">
                    <textarea name="content" placeholder="댓글 내용을 입력하세요..." required></textarea>
                    <button type="submit">작성</button>
                </form>
            </div>
        <%
            } else {
        %>
            <p>댓글을 작성하려면 <a href="<%= request.getContextPath() %>/login.jsp">로그인</a>하세요.</p>
        <%
            }
        %>
    </div>
</body>
</html>
