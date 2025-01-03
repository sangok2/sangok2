package Main.java.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ok_project?useUnicode=true&characterEncoding=UTF-8"; // DB URL
    private static final String DB_USER = "root"; // DB 사용자 이름
    private static final String DB_PASSWORD = "secureok"; // DB 비밀번호


    // 게시물 작성자 가져오기 메서드
    public String getPostAuthor(int postId) {
    String author = null;
    String query = "SELECT author FROM posts WHERE post_id = ?";
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, postId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                author = rs.getString("author"); // 작성자 정보 가져오기
            }
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return author; // 작성자가 없으면 null 반환
    }

    // 게시물 가져오기
    public Post getPostById(int postId, String userId) {
    Post post = null;
    String sql = "SELECT * FROM posts WHERE post_id = ?";
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, postId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String status = rs.getString("status");
                String author = rs.getString("author");

                // 비공개 게시물 접근 제한
                if ("비공개".equals(status) && !(userId != null && (userId.equals(author) || "admin".equals(userId)))) {
                    throw new RuntimeException("비공개 게시물에 대한 접근 권한이 없습니다.");
                }

                    post = new Post(
                        rs.getInt("post_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        author,
                        rs.getString("created_at"),
                        rs.getString("updated_at"),
                        status,
                        rs.getString("file_path")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }
    
    // 게시물 목록 반환 메서드(getAllPost)
    public List<Post> getAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM posts")) {
    
            while (rs.next()) {
                posts.add(new Post(
                    rs.getInt("post_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("author"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"),
                    rs.getString("status"),
                    rs.getString("file_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    //게시물 등록 메서드(createPost)
    public void createPost(String title, String content, String author, String status, String filePath) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // 파일 경로 유효성 검사
            if (filePath != null && !filePath.isEmpty()) {
                String lowerPath = filePath.toLowerCase();
                if (!(lowerPath.endsWith(".txt") || lowerPath.endsWith(".jpg") || lowerPath.endsWith(".png") || lowerPath.endsWith(".jsp"))) {
                    System.out.println("유효하지 않은 파일 확장자입니다. 파일 경로가 저장되지 않습니다.");
                    filePath = null;
                }
            }

            // SQL 쿼리 준비
            String sql = "INSERT INTO posts (title, content, author, status, file_path) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, title);
                pstmt.setString(2, content);
                pstmt.setString(3, author);
                pstmt.setString(4, status);
                pstmt.setString(5, filePath);
                // SQL 실행
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("게시물이 성공적으로 등록되었습니다.");
                } else {
                    System.out.println("게시물 등록에 실패하였습니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 게시물 수정 메서드(updatePost)
    public void updatePost(int postId, String title, String content, String status, String author, String filePath) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // 작성자 확인
            String checkAuthorSql = "SELECT author FROM posts WHERE post_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkAuthorSql)) {
                checkStmt.setInt(1, postId);
                ResultSet rs = checkStmt.executeQuery();

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
            }

            // 파일 확장자 검증
            if (filePath != null && !filePath.isEmpty()) {
                String lowerPath = filePath.toLowerCase();
                if (!(lowerPath.endsWith(".txt") || lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jsp"))) {
                    System.out.println("유효하지 않은 파일 확장자입니다. 파일 경로가 저장되지 않습니다.");
                    filePath = null;
                }
            }

            String updateSql = "UPDATE posts SET title = ?, content = ?, status = ?, file_path = ? WHERE post_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 게시물 삭제(deletePost)
    public void deletePost(int postId, String author) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // 작성자 확인
            String checkAuthorSql = "SELECT author FROM posts WHERE post_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkAuthorSql)) {
                checkStmt.setInt(1, postId);
                ResultSet rs = checkStmt.executeQuery();

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
            }

            String deleteSql = "DELETE FROM posts WHERE post_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
                pstmt.setInt(1, postId);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("게시물이 성공적으로 삭제되었습니다.");
                } else {
                    System.out.println("게시물 삭제에 실패하였습니다.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 게시물 검색(searchPosts)
    public List<Post> searchPosts(String keyword, String condition, String userId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE "; 

    // 검색 조건에 따른 SQL 쿼리
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
            return posts;
    }

    // 비공개 게시물 필터링
    if (!"admin".equals(userId)) {
        sql += " AND (status = '공개' OR author = ?)";
    }

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        int paramIndex = 1;

        //검색 키워드 매핑
        switch (condition) {
            case "제목+내용":
            case "작성자+내용":
            case "제목+작성자":
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                break;
            case "제목+작성자+내용":
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                pstmt.setString(paramIndex++, "%" + keyword + "%");
                break;
            default:
                pstmt.setString(paramIndex++, "%" + keyword + "%");
        }

        // 비공개 게시물 필터링 조건
        if (!"admin".equals(userId)) {
            pstmt.setString(paramIndex, userId);
        }

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                posts.add(new Post(
                    rs.getInt("post_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("author"),
                    rs.getString("created_at"),
                    rs.getString("updated_at"),
                    rs.getString("status"),
                    rs.getString("file_path")
                ));
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return posts;
    }

    // 특정 게시물의 파일 경로 가져오기
    public String getFilePathByPostId(int postId) {
        String sql = "SELECT file_path FROM posts WHERE post_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String filePath = rs.getString("file_path");
                    System.out.println("DB file_path: " + filePath); // 디버깅용 로그
                    return filePath;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}