<!-- register 회원가입 페이지-->
 
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
    </style>
</head>
<body>
    <h2>회원가입</h2>
    <form action="register" method="post">
        <input type="text" name="userId" placeholder="아이디" required><br>
        <input type="password" name="password" placeholder="비밀번호" required><br>
        <input type="text" name="name" placeholder="이름" required><br>
        <input type="text" name="phone" placeholder="연락처" required><br>
        <input type="text" name="address" placeholder="주소" required><br>
        <input type="text" name="ssn" placeholder="주민번호" required><br>
        <button type="submit">가입하기</button>
    </form>
</body>
</html>
