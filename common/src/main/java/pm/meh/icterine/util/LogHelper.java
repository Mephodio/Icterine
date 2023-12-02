package pm.meh.icterine.util;

import pm.meh.icterine.Common;

public class LogHelper {

    private static boolean initialized = false;
    private static boolean debugEnabled = false;
    public static void debug(String msg) {
        if (!initialized && Common.config != null) {
            initialized = true;
            debugEnabled = Common.config.DEBUG_MODE;
        }
        if (debugEnabled) {
            Common.LOG.info(msg);
        }
    }
}
