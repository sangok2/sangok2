import java.util.Scanner;

public class Main_user {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 회원가입을 위한 정보 입력 받기
        System.out.println("회원가입을 진행합니다.");

        System.out.print("사용자 아이디: ");
        String username = scanner.nextLine();  // 사용자 아이디 입력 받기

        System.out.print("비밀번호: ");
        String password = scanner.nextLine();  // 비밀번호 입력 받기

        System.out.print("비밀번호 확인: ");
        String confirmPassword = scanner.nextLine();  // 비밀번호 확인 입력 받기

        // 비밀번호 확인
        if (!password.equals(confirmPassword)) {
            System.out.println("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return; // 비밀번호 불일치 시 회원가입 중단
        }

        System.out.print("이름: ");
        String name = scanner.nextLine();

        System.out.print("연락처: ");
        String phone = scanner.nextLine();

        System.out.print("주소: ");
        String address = scanner.nextLine();

        System.out.print("주민번호: ");
        String ssn = scanner.nextLine();

        // User 객체 생성
        User newUser = new User(username, password, confirmPassword, name, phone, address, ssn);

        // UserService 객체를 생성하여 회원가입 실행
        UserService userService = new UserService();
        userService.registerUser(newUser);  // 회원가입
    }
}

