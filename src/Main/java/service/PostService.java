package Main.java.service;

import java.sql.*;

public class PostService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project"; // DB URL
    private static final String DB_USER = "root"; // DB 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // DB 비밀번호

    public void viewAllPosts() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            stmt = conn.createStatement();
            String sql = "SELECT * FROM posts";
            rs = stmt.executeQuery(sql);

            System.out.println("=== 게시물 목록 ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("post_id") +
                                   ", 제목: " + rs.getString("title") +
                                   ", 작성자: " + rs.getString("author") +
                                   ", 작성일: " + rs.getString("created_at") +
                                   ", 상태: " + rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // (수정부분) 게시물 등록 메서드
    public void createPost(String title, String content, String author, String status, String filePath) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (filePath != null && !filePath.isEmpty()) {
                String lowerPath = filePath.toLowerCase();
                if (!(lowerPath.endsWith(".txt") || lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jsp"))) {
                    System.out.println("유효하지 않은 파일 확장자입니다. 파일 경로가 저장되지 않습니다.");
                    filePath = null;
                }
            }

            String sql = "INSERT INTO posts (title, content, author, status, file_path) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, author);
            pstmt.setString(4, status);
            pstmt.setString(5, filePath);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("게시물이 성공적으로 등록되었습니다.");
            } else {
                System.out.println("게시물 등록에 실패하였습니다.");
            }

        } catch (SQLException e) {
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

    // 게시물 수정(updatePost)
    public void updatePost(int postId, String title, String content, String status, String author, String filePath) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String checkAuthorSql = "SELECT author FROM posts WHERE post_id = ?";
            pstmt = conn.prepareStatement(checkAuthorSql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbAuthor = rs.getString("author");
                if (!dbAuthor.equals(author) && !"admin".equals(author)) {
                    System.out.println("본인이 작성한 게시물만 수정할 수 있습니다.");
                    return;
                }
            } else {
                System.out.println("존재하지 않는 게시물입니다.");
                return;
            }

            if (filePath != null && !filePath.isEmpty()) {
                String lowerPath = filePath.toLowerCase();
                if (!(lowerPath.endsWith(".txt") || lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jsp"))) {
                    System.out.println("유효하지 않은 파일 확장자입니다. 파일 경로가 저장되지 않습니다.");
                    filePath = null;
                }
            }

            String updateSql = "UPDATE posts SET title = ?, content = ?, status = ?, file_path = ? WHERE post_id = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, status);
            pstmt.setString(4, filePath);
            pstmt.setInt(5, postId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("게시물이 성공적으로 수정되었습니다.");
            } else {
                System.out.println("게시물 수정에 실패하였습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

    // 게시물 삭제(deletePost)
    public void deletePost(int postId, String author) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String checkAuthorSql = "SELECT author FROM posts WHERE post_id = ?";
            pstmt = conn.prepareStatement(checkAuthorSql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbAuthor = rs.getString("author");
                if (!dbAuthor.equals(author) && !"admin".equals(author)) {
                    System.out.println("본인이 작성한 게시물만 삭제할 수 있습니다.");
                    return;
                }
            } else {
                System.out.println("존재하지 않는 게시물입니다.");
                return;
            }

            String deleteSql = "DELETE FROM posts WHERE post_id = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setInt(1, postId);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("게시물이 성공적으로 삭제되었습니다.");
            } else {
                System.out.println("게시물 삭제에 실패하였습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 게시물 검색(searchPosts)
    public void searchPosts(String keyword, String condition, String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 검색 조건에 따른 SQL 쿼리
            String sql = "SELECT * FROM posts WHERE ";
            switch (condition) {
                case "제목":
                    sql += "title LIKE ?";
                    break;
                case "내용":
                    sql += "content LIKE ?";
                    break;
                case "제목+내용":
                    sql += "(title LIKE ? OR content LIKE ?)";
                    break;
                case "작성자":
                    sql += "author LIKE ?";
                    break;
                case "작성자+내용":
                    sql += "(author LIKE ? OR content LIKE ?)";
                    break;
                case "제목+작성자":
                    sql += "(title LIKE ? OR author LIKE ?)";
                    break;
                case "제목+작성자+내용":
                    sql += "(title LIKE ? OR author LIKE ? OR content LIKE ?)";
                    break;
                default:
                    System.out.println("잘못된 검색 조건입니다.");
                    return;
            }

            if (!"admin".equals(userId)) {
                sql += " AND (status = '공개' OR author = ?)"; // 비공개 게시물은 본인만 조회 가능
            }

            pstmt = conn.prepareStatement(sql);

            // 검색 키워드 바인딩
            int paramIndex = 1;
            if (condition.equals("제목+내용") || condition.equals("작성자+내용") || condition.equals("제목+작성자")) {
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                pstmt.setString(paramIndex++, "%" + keyword + "%");
            } else if (condition.equals("제목+작성자+내용")) {
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                pstmt.setString(paramIndex++, "%" + keyword + "%");
            } else {
                pstmt.setString(paramIndex++, "%" + keyword + "%");
            }

            if (!"admin".equals(userId)) {
                pstmt.setString(paramIndex, userId);
            }

            rs = pstmt.executeQuery();

            // 검색 결과 출력
            System.out.println("=== 검색 결과 ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("post_id") +
                                   ", 제목: " + rs.getString("title") +
                                   ", 작성자: " + rs.getString("author") +
                                   ", 작성일: " + rs.getString("created_at") +
                                   ", 상태: " + rs.getString("status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
