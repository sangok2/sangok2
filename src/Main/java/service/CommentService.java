package Main.java.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project?useUnicode=true&characterEncoding=UTF-8"; // DB URL
    private static final String DB_USER = "root"; // DB 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // DB 비밀번호

    // 특정 게시물의 댓글 가져오기
    public List<Comment> getCommentsByPostId(int postId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE post_id = ? AND is_deleted = 0";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment(
                        rs.getInt("comment_id"),
                        rs.getInt("post_id"),
                        rs.getString("author"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toString(),  // 수정된 부분
                        rs.getTimestamp("updated_at").toString(),  // 수정된 부분
                        rs.getBoolean("is_deleted")
                    );
                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    // 댓글 작성
    public boolean createComment(int postId, String author, String content) {
        String sql = "INSERT INTO comments (post_id, author, content) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, postId);
            pstmt.setString(2, author);
            pstmt.setString(3, content);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 댓글 수정
    public boolean updateComment(int commentId, String content, String author) {
        String sql = "UPDATE comments SET content = ?, updated_at = CURRENT_TIMESTAMP WHERE comment_id = ? AND author = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, content);
            pstmt.setInt(2, commentId);
            pstmt.setString(3, author);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 댓글 삭제
    public boolean deleteComment(int commentId, String author) {
        String sql = "UPDATE comments SET is_deleted = 1 WHERE comment_id = ? AND author = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, commentId);
            pstmt.setString(2, author);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
