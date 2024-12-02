-- 회원가입 테이블 생성 쿼리

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,      -- 자동 증가하는 고유 ID
    userId VARCHAR(50) NOT NULL UNIQUE,     -- 사용자 ID (유니크 제약)
    password VARCHAR(255) NOT NULL,         -- 비밀번호
    name VARCHAR(100) NOT NULL,             -- 이름
    phone VARCHAR(15) NOT NULL,             -- 연락처
    address TEXT,                           -- 주소
    ssn VARCHAR(20) NOT NULL                -- 주민번호
);