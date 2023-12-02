package pm.meh.icterine;

import java.util.logging.Level;

public class CommonClass {

    public static Config config;

    public static void init() {
        config = new Config();

        Constants.LOG.setLevel(config.DEBUG_MODE ? Level.ALL : Level.INFO);
    }
}