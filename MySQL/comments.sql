CREATE TABLE comments (
comment_id INT AUTO_INCREMENT PRIMARY KEY,   -- 댓글 ID (자동 증가)
post_id INT,                                 -- 댓글이 속한 게시글 ID
user_id INT,                                 -- 댓글 작성자 ID (users 테이블의 user_id)
content TEXT NOT NULL,                       -- 댓글 내용
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 댓글 작성 일시
FOREIGN KEY (post_id) REFERENCES posts(post_id),   -- posts 테이블과 연결
FOREIGN KEY (user_id) REFERENCES users(user_id)    -- users 테이블과 연결
);