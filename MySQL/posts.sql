-- 게시판 테이블 생성 쿼리

CREATE TABLE posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,   -- 게시물 ID
    title VARCHAR(255) NOT NULL,              -- 제목
    content TEXT NOT NULL,                    -- 내용
    author VARCHAR(50) NOT NULL,              -- 작성자
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 작성일
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 수정일
    status VARCHAR(20) DEFAULT '공개',        -- 공개/비공개
    FOREIGN KEY (author) REFERENCES users(user_id) -- 작성자와 회원 연결
);
