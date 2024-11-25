public class User {
    private int userId;         // 사용자 ID
    private String name;        // 이름
    private String phone;       // 연락처
    private String password;    // 비밀번호 (암호화된 형태로 저장)
    private String address;     // 주소
    private String ssn;         // 주민번호

    // 생성자
    public User(String name, String phone, String password, String address, String ssn) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.ssn = ssn;
    }

    // Getter와 Setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }
}
