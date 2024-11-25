// user.js - 회원 관련 라우트
const express = require('express');
const router = express.Router();

router.post('/signup', (req, res) => {
    const { username, email, password } = req.body;
    console.log('회원가입 정보:', { username, email, password });
    res.send('회원가입 완료!');
});

module.exports = router;

