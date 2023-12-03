package pm.meh.icterine.util;

import pm.meh.icterine.Common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Config {

    private static final String configFileName = Common.MOD_ID + ".yml";

    public final boolean DEBUG_MODE;
    public final boolean IGNORE_TRIGGERS_FOR_EMPTIED_STACKS;
    public final boolean IGNORE_TRIGGERS_FOR_DECREASED_STACKS;
    public final boolean OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER;
    public final boolean INITIALIZE_INVENTORY_LAST_SLOTS;

    private final Map<String, String> configData = new HashMap<>();

    public Config() {
        try {
            load(Paths.get("config", configFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DEBUG_MODE = getBoolean("debug_mode", false);
        IGNORE_TRIGGERS_FOR_EMPTIED_STACKS = getBoolean("ignore_triggers_for_emptied_stacks", true);
        IGNORE_TRIGGERS_FOR_DECREASED_STACKS = getBoolean("ignore_triggers_for_decreased_stacks", true);
        OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER = getBoolean("optimize_multiple_predicate_trigger", true);
        INITIALIZE_INVENTORY_LAST_SLOTS = getBoolean("initialize_inventory_last_slots", true);
    }

    private void load(Path configPath) throws IOException {
        if (!Files.exists(configPath)) {
            Files.copy(Objects.requireNonNull(getClass().getResourceAsStream('/' + configFileName)), configPath);
        }
        try (Stream<String> lines = Files.lines(configPath)) {
            lines.forEach(line -> {
                    if (!line.trim().isEmpty() && line.charAt(0) != '#') {
                        String[] parts = line.split(": ");
                        if (parts.length != 2) {
                            throw new RuntimeException("Invalid config parameter:\n" + line);
                        }
                        configData.put(parts[0], parts[1]);
                    }
                });
        }
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        String value = configData.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value.equals("true");
    }

}
