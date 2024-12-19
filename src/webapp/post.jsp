<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>

<%
    // 세션에서 로그인된 사용자 확인
    HttpSession sessionObj = request.getSession(false); // 세션 객체를 가져옴
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
        td {
            color: black !important;
            visibility: visible !important;
            display: table-cell !important;
            opacity: 1 !important;
        }
    </style>
    
</head>
<body>
    <h2>게시판</h2>

    <!-- 게시물 작성 버튼 -->
    <div class="action-buttons">
        <%
            if (userId != null) { // 로그인된 사용자
        %>
            <button onclick="location.href='<%= request.getContextPath() %>/index.jsp'">메인홈</button>
            <button onclick="location.href='<%= request.getContextPath() %>/create_post.jsp'">게시물 작성</button>
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

    <!-- 검색 폼 -->
    <form id="searchForm" method="post">
        <input type="text" id="keyword" name="keyword" placeholder="검색어를 입력하세요" required>
        <select id="condition" name="condition">
            <option value="제목">제목</option>
            <option value="내용">내용</option>
            <option value="제목+내용">제목+내용</option>
            <option value="작성자">작성자</option>
            <option value="작성자+내용">작성자+내용</option>
            <option value="제목+작성자">제목+작성자</option>
            <option value="제목+작성자+내용">제목+작성자+내용</option>
        </select>
        <button type="submit">검색</button>
    </form>

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
        <tbody id="postList"></tbody>
    </table>

    <!-- 게시물 데이터를 불러오는 JavaScript -->
    <script>
    document.addEventListener("DOMContentLoaded", function () {
        const postList = document.getElementById("postList");

        // 테이블 업데이트 함수
        function updateTable(data) {
            postList.innerHTML = ""; // 기존 내용 초기화

            if (data.length === 0) {
                postList.innerHTML = "<tr><td colspan='5'>게시물이 없습니다.</td></tr>";
                return;
            }

            for (let i = 0; i < data.length; i++) {
                const row = document.createElement("tr");
                row.innerHTML +=  '<td>' + (i + 1)  + '</td>';
                row.innerHTML +=  '<td><a href="view_post.jsp?postId=' + data[i].postId + '">' + data[i].title + '</a></td>';
                row.innerHTML +=  '<td>' + data[i].author + '</td>';
                row.innerHTML +=  '<td>' + data[i].createdAt + '</td>';
                row.innerHTML +=  '<td>' + data[i].status + '</td>';
                postList.appendChild(row);
            }
        }

        // 게시물 목록 로드
        function loadPosts() {
            fetch('<%= request.getContextPath() %>/post?action=list')
                .then(response => {
                    if (!response.ok) throw new Error("서버 오류 발생: " + response.status);
                    return response.json();
                })
                .then(updateTable)
                .catch(error => {
                    console.error("게시물 로드 중 오류 발생:", error);
                    postList.innerHTML = "<tr><td colspan='5'>게시물을 불러오는 중 오류가 발생했습니다.</td></tr>";
                });
        }

        // 검색 요청 처리
        document.getElementById("searchForm").addEventListener("submit", function (event) {
            event.preventDefault();

            const keyword = document.getElementById("keyword").value.trim();
            const condition = document.getElementById("condition").value;

            fetch('<%= request.getContextPath() %>/post?action=search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: "keyword=" + encodeURIComponent(keyword) + "&condition=" + encodeURIComponent(condition)
            })
                .then(response => {
                    if (!response.ok) throw new Error("검색 요청 실패: " + response.status);
                    return response.json();
                })
                .then(updateTable)
                .catch(error => {
                    console.error("검색 요청 중 오류 발생:", error);
                    postList.innerHTML = "<tr><td colspan='5'>검색 중 오류가 발생했습니다.</td></tr>";
                });
        });

        // 초기 게시물 목록 로드
        loadPosts();
    });
    </script>
</body>
</html>
