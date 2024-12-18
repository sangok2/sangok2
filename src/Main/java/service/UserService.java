package Main.java.service;

import java.sql.*;
import java.util.Scanner;

public class UserService {
    // 데이터베이스 연결 정보 (URL, 사용자 이름, 비밀번호)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project"; // 데이터베이스 URL
    private static final String DB_USER = "root"; // MySQL 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // MySQL 비밀번호

    // 현재 로그인한 사용자 정보 (static)
    private static User loggedInUser;
    
    // 로그인된 사용자 정보를 반환
    public static User getLoggedInUser() {
        return loggedInUser;
    }
    
    //로그아웃 기능
    public void logout() {
        if (loggedInUser != null) {
            loggedInUser = null;
            System.out.println("로그아웃되었습니다.");
        } else {
            System.out.println("현재 로그인된 사용자가 없습니다.");
        }
    }
    // 로그인이 안되어 있으면 입력값을 DB값과 확인해서 로그인
    public boolean attemptLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("아이디: ");
        String userId = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM users WHERE userId = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId.trim());
            pstmt.setString(2, password.trim());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                loggedInUser = new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("ssn")
                );
                System.out.println("로그인 성공!");
                return true;
            } else {
                System.out.println("로그인 실패. 아이디 또는 비밀번호를 확인하세요.");
                return false;
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

    // 회원 정보를 데이터베이스에 저장하는 메서드
    public boolean registerUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String checkSql = "SELECT userId FROM users WHERE userId = ?";
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, user.getUserId());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("이미 존재하는 사용자 ID입니다. 회원가입이 불가능합니다.");
                return false;
            }

            String sql = "INSERT INTO users (userId, password, name, phone, address, ssn) VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, user.getSsn());

            pstmt.executeUpdate();
            System.out.println("회원가입이 성공적으로 완료되었습니다!");
            return true;
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


    // 사용자 비밀번호를 찾는 메서드 (취약점!_SQL Injection)
    public String findPassword(String userId, String name, String ssn) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();

            // SQL Injection 취약점을 유발할 수 있는 쿼리
            String query = "SELECT password FROM users WHERE userId = '" + userId +
                           "' AND name = '" + name + "' AND ssn = '" + ssn + "'";
            rs = stmt.executeQuery(query);

            if (rs.next()) {
                return rs.getString("password"); // 비밀번호를 평문으로 반환(취약점!_중요정보 평문 노출)
            } else {
                return "사용자를 찾을 수 없습니다.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "에러 발생";
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 사용자 로그인 메서드
    public boolean login(String userId, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 공백 제거한 입력값 로그 출력(디버그 내용 보기)
            System.out.println("로그인 시도: userId=" + userId + ", password=" + password.trim());
    
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT * FROM users WHERE userId = ? AND password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId.trim()); // 공백 제거
            pstmt.setString(2, password.trim()); // 공백 제거
    
            // SQL 실행 전 로그 출력
            System.out.println("SQL 실행: " + sql + " (userId=" + userId + ", password=" + password.trim() + ")");
    
            rs = pstmt.executeQuery();
            
            // 결과 확인
            if (rs.next()) {
                System.out.println("로그인 성공: userId=" + rs.getString("userId")); // 로그 출력
                loggedInUser = new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("ssn")
                );
                return true; // 로그인 성공
            } else {
                System.out.println("로그인 실패: 입력값과 DB 값이 일치하지 않습니다.");
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
}
