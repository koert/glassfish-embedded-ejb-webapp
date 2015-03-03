package hello;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Example repository EJB.
 * @author Koert Zeilstra
 */
@Stateless
@LocalBean
public class HelloRepository {

    public String hello(String name) {
        return "Hello " + name + "!";
    }
}
