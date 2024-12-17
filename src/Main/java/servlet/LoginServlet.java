package Main.java.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import Main.java.UserService; // UserService 패키지 경로

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // UserService를 통해 로그인 검증
        UserService userService = new UserService();

        if (userService.login(userId, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("userId", userId);
            response.sendRedirect("index.jsp"); // 로그인 성공 시 index.jsp로 이동
        } else {
            response.sendRedirect("login.jsp?error=true"); // 로그인 실패 시 에러 메시지
        }
    }
}
