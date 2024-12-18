
package Main.java.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Main.java.service.User;
import Main.java.service.UserService;

import java.sql.*;

public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 폼에서 전달된 데이터 가져오기
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String ssn = request.getParameter("ssn");

        // User 객체 생성
        User user = new User(userId, password, name, phone, address, ssn);

        // UserService를 사용하여 회원가입 처리
        UserService userService = new UserService();

        if (userService.registerUser(user)) {
            // 회원가입 성공 → 로그인 페이지로 리다이렉트(절대 경로)
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            // 회원가입 실패 → 회원가입 페이지 리다이렉트 + 에러 메시지 출력
            response.sendRedirect("register.html?error=true");
        }
    }
}

