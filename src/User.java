public class User { 
    private String userId;         // 사용자 ID
    private String password;    // 비밀번호
    private String confirmPassword; // 비밀번호 확인
    private String name;        // 이름
    private String phone;       // 연락처
    private String address;     // 주소
    private String ssn;         // 주민등록번호

    // 생성자
    public User(String userId, String password, String confirmPassword, String name, 
                String phone, String address, String ssn) {
        this.userId = userId;      // 사용자 ID
        this.password = password;  // 비밀번호
        this.confirmPassword = confirmPassword; // 비밀번호 확인
        this.name = name;          // 이름
        this.phone = phone;        // 연락처
        this.address = address;    // 주소
        this.ssn = ssn;            // 주민번호
    }

    // Getter(객체 속성값 읽기)와 Setter(객체 속성값 수정)
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
}
