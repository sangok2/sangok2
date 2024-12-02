-- 댓글 테이블 생성 쿼리

CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,   -- 댓글 ID (자동 증가)
    post_id INT NOT NULL,                        -- 댓글이 속한 게시글 ID
    user_id INT NOT NULL,                        -- 댓글 작성자 ID (users 테이블의 id)
    content TEXT NOT NULL,                       -- 댓글 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 댓글 작성 일시
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE, -- 게시글 삭제 시 관련 댓글도 삭제
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE       -- 회원 삭제 시 관련 댓글도 삭제
);