import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static void main(String[] args) {
        // MySQL 데이터베이스 연결 정보
        String url = "jdbc:mysql://localhost:3306/ok_project";  // DB URL
        String username = "root";  // 사용자 이름
        String password = "secureok";  // 비밀번호 (설치 시 설정한 비밀번호)

        try {
            // MySQL JDBC 드라이버를 통해 연결 생성
            Connection conn = DriverManager.getConnection(url, username, password);

            // 연결 성공 시 메시지 출력
            if (conn != null) {
                System.out.println("DB 연결 성공!");
            }

            // 연결 종료
            conn.close();
        } catch (SQLException e) {
            // 연결 실패 시 에러 메시지 출력
            System.out.println("DB 연결 실패: " + e.getMessage());
        }
    }
}
