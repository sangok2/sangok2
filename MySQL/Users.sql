CREATE TABLE users (
user_id INT AUTO_INCREMENT PRIMARY KEY,     -- 사용자 고유 ID (자동 증가)
username VARCHAR(50) NOT NULL,               -- 사용자 아이디
password VARCHAR(255) NOT NULL,              -- 비밀번호 (암호화된 값)
name VARCHAR(100),                           -- 성명
contact VARCHAR(20),                         -- 연락처
address TEXT,                                -- 주소
ssn VARCHAR(20),                            -- 주민등록번호 (선택 사항)
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 가입일시
UNIQUE (user_id)                            -- 중복 방지
);