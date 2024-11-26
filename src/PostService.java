import java.sql.*;

public class PostService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "secureok";

    // 게시물 등록 메서드
    public void registerPost(Post post) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 게시물 등록 쿼리
            String sql = "INSERT INTO posts (title, content, author, status) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getContent());
            pstmt.setString(3, post.getAuthor());  // 작성자 정보도 저장
            pstmt.setString(4, post.getStatus());

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

    // 게시물 조회 메소드
    public void viewAllPosts() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        // 모든 게시물 조회 쿼리
        String sql = "SELECT * FROM posts";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);

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

    // 게시물 삭제 메서드 (작성자 본인만 가능)
    public void deletePost(int postId, String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 게시물의 작성자가 요청한 사용자인지 확인
            String checkAuthorSql = "SELECT author FROM posts WHERE post_id = ?";
            pstmt = conn.prepareStatement(checkAuthorSql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String author = rs.getString("author");

                if (author.equals(username)) {
                    // 작성자 본인만 삭제할 수 있도록
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
                    System.out.println("삭제 권한이 없습니다. 본인만 삭제할 수 있습니다.");
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

    // 게시물 수정 메서드 (작성자 본인만 가능)
    public void updatePost(int postId, String username, String title, String content, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 게시물의 작성자가 요청한 사용자인지 확인
            String checkAuthorSql = "SELECT author FROM posts WHERE post_id = ?";
            pstmt = conn.prepareStatement(checkAuthorSql);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String author = rs.getString("author");

                if (author.equals(username)) {
                    // 작성자 본인만 수정할 수 있도록
                    String updateSql = "UPDATE posts SET title = ?, content = ?, status = ? WHERE post_id = ?";
                    pstmt = conn.prepareStatement(updateSql);
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
                } else {
                    System.out.println("수정 권한이 없습니다. 본인만 수정할 수 있습니다.");
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
}
