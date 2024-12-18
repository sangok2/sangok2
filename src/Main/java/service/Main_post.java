package Main.java.service;

import java.util.Scanner;
import java.io.File;

public class Main_post {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PostService postService = new PostService();
        UserService userService = new UserService();

        while (true) {
            System.out.println("=== 게시판 메뉴 ===");
            System.out.println("1. 게시물 목록 조회");
            System.out.println("2. 게시물 검색");
            System.out.println("3. 게시물 작성"); // 로그인이 필요
            System.out.println("4. 게시물 수정"); // 로그인이 필요
            System.out.println("5. 게시물 삭제"); // 로그인이 필요
            System.out.println("6. 로그아웃");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 정리

            switch (choice) {
                case 1: // 게시물 목록 조회
                    postService.viewAllPosts();
                    break;

                case 2: // 게시물 검색
                    System.out.print("검색 키워드: ");
                    String keyword = scanner.nextLine();
                    System.out.print("검색 조건 (제목/내용/작성자/제목+내용/작성자+내용): ");
                    String condition = scanner.nextLine();
                    String userIdForSearch = (UserService.getLoggedInUser() != null) ? UserService.getLoggedInUser().getUserId() : "guest";
                    postService.searchPosts(keyword, condition, userIdForSearch);
                    break;

                case 3: // 게시물 작성 (로그인 필요)
                    if (!checkLogin(userService)) break; // 로그인 체크
                    System.out.print("제목: ");
                    String title = scanner.nextLine();
                    System.out.print("내용: ");
                    String content = scanner.nextLine();
                    System.out.print("파일 경로 (선택, 없으면 Enter): ");
                    String filePath = scanner.nextLine().trim();

                    // 파일 유효성 검사
                    if (!filePath.isEmpty()) {
                        File file = new File(filePath);
                        if (!file.exists() || (!filePath.endsWith(".txt") && !filePath.endsWith(".jpg") && !filePath.endsWith(".jsp"))) {
                            System.out.println("유효하지 않은 파일입니다. 텍스트(.txt), 이미지(.jpg), JSP(.jsp) 파일만 허용됩니다.");
                            break;
                        }
                    }

                    postService.createPost(title, content, UserService.getLoggedInUser().getUserId(), "공개", filePath.isEmpty() ? null : filePath);
                    break;

                case 4: // 게시물 수정 (로그인 필요)
                    if (!checkLogin(userService)) break; // 로그인 체크
                    System.out.print("수정할 게시물 ID: ");
                    int postIdToEdit = scanner.nextInt();
                    scanner.nextLine(); // 버퍼 정리

                    System.out.print("새 제목: ");
                    String newTitle = scanner.nextLine();
                    System.out.print("새 내용: ");
                    String newContent = scanner.nextLine();
                    System.out.print("공개 여부 (공개/비공개): ");
                    String newStatus = scanner.nextLine();
                    System.out.print("새 파일 경로 (선택, 없으면 Enter): ");
                    String newFilePath = scanner.nextLine().trim();

                    postService.updatePost(postIdToEdit, newTitle, newContent, newStatus, UserService.getLoggedInUser().getUserId(), newFilePath.isEmpty() ? null : newFilePath);
                    break;

                case 5: // 게시물 삭제 (로그인 필요)
                    if (!checkLogin(userService)) break; // 로그인 체크
                    System.out.print("삭제할 게시물 ID: ");
                    int postIdToDelete = scanner.nextInt();
                    scanner.nextLine(); // 버퍼 정리

                    postService.deletePost(postIdToDelete, UserService.getLoggedInUser().getUserId());
                    break;

                case 6: // 로그아웃
                    if (UserService.getLoggedInUser() != null) {
                        userService.logout();
                        System.out.println("로그아웃되었습니다.");
                    } else {
                        System.out.println("로그인 상태가 아닙니다.");
                    }
                    break;

                case 0: // 종료
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    return;

                default:
                    System.out.println("올바른 메뉴를 선택하세요.");
            }
        }
    }

    // 로그인 상태 확인 메서드
    private static boolean checkLogin(UserService userService) {
        if (UserService.getLoggedInUser() == null) {
            System.out.println("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
            return userService.attemptLogin(); // 로그인 시도
        }
        return true; // 이미 로그인되어 있음
    }
}
