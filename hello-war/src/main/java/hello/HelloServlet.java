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
@WebServlet(name="hello", urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet {

    @EJB HelloRepository helloRepository;

    public void doGet (HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        out.println("Hello, world!");
        out.close();
    }
}