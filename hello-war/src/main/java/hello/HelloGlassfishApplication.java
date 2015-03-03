package hello;

import org.glassfish.embeddable.*;
import org.glassfish.embeddable.archive.ScatteredArchive;

import java.io.File;
import java.io.IOException;

/**
 * Standalone class for embedded Glassfish.
 * @author Koert Zeilstra
 * https://glassfish.java.net/docs/4.0/embedded-server-guide.pdf
 */
public class HelloGlassfishApplication {

    private static final int DEFAULT_PORT = 8888;

    private GlassFish glassfish;

    public static void main(String[] args) {
        try {
            System.out.println(new File("").getAbsolutePath());
            HelloGlassfishApplication application = new HelloGlassfishApplication();
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

    private void startServer(int port) throws Exception {

        GlassFishProperties glassfishProperties = new GlassFishProperties();
        glassfishProperties.setPort("http-listener", 8080);
        glassfish = GlassFishRuntime.bootstrap().newGlassFish(glassfishProperties);

        glassfish.start();
    }

    private void deploy() {
        try {
            Deployer deployer = glassfish.getDeployer();
            ScatteredArchive archive = new ScatteredArchive("helloapp", ScatteredArchive.Type.WAR,
                    new File("glassfish-embedded-ear/hello-war/src/main/webapp"));
            archive.addMetadata(new File("glassfish-embedded-ear/hello-war/src/main/resources/web.xml"));
            String appName = deployer.deploy(archive.toURI(), "--contextroot=hello");
        } catch (GlassFishException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
