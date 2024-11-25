CREATE TABLE posts (
post_id INT AUTO_INCREMENT PRIMARY KEY,     -- 게시글 ID (자동 증가)
title VARCHAR(100) NOT NULL,                 -- 게시글 제목
content TEXT NOT NULL,                       -- 게시글 내용
author_id INT,                               -- 작성자 (user_id와 연결)
is_public BOOLEAN DEFAULT TRUE,              -- 게시글 공개 여부 (기본값은 TRUE)
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 작성 일시
FOREIGN KEY (author_id) REFERENCES users(user_id) -- users 테이블과 연결
);