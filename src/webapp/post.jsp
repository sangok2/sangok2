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
            <p>로그인하고 싶으면 버튼을 클릭하세요.</p>
            <form action="login.jsp" method="get">
                <button type="submit">로그인 페이지로 이동</button>
            </form>
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
            </tr>
        </thead>
        <tbody id="postList">
            <!-- 게시물 데이터를 불러오는 JavaScript -->
    <script>
    document.addEventListener("DOMContentLoaded", function () {
        fetch('<%= request.getContextPath() %>/post?action=list')
    .then(response => {
        if (!response.ok) {
            throw new Error("서버 오류 발생: " + response.status);
            }
        return response.json();
        })
    .then(data => {
        const tableBody = document.getElementById("postList");
        tableBody.innerHTML = ""; // 기존 내용 초기화

        if (data.length === 0) {
            tableBody.innerHTML = "<tr><td colspan='6'>게시물이 없습니다.</td></tr>";
            return;
        }

        data.forEach((post, index) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${index + 1}</td>
                <td>${post.title}</td>
                <td>${post.author}</td>
                <td>${post.createdAt}</td>
                <td>${post.status}</td>
            `;
            tableBody.appendChild(row);
            });
        })
    .catch(error => {
        console.error("게시물을 불러오는 중 오류 발생:", error);
        const tableBody = document.getElementById("postList");
        tableBody.innerHTML = "<tr><td colspan='6'>데이터를 불러올 수 없습니다.</td></tr>";
        });
    });
    </script>
        </tbody>
    </table>
</body>
</html>
