import java.sql.*;
import java.util.Scanner;

public class UserService {
    // 데이터베이스 연결 정보 (URL, 사용자 이름, 비밀번호)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project"; // 데이터베이스 URL
    private static final String DB_USER = "root"; // MySQL 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // MySQL 비밀번호

    // 현재 로그인한 사용자 정보 (static)
    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    // 회원 정보를 데이터베이스에 저장하는 메서드
    public boolean registerUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 1. JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. 데이터베이스에 연결
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 3. SQL 작성
            String sql = "INSERT INTO users (userId, password, name, phone, address, ssn) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            // 4. User 객체에서 데이터 가져와 SQL에 바인딩
            pstmt.setString(1, user.getUserId());  // 사용자 아이디
            pstmt.setString(2, user.getPassword());  // 비밀번호
            pstmt.setString(3, user.getName());      // 이름
            pstmt.setString(4, user.getPhone());     // 연락처
            pstmt.setString(5, user.getAddress());   // 주소
            pstmt.setString(6, user.getSsn());       // 주민번호

            // SQL 실행
            pstmt.executeUpdate();
            return true; // 회원가입 성공
        } catch (SQLException | ClassNotFoundException e) {
            if (e instanceof SQLException && ((SQLException) e).getErrorCode() == 1062) {
                System.out.println("이미 존재하는 회원ID입니다.");
            } else {
                e.printStackTrace();
            }
            return false; // 회원가입 실패
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 로그인 시도 메서드 (아이디와 비밀번호 확인)
    public boolean attemptLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("아이디: ");
        String userId = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        return login(userId, password);
    }

    // 로그인 메서드
    public boolean login(String userId, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM users WHERE userId = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();
            if (rs.next()) { // 로그인 성공
                loggedInUser = new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("ssn")
                );
                return true;
            } else {
                return false; // 로그인 실패
            }
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
        loggedInUser = null; // 로그인된 사용자 정보 제거
        System.out.println("로그아웃되었습니다.");
    }
}
