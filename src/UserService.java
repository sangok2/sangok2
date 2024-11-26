import java.sql.*;

// UserService 클래스: 사용자 관련 로직을 처리하는 클래스
public class UserService {
    // 데이터베이스 연결 정보 (URL, 사용자 이름, 비밀번호)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project"; // 데이터베이스 URL
    private static final String DB_USER = "root"; // MySQL 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // MySQL 비밀번호

    // 회원 정보를 데이터베이스에 저장하는 메서드
    // User 객체를 매개변수로 받아와서 데이터베이스에 삽입
    public boolean registerUser(User user) {
        Connection conn = null; // 데이터베이스 연결 객체
        PreparedStatement pstmt = null; // SQL 실행 객체

        try {
            // 1. JDBC 드라이버 로드
            // MySQL 드라이버를 메모리에 로드하여 사용할 준비를 함
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); // 예외 로그 출력
                System.out.println("JDBC 드라이버를 로드하지 못했습니다. 프로그램을 종료합니다.");
                return false; // 회원 등록 실패 반환
            }

            // 2. 데이터베이스에 연결
            // DriverManager를 통해 데이터베이스와 연결을 생성
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 3. SQL 작성
            // SQL 쿼리를 미리 작성해 둠 (PreparedStatement로 값을 나중에 대체)
            String sql = "INSERT INTO users (userId, password, confirmPassword, name, phone, address, ssn) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            // 4. User 객체에서 데이터 가져와 SQL에 바인딩
            // ? 부분에 각각의 값을 채워 넣음
            pstmt.setString(1, user.getUserId());  // 첫 번째 ?에 사용자 아이디 입력
            pstmt.setString(2, user.getPassword());  // 두 번째 ?에 비밀번호 입력
            pstmt.setString(2, user.getConfirmPassword());  // 세 번째 ?에 비밀번호 입력
            pstmt.setString(3, user.getName());      // 네 번째 ?에 이름 입력
            pstmt.setString(4, user.getPhone());     // 다섯 번째 ?에 연락처 입력
            pstmt.setString(5, user.getAddress());   // 여섯 번째 ?에 주소 입력
            pstmt.setString(6, user.getSsn());       // 일곱 번째 ?에 주민번호 입력

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("이미 존재하는 회원ID입니다.");
            } else {
                e.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 로그인 메서드
    public boolean login(String userId, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();
            return rs.next(); // 아이디/비밀번호가 일치하면 true 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // 사용자 로그아웃 메서드
    public void logoutUser() {
        // 로그아웃 처리 로직 (세션 종료)
        // 예시에서는 콘솔에서만 처리하지만 실제로는 세션 정보를 관리하는 방식으로 구현
        System.out.println("로그아웃 되었습니다.");
    }
}

