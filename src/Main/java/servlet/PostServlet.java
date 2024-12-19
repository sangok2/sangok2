package Main.java.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Main.java.service.Post;
import Main.java.service.PostService;

import com.google.gson.Gson;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class PostServlet extends HttpServlet {
    private PostService postService = new PostService();

    // 게시물 목록 반환
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("list".equals(action)) {
            handleListPosts(response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    // POST 요청 처리
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String userId = (session != null && session.getAttribute("userId") != null)
                ? (String) session.getAttribute("userId")
                : null;

                try {
                    if ("create".equals(action)) {
                        handleCreatePost(request, userId, response);
                    } else if ("update".equals(action)) {
                        handleUpdatePost(request, userId, response);
                    } else if ("delete".equals(action)) {
                        handleDeletePost(request, userId, response);
                    } else if ("search".equals(action)) {
                        handleSearchPosts(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/post.jsp");
                }
    }

    // 게시물 조회
    private void handleListPosts(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Post> posts = postService.getAllPosts();
        Gson gson = new Gson();
        String jsonData = gson.toJson(posts);

        PrintWriter out = response.getWriter();
        out.print(jsonData);
        out.flush();
    }

    // 게시물 작성
    private void handleCreatePost(HttpServletRequest request, String userId, HttpServletResponse response)
            throws Exception {
        String title = null, content = null, status = "공개";
        String filePath = null;

        if (!ServletFileUpload.isMultipartContent(request)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "폼 데이터가 멀티파트 형식이 아닙니다.");
            return;
        }

        // 메모리 임계값 설정 (기본: 10KB)
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024); // 1MB 제한
        // 임시 파일 저장 디렉토리 설정
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        // 전체 요청 크기 제한 설정
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(10 * 1024 * 1024); // 10MB 제한

        List<FileItem> items = upload.parseRequest(request);

        String uploadPath = "C:/Users/이상옥/apache-tomcat-9.0.98/webapps/OKsite/uploads"; // 업로드 경로
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();
        
            for (FileItem item : items) {
                if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        case "title": title = item.getString("UTF-8"); break;
                        case "content": content = item.getString("UTF-8"); break;
                        case "status": status = item.getString("UTF-8"); break;
                    }
                } else {
                    if (!item.getName().isEmpty()) {
                        String fileName = System.currentTimeMillis() + "_" + new File(item.getName()).getName();
                        filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        item.write(storeFile);
                        System.out.println("파일 업로드 성공: " + filePath);
                    }
                }
            }
            // 필수 필드 확인
            if (title == null || content == null || userId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 필드가 누락되었습니다.");
                return;
            }

            // 게시물 저장
            Post post = new Post(title, content, userId, status, filePath);
            postService.createPost(title, content, userId, status, filePath);
            // 게시물 저장 완료 후 리다이렉트
            response.sendRedirect(request.getContextPath() + "/post.jsp");
        }
    

    // 게시물 수정
    private void handleUpdatePost(HttpServletRequest request, String userId, HttpServletResponse response)
            throws Exception {
        
        // 요청 파라미터 받아오기
        int postId = Integer.parseInt(request.getParameter("postId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String status = request.getParameter("status");
        String filePath = request.getParameter("filePath");
        
        // 미로그인 시 로그인페이지로 리다이렉트
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // 수정 완료 후 게시물페이지로 리다이렉트
        try {
            postService.updatePost(postId, title, content, status, userId, filePath);
            System.out.println("게시물 수정 성공: postId=" + postId);
            response.sendRedirect(request.getContextPath() + "/post.jsp"); // 수정 완료 후 게시판으로 이동
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            response.sendRedirect(request.getContextPath() + "/post.jsp"); // 에러 발생 시 에러 페이지로 이동
        }
    }

    // 게시물 삭제
    private void handleDeletePost(HttpServletRequest request, String userId, HttpServletResponse response)
            throws Exception {
        int postId = Integer.parseInt(request.getParameter("postId"));
        
        // 미로그인 시 로그인페이지로 리다이렉트
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 삭제 완료 후 게시물페이지로 리다이렉트
        postService.deletePost(postId, userId);
        response.sendRedirect(request.getContextPath() + "/post.jsp");
    }

    // 게시물 검색 처리 메서드
    private void handleSearchPosts(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String keyword = request.getParameter("keyword");
        String condition = request.getParameter("condition");
        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        List<Post> posts = postService.searchPosts(keyword, condition, userId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String jsonData = gson.toJson(posts);

        PrintWriter out = response.getWriter();
        out.print(jsonData);
        out.flush();
    }
}

