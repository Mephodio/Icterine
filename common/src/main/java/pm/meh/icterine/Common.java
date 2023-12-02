package pm.meh.icterine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pm.meh.icterine.util.Config;

public class Common {

    public static final String MOD_ID = "icterine";
    public static final String MOD_NAME = "Icterine";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static Config config;

    public static void init() {
        config = new Config();
    }
}