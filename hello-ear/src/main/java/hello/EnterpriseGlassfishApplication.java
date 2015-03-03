package hello;

import org.glassfish.embeddable.*;
import org.glassfish.embeddable.archive.ScatteredArchive;
import org.glassfish.embeddable.archive.ScatteredEnterpriseArchive;

import java.io.File;
import java.io.IOException;

/**
 * Standalone class for embedded Glassfish.
 * @author Koert Zeilstra
 * https://glassfish.java.net/docs/4.0/embedded-server-guide.pdf
 */
public class EnterpriseGlassfishApplication {

    private static final int DEFAULT_PORT = 8888;

    private GlassFish glassfish;

    public static void main(String[] args) {
        try {
            System.out.println(new File("").getAbsolutePath());
            EnterpriseGlassfishApplication application = new EnterpriseGlassfishApplication();
            application.startServer(getPortFromArgs(args));
            application.deploy();

            while(true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static int getPortFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.valueOf(args[0]);
            } catch (NumberFormatException ignore) {
            }
        }
        return DEFAULT_PORT;
    }

    /**
     * Start server.
     * @param port HTTP port number.
     * @throws Exception Server failure.
     */
    private void startServer(int port) throws Exception {

        GlassFishProperties glassfishProperties = new GlassFishProperties();
        glassfishProperties.setPort("http-listener", 8080);
//        glassfishProperties.setPort("https-listener", 8081);
        glassfish = GlassFishRuntime.bootstrap().newGlassFish(glassfishProperties);

        glassfish.start();
    }

    /**
     * Deploy application on running server.
     */
    private void deploy() {
        try {
            Deployer deployer = glassfish.getDeployer();

            // Create a scattered enterprise archive.
            ScatteredEnterpriseArchive enterpriseArchive = new ScatteredEnterpriseArchive("testapp");

            ScatteredArchive ejbModule = new ScatteredArchive("hello-ejb", ScatteredArchive.Type.JAR);
            ejbModule.addClassPath(new File("glassfish-embedded-ear/hello-ejb/build/classes/main"));
            enterpriseArchive.addArchive(ejbModule.toURI(), "hello-ejb.jar");

            // src/application.xml is my META-INF/application.xml
            enterpriseArchive.addMetadata(new File("glassfish-embedded-ear/hello-ear/src/main/resources/application.xml"), "application.xml");
//            enterpriseArchive.addMetadata(new File("glassfish-embedded-ear/hello-war/src/main/resources/glassfish-resources.xml"));
            // Add scattered web module to the scattered enterprise archive.
            // src/application.xml references Web module as "scattered.war".
            //Hence specify the name while adding the archive.
            ScatteredArchive webModule = new ScatteredArchive("helloweb", ScatteredArchive.Type.WAR,
                    new File("glassfish-embedded-ear/hello-war/src/main/webapp"));
            webModule.addMetadata(new File("glassfish-embedded-ear/hello-war/src/main/resources/web.xml"));
            enterpriseArchive.addArchive(webModule.toURI(), "hello.war");
            // lib/mylibrary.jar is a library JAR file.
            //enterpriseArchive.addArchive(new File("lib", "mylibrary.jar"));
            // target/ejbclasses contain my compiled EJB module.
            // src/application.xml references EJB module as "ejb.jar".
            //Hence specify the name while adding the archive.
            //enterpriseArchive.addArchive(new File("target", "ejbclasses"), "ejb.jar");


            String appName = deployer.deploy(enterpriseArchive.toURI());
        } catch (GlassFishException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
