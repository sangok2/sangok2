import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        try {
            // 사용자 입력 받기
            System.out.println("회원가입을 진행합니다.");
            System.out.print("이름: ");
            String name = scanner.nextLine();
            System.out.print("연락처: ");
            String phone = scanner.nextLine();
            System.out.print("비밀번호: ");
            String password = scanner.nextLine();
            System.out.print("주소: ");
            String address = scanner.nextLine();
            System.out.print("주민번호: ");
            String ssn = scanner.nextLine();

            // User 객체 생성
            User newUser = new User(name, phone, password, address, ssn);

            // 회원가입 처리
            userService.registerUser(newUser);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
