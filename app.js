// app.js - 서버 코드
const express = require('express');
const bodyParser = require('body-parser');
const userRoutes = require('./routes/user');

const app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static('views'));

app.use('/', userRoutes);

app.listen(3000, () => console.log('서버가 3000번 포트에서 실행 중입니다.'));
