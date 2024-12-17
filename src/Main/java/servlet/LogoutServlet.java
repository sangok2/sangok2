package Main.java.servlet;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("index.jsp"); // 로그아웃 후 index.jsp로 리다이렉트
    }
}
