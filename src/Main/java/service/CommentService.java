package Main.java.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project?useUnicode=true&characterEncoding=UTF-8"; // DB URL
    private static final String DB_USER = "root"; // DB 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // DB 비밀번호

    // 작성자 확인 메서드 추가
    public boolean isAuthorizedToUpdate(int commentId, String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "SELECT author FROM comments WHERE comment_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, commentId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                String author = rs.getString("author");
                return author.equals(userId) || "admin".equals(userId);
            } // 작성자 본인과 admin 계정은 권한 부여

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
        return false;
    }

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
    public boolean updateComment(int commentId, String content, String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
    
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
            // 댓글 작성자 확인 또는 admin 계정 허용
            String checkAuthorSql = "SELECT author FROM comments WHERE comment_id = ?";
            pstmt = conn.prepareStatement(checkAuthorSql);
            pstmt.setInt(1, commentId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String author = rs.getString("author");
    
                // 권한 확인
                if (!author.equals(userId) && !"admin".equals(userId)) {
                    return false;
                }
            } else {
                System.out.println("해당 댓글이 존재하지 않습니다.");
                return false;
            }
    
            // 댓글 업데이트 SQL
            String updateSql = "UPDATE comments SET content = ?, updated_at = NOW() WHERE comment_id = ?";
            pstmt = conn.prepareStatement(updateSql);
            pstmt.setString(1, content);
            pstmt.setInt(2, commentId);
    
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
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

    // 댓글 삭제
    public boolean deleteComment(int commentId, String userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
    
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
            // 댓글 작성자 확인 또는 admin 계정 허용
            String checkAuthorSql = "SELECT author FROM comments WHERE comment_id = ?";
            pstmt = conn.prepareStatement(checkAuthorSql);
            pstmt.setInt(1, commentId);
            rs = pstmt.executeQuery();
    
            if (rs.next()) {
                String author = rs.getString("author");
    
                // 권한 확인
                if (!author.equals(userId) && !"admin".equals(userId)) {
                    System.out.println("댓글 삭제 권한이 없습니다. userId: " + userId);
                    return false; // 권한 없음
                }
            } else {
                System.out.println("해당 댓글이 존재하지 않습니다. commentId: " + commentId);
                return false; // 댓글 없음
            }
    
            // 댓글 삭제
            String deleteSql = "DELETE FROM comments WHERE comment_id = ?";
            pstmt = conn.prepareStatement(deleteSql);
            pstmt.setInt(1, commentId);
    
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("댓글 삭제 성공: commentId=" + commentId + ", userId=" + userId);
                return true;
            } else {
                System.out.println("댓글 삭제 실패: commentId=" + commentId + ", userId=" + userId);
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
}
