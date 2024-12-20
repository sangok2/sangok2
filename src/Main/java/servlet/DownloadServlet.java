package Main.java.servlet;

import Main.java.service.Post;
import Main.java.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;

public class DownloadServlet extends HttpServlet {

    private PostService postService = new PostService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String postIdParam = request.getParameter("postId");

        // 게시물 ID 검증
        if (postIdParam == null || postIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "게시물 ID가 제공되지 않았습니다.");
            return;
        }

        try {
            int postId = Integer.parseInt(postIdParam);

            // 게시물의 첨부파일 경로 가져오기
            String filePath = postService.getFilePathByPostId(postId);
            System.out.println("Retrieved filePath: " + filePath); // 디버깅용 로그

            if (filePath == null || filePath.trim().isEmpty()) {
                System.out.println("File path is null or empty for postId: " + postId); // 디버깅용 로그
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "첨부파일이 존재하지 않습니다.");
                return;
            }

            File file = new File(filePath);

            // 파일 존재 여부 확인
            if (!file.exists() || file.isDirectory()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일이 존재하지 않거나 다운로드할 수 없습니다.");
                return;
            }

            // 응답 설정
            response.setContentType(getServletContext().getMimeType(file.getName()));
            String encodedFileName = java.net.URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            response.setContentLength((int) file.length());

            // 파일 전송
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "파일 다운로드 중 오류가 발생했습니다.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid postId format: " + postIdParam); // 디버깅용 로그
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "게시물 ID는 숫자여야 합니다.");
        }
    }
}