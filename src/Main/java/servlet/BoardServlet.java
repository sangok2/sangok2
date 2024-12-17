
package Main.JAVA.servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import PostService;
import Post;

@WebServlet("/board")
public class PostServlet extends HttpServlet {
    private PostService postService = new PostService();

    // GET 요청: 게시물 목록 조회
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("list".equals(action)) { // 게시물 목록 조회
            List<Post> posts = postService.getAllPosts();
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("post.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    // POST 요청: 게시물 작성
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) { // 게시물 작성
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String filePath = request.getParameter("filePath");
            HttpSession session = request.getSession();
            String author = (String) session.getAttribute("userId"); // 로그인된 사용자 ID 가져오기

            if (author == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            postService.createPost(title, content, author, "공개", filePath);
            response.sendRedirect("post?action=list");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
    }

    // PUT 요청: 게시물 수정
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String status = request.getParameter("status");
        String filePath = request.getParameter("filePath");
        HttpSession session = request.getSession();
        String author = (String) session.getAttribute("userId");

        if (author == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        postService.updatePost(postId, title, content, status, author, filePath);
        response.sendRedirect("post?action=list");
    }

    // DELETE 요청: 게시물 삭제
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int postId = Integer.parseInt(request.getParameter("postId"));
        HttpSession session = request.getSession();
        String author = (String) session.getAttribute("userId");

        if (author == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        postService.deletePost(postId, author);
        response.sendRedirect("post?action=list");
    }
}

