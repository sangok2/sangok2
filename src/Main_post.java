import java.util.Scanner;

public class Main_post {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PostService postService = new PostService();  // PostService 인스턴스 생성

        // 로그인 상태 확인
        boolean isLoggedIn = false;
        String loggedInUser = null;  // 로그인한 사용자 이름 저장

        // 간단한 메뉴 구현
        while (true) {
            System.out.println("1. 로그인");
            System.out.println("2. 게시물 등록");
            System.out.println("3. 게시물 조회");
            System.out.println("4. 로그아웃");
            System.out.println("5. 종료");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 버퍼 비우기

            switch (choice) {
                case 1:
                    // 로그인 로직
                    System.out.print("아이디: ");
                    loggedInUser = scanner.nextLine();
                    System.out.print("비밀번호: ");
                    String password = scanner.nextLine();
                    if (login(loggedInUser, password)) {
                        System.out.println("로그인 성공!");
                        isLoggedIn = true;
                    } else {
                        System.out.println("로그인 실패!");
                    }
                    break;

                case 2:
                    if (isLoggedIn) {
                        // 게시물 등록
                        System.out.print("게시물 제목: ");
                        String title = scanner.nextLine();
                        System.out.print("게시물 내용: ");
                        String content = scanner.nextLine();

                        // 새로운 게시물 객체 생성
                        Post newPost = new Post(title, content, loggedInUser, "공개");

                        // 게시물 등록
                        postService.registerPost(newPost);  // PostService의 registerPost 호출
                    } else {
                        System.out.println("로그인 후 게시물을 등록하세요.");
                    }
                    break;

                case 3:
                    // 게시물 조회 로직
                    System.out.println("모든 게시물을 조회합니다.");
                    postService.viewAllPosts();
                    break;

                case 4:
                    System.out.println("로그아웃 되었습니다.");
                    isLoggedIn = false;
                    loggedInUser = null;
                    break;

                case 5:
                    System.out.println("프로그램 종료");
                    scanner.close();
                    return;

                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    // 로그인 메소드 예시
    public static boolean login(String username, String password) {
        // 로그인 로직 구현 (데이터베이스와 비교)
        return true;  // 성공적인 로그인 예시
    }
}
