package Main.java.servlet;

import Main.java.service.Comment;
import Main.java.service.CommentService;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class CommentServlet extends HttpServlet {
    private CommentService commentService = new CommentService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null && session.getAttribute("userId") != null)
                ? (String) session.getAttribute("userId")
                : null;

        // 로그인 여부 확인
        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        try {
            if ("create".equals(action)) {
                handleCreateComment(request, userId, response);
            } else if ("update".equals(action)) {
                handleUpdateComment(request, userId, response);
            } else if ("delete".equals(action)) {
                handleDeleteComment(request, userId, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 에러 발생 시 게시물 상세 페이지로 리다이렉트
            String postId = request.getParameter("postId");
            if (postId != null) {
                response.sendRedirect("view_post.jsp?postId=" + postId);
            } else {
                response.sendRedirect("post.jsp"); // 기본적으로 게시판으로 리다이렉트
            }
        }
    }

    // 댓글 작성 기능(로그인한 사용자만 가능)
    private void handleCreateComment(HttpServletRequest request, String userId, HttpServletResponse response) throws IOException {
        String postIdParam = request.getParameter("postId");
        String content = request.getParameter("content");

        // 필수 파라미터 검증
        if (postIdParam == null || content == null || content.trim().isEmpty()) {
            System.err.println("필수 파라미터 누락: postId=" + postIdParam + ", content=" + content);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 파라미터가 누락되었습니다.");
        return;
        }

        try {
            // postId를 숫자로 변환
            int postId = Integer.parseInt(postIdParam);

            // 댓글 작성 시도
            boolean success = commentService.createComment(postId, userId, content);

            if (success) {
                System.out.println("댓글 작성 성공: postId=" + postId + ", userId=" + userId);
                // 성공 시 게시물 상세 페이지로 리다이렉트
                response.sendRedirect("view_post.jsp?postId=" + postId);
            } else {
                System.err.println("댓글 작성 실패: postId=" + postId + ", userId=" + userId);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "댓글 작성에 실패했습니다.");
            }
        } catch (NumberFormatException e) {
            // postId 변환 실패
            System.err.println("postId 파싱 오류: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "postId는 숫자여야 합니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("댓글 작성 중 예외 발생: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    // 댓글 수정 기능(작성자 본인만 가능)
    private void handleUpdateComment(HttpServletRequest request, String userId, HttpServletResponse response) throws IOException {
        String commentIdParam = request.getParameter("commentId");
        String content = request.getParameter("content");

        if (commentIdParam == null || content == null || content.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 파라미터가 누락되었습니다.");
            return;
        }

        try {
            int commentId = Integer.parseInt(commentIdParam);
    
            // 작성자 본인 또는 admin 계정인지 확인
            boolean isAuthorized = commentService.isAuthorizedToUpdate(commentId, userId);
            if (!isAuthorized) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "댓글 수정 권한이 없습니다.");
                return;
            }
    
            boolean success = commentService.updateComment(commentId, content, userId);

            if (success) {
                System.out.println("댓글 수정 성공: commentId=" + commentId + ", userId=" + userId);
                // 댓글 수정 후 게시물 상세 페이지로 리다이렉트
                response.sendRedirect(request.getHeader("Referer"));
            } else {
                System.out.println("댓글 수정 실패");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "댓글 수정 권한이 없습니다.");
            }
        } catch (NumberFormatException e) {
            System.err.println("commentId 파싱 에러: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "commentId는 숫자여야 합니다.");
        }
    }

    // 댓글 삭제 기능(작성자 본인만 가능)
    private void handleDeleteComment(HttpServletRequest request, String userId, HttpServletResponse response) throws IOException {
        String commentIdParam = request.getParameter("commentId");

        if (commentIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "필수 파라미터가 누락되었습니다.");
            return;
        }

        try {
        int commentId = Integer.parseInt(commentIdParam);

        // 작성자 본인 또는 admin 계정인지 확인
        boolean isAuthorized = commentService.isAuthorizedToUpdate(commentId, userId);
        if (!isAuthorized) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "댓글 삭제 권한이 없습니다.");
            return;
        }

        boolean success = commentService.deleteComment(commentId, userId);

            if (success) {
                System.out.println("댓글 삭제 성공: commentId=" + commentId + ", userId=" + userId);
                // 댓글 삭제 후 게시물 상세 페이지로 리다이렉트
                String postId = request.getParameter("postId");
                if (postId != null) {
                    response.sendRedirect("view_post.jsp?postId=" + postId);
                } else {
                    response.sendRedirect("post.jsp"); // 기본적으로 게시판으로 이동
                }
            } else {
                System.out.println("댓글 삭제 실패");
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "댓글 삭제 권한이 없습니다.");
            }
        } catch (NumberFormatException e) {
            System.err.println("commentId 파싱 에러: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "commentId는 숫자여야 합니다.");
        }
    }
}
