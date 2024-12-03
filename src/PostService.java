import java.sql.*;

public class PostService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "secureok";

    // 모든 게시물을 조회하는 메서드
    public void viewAllPosts() {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

     try {
        // JDBC 드라이버 로드 및 DB 연결
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        // 모든 게시물 조회 SQL 쿼리
        String sql = "SELECT * FROM posts";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);

        // 게시물 출력
        System.out.println("=== 게시물 목록 ===");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("post_id") +
                               ", 제목: " + rs.getString("title") +
                               ", 작성자: " + rs.getString("author") +
                               ", 작성일: " + rs.getString("created_at") +
                               ", 상태: " + rs.getString("status"));
        }
    } catch (ClassNotFoundException | SQLException e) {
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

    // 게시물 등록 메서드
    public void createPost(Post post) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // JDBC 드라이버 로드 및 DB 연결
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 게시물 등록 SQL 쿼리
            String sql = "INSERT INTO posts (title, content, author, status, file_path) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getAuthor());
            pstmt.setString(4, post.getStatus());
            pstmt.setString(5, post.getFilePath());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("게시물 등록 성공!");
            } else {
                System.out.println("게시물 등록 실패!");
            }

        } catch (ClassNotFoundException | SQLException e) {
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

    // 특정 ID로 게시물 조회 메서드
    public Post getPostById(int postId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Post post = null;

        try {
            // JDBC 드라이버 로드 및 DB 연결
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 게시물 조회 SQL 쿼리
            String sql = "SELECT * FROM posts WHERE post_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, postId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                post = new Post(
                    rs.getInt("post_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("author"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"),
                    rs.getString("status"),
                    rs.getString("file_path")
                );
            } else {
                System.out.println("해당 ID의 게시물이 존재하지 않습니다.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return post;
    }

    // 게시물 수정 메서드
    public void updatePost(int postId, String title, String content, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // JDBC 드라이버 로드 및 DB 연결
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 게시물 수정 SQL 쿼리
            String sql = "UPDATE posts SET title = ?, content = ?, status = ? WHERE post_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, status);
            pstmt.setInt(4, postId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("게시물 수정 성공!");
            } else {
                System.out.println("게시물 수정 실패!");
            }

        } catch (ClassNotFoundException | SQLException e) {
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

    // 게시물 삭제 메서드
    public void deletePost(int postId, String author) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // JDBC 드라이버 로드 및 DB 연결
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 게시물 작성자 확인 SQL 쿼리
            String checkAuthorSql = "SELECT author FROM posts WHERE post_id = ?";
            pstmt = conn.prepareStatement(checkAuthorSql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbAuthor = rs.getString("author");

                if (dbAuthor.equals(author)) {
                    // 삭제 SQL 쿼리
                    String deleteSql = "DELETE FROM posts WHERE post_id = ?";
                    pstmt = conn.prepareStatement(deleteSql);
                    pstmt.setInt(1, postId);

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("게시물 삭제 성공!");
                    } else {
                        System.out.println("게시물 삭제 실패!");
                    }
                } else {
                    System.out.println("본인이 작성한 게시물만 삭제할 수 있습니다.");
                }
            } else {
                System.out.println("해당 게시물이 존재하지 않습니다.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 게시물 검색 메서드
    public void searchPosts(String keyword, String condition) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // JDBC 드라이버 로드 및 DB 연결
            Class.forName("com.mysql.cj.jdbc.Driver");
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
                default:
                    sql = "SELECT * FROM posts"; // 기본적으로 모든 게시물 조회
            }

            pstmt = conn.prepareStatement(sql);

            // 검색 키워드 바인딩
            if (condition.equals("제목+내용") || condition.equals("작성자+내용")) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
            } else {
                pstmt.setString(1, "%" + keyword + "%");
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("post_id") +
                                   ", 제목: " + rs.getString("title") +
                                   ", 작성자: " + rs.getString("author") +
                                   ", 작성일: " + rs.getString("created_at") +
                                   ", 상태: " + rs.getString("status"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
