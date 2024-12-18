package Main.java.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 세션 무효화
        HttpSession session = request.getSession(false); // 기존 세션 가져오기
        if (session != null) {
            session.invalidate(); // 세션 무효화
            System.out.println("세션이 무효화되었습니다."); // 로그 출력
        }

        // 로그아웃 후 index.jsp로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
