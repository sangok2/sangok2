public class Post {
    private int postId;         // 게시물 ID
    private String title;       // 제목
    private String content;     // 내용
    private String author;      // 작성자
    private String createdAt;   // 작성일
    private String updatedAt;   // 수정일
    private String status;      // 공개/비공개 상태
    private String filePath;    // 파일 경로 (선택적)

    // 생성자 (게시물 등록 시 사용)
    public Post(String title, String content, String author, String status, String filePath) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.status = status;
        this.filePath = filePath;
    }

    // 생성자 (DB에서 데이터를 가져올 때 사용)
    public Post(int postId, String title, String content, String author, 
                String createdAt, String updatedAt, String status, String filePath) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.filePath = filePath;
    }

    // Getter 및 Setter
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
