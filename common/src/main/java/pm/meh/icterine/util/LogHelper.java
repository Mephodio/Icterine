package pm.meh.icterine.util;

import pm.meh.icterine.CommonClass;
import pm.meh.icterine.Constants;

public class LogHelper {

    private static boolean initialized = false;
    private static boolean debugEnabled = false;
    public static void debug(String msg) {
        if (!initialized && CommonClass.config != null) {
            initialized = true;
            debugEnabled = CommonClass.config.DEBUG_MODE;
        }
        if (debugEnabled) {
            Constants.LOG.info(msg);
        }
    }
}
