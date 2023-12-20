package pm.meh.icterine.util;

import pm.meh.icterine.Common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Config {

    private static final String configFileName = Common.MOD_ID + ".yml";
    private static final Path configPath = Paths.get("config", configFileName);

    public final boolean DEBUG_MODE;
    public final boolean IGNORE_TRIGGERS_FOR_EMPTIED_STACKS;
    public final boolean IGNORE_TRIGGERS_FOR_DECREASED_STACKS;
    public final boolean OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER;
    public final boolean INITIALIZE_INVENTORY_LAST_SLOTS;
    public final boolean OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS;
    public final boolean CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH;

    private final Map<String, String> configData = new HashMap<>();

    public Config() {
        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DEBUG_MODE = getBoolean("debug_mode", false);
        IGNORE_TRIGGERS_FOR_EMPTIED_STACKS = getBoolean("ignore_triggers_for_emptied_stacks", true);
        IGNORE_TRIGGERS_FOR_DECREASED_STACKS = getBoolean("ignore_triggers_for_decreased_stacks", true);
        OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER = getBoolean("optimize_multiple_predicate_trigger", true);
        INITIALIZE_INVENTORY_LAST_SLOTS = getBoolean("initialize_inventory_last_slots", true);
        OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS = getBoolean("optimize_triggers_for_increased_stacks", true);
        CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH = getBoolean("check_count_before_item_predicate_match", true);
    }

    private void load() throws IOException {
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

            try {
                Common.LOG.error("Appending missing Icterine option {} to config file", key);
                Files.write(
                        configPath,
                        ("\n\n" + key + ": " + defaultValue).getBytes(),
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                Common.LOG.error("Unable to append missing option {} to config file: {}", key, e);
            }

            return defaultValue;
        }
        return value.equals("true");
    }

}
