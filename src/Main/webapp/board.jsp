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
    <title>게시판</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        h2 { text-align: center; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid #ddd; }
        th, td { padding: 12px; text-align: center; }
        th { background-color: #f2f2f2; }
        a { text-decoration: none; color: #007bff; }
        .action-buttons { margin-top: 10px; text-align: center; }
        button { padding: 8px 12px; margin: 5px; border: none; background-color: #007bff; color: white; cursor: pointer; border-radius: 4px; }
        button:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <h2>게시판</h2>

    <!-- 게시물 작성 버튼 -->
    <div class="action-buttons">
        <%
            if (userId != null) { // 로그인된 사용자
        %>
            <button onclick="location.href='create_post.jsp'">게시물 작성</button>
        <%
            } else { // 미로그인 사용자
        %>
            <p>로그인하면 게시물을 작성할 수 있습니다.</p>
        <%
            }
        %>
    </div>

    <!-- 게시물 목록 테이블 -->
    <table>
        <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>작성일</th>
                <th>상태</th>
                <%
                    if (userId != null) { // 로그인된 사용자만 수정/삭제 표시
                %>
                <th>관리</th>
                <%
                    }
                %>
            </tr>
        </thead>
        <tbody id="postList">
            <!-- JavaScript를 통해 동적으로 게시물 데이터를 불러옵니다 -->
        </tbody>
    </table>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            fetch('/post?action=list') // 서버의 게시물 목록 API 호출
                .then(response => response.json()) // JSON 형식으로 응답 받음
                .then(data => {
                    const tableBody = document.getElementById("postList");
                    data.forEach((post, index) => {
                        const row = document.createElement("tr");

                        let actionButtons = ""; // 수정/삭제 버튼
                        <% if (userId != null) { %>
                            // 로그인한 사용자에게만 수정/삭제 링크 표시
                            if ("<%= userId %>" === post.author) {
                                actionButtons = `
                                    <a href='edit_post.jsp?postId=${post.id}'>수정</a> |
                                    <a href='delete_post.jsp?postId=${post.id}' onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
                                `;
                            }
                        <% } %>

                        row.innerHTML = `
                            <td>${index + 1}</td>
                            <td>${post.title}</td>
                            <td>${post.author}</td>
                            <td>${post.createdAt}</td>
                            <td>${post.status}</td>
                            ${actionButtons ? `<td>${actionButtons}</td>` : ""}
                        `;
                        tableBody.appendChild(row);
                    });
                })
                .catch(error => {
                    console.error("게시물 데이터를 불러오는 중 오류 발생:", error);
                    const tableBody = document.getElementById("postList");
                    tableBody.innerHTML = `<tr><td colspan="6">게시물을 불러올 수 없습니다.</td></tr>`;
                });
        });
    </script>
</body>
</html>
