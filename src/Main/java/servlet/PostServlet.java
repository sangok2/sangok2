package Main.java.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Post> posts = postService.getAllPosts();
        Gson gson = new Gson();
        String jsonData = gson.toJson(posts);

        PrintWriter out = response.getWriter();
        out.print(jsonData);
        out.flush();
    }

    // 게시물 작성 및 수정 처리
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        String userId = (session != null && session.getAttribute("userId") != null)
                ? (String) session.getAttribute("userId")
                : null;

        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
            return;
        }

        try {
            if ("create".equals(action)) {
                handleCreatePost(request, userId, response);
            } else if ("update".equals(action)) {
                handleUpdatePost(request, userId, response);
            } else if ("delete".equals(action)) {
                handleDeletePost(request, userId, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }

    // 게시물 작성
    private void handleCreatePost(HttpServletRequest request, String userId, HttpServletResponse response)
            throws Exception {
        String title = null, content = null, status = "공개";
        String filePath = null;

        if (ServletFileUpload.isMultipartContent(request)) {
            List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    switch (item.getFieldName()) {
                        case "title": title = item.getString("UTF-8"); break;
                        case "content": content = item.getString("UTF-8"); break;
                        case "status": status = item.getString("UTF-8"); break;
                    }
                } else {
                    if (!item.getName().isEmpty()) {
                        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) uploadDir.mkdir();

                        String fileName = new File(item.getName()).getName();
                        filePath = uploadPath + File.separator + fileName;
                        item.write(new File(filePath));
                    }
                }
            }

            Post post = new Post(title, content, userId, status, filePath);
            postService.createPost(title, content, userId, status, filePath);
            response.sendRedirect(request.getContextPath() + "/post.jsp");
        }
    }

    // 게시물 수정
    private void handleUpdatePost(HttpServletRequest request, String userId, HttpServletResponse response)
            throws Exception {
        int postId = Integer.parseInt(request.getParameter("postId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String status = request.getParameter("status");
        String filePath = request.getParameter("filePath");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        postService.updatePost(postId, title, content, status, userId, filePath);
        response.sendRedirect(request.getContextPath() + "/post.jsp");
    }

    // 게시물 삭제
    private void handleDeletePost(HttpServletRequest request, String userId, HttpServletResponse response)
            throws Exception {
        int postId = Integer.parseInt(request.getParameter("postId"));

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        postService.deletePost(postId, userId);
        response.sendRedirect(request.getContextPath() + "/post.jsp");
    }
}
