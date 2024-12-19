package Main.java.service;

public class Comment {
    private int commentId;      // 댓글 ID
    private int postId;         // 게시물 ID
    private String author;      // 댓글 작성자
    private String content;     // 댓글 내용
    private String createdAt;   // 댓글 생성 시간
    private String updatedAt;   // 댓글 수정 시간
    private boolean isDeleted;  // 삭제 여부

    // 기본 생성자
    public Comment() {}

    // 매개변수를 받는 생성자
    public Comment(int commentId, int postId, String author, String content, String createdAt, String updatedAt, boolean isDeleted) {
        this.commentId = commentId;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }

    // Getter와 Setter
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // toString 메서드 (디버깅 용도)
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", postId=" + postId +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
