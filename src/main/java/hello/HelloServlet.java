package hello;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Hello world servlet.
 * @author Koert Zeilstra
 */
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @EJB private HelloRepository helloRepository;

    @Override
    public void doGet (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        out.println(helloRepository.hello("world"));
        out.close();
    }
}