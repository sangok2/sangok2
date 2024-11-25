import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserService {
    // 데이터베이스 연결 정보
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project?serverTimezone=UTC";
    private static final String DB_USER = "root"; // MySQL 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // MySQL 비밀번호

    public void registerUser(String name, String phone, String password, String address, String ssn) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // **JDBC 드라이버 로드 추가**
            Class.forName("com.mysql.cj.jdbc.Driver"); // 드라이버 로드

            // 데이터베이스 연결
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("DB 연결 성공!");

            // SQL 문 작성
            String sql = "INSERT INTO users (name, phone, password, address, ssn) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            // 파라미터 바인딩
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, password); // 암호화된 비밀번호 전달
            pstmt.setString(4, address);
            pstmt.setString(5, ssn);

            // 실행
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("회원가입 성공!");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC 드라이버를 로드할 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("DB 작업 중 오류 발생!");
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
