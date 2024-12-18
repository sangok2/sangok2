<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시물 작성</title>
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
    <h2>게시물 작성</h2>

    <form action="<%= request.getContextPath() %>/post" method="post" enctype="multipart/form-data">
        <!-- 제목 입력 -->
        <label for="title">제목</label>
        <input type="text" id="title" name="title" required>

        <!-- 내용 입력 -->
        <label for="content">내용</label>
        <textarea id="content" name="content" rows="5" required></textarea>

        <!-- 공개/비공개 상태 선택(미선택 시 자동 공개) -->
        <label for="status">공개 여부</label>
        <select id="status" name="status">
            <option value="공개">공개</option>
            <option value="비공개">비공개</option>
        </select>

        <!-- 파일 업로드 -->
        <label for="file">파일 업로드</label>
        <input type="file" id="file" name="file">

        <!-- 작성 완료 버튼 -->
        <button type="submit">작성 완료</button>
    </form>
</body>
</html>
