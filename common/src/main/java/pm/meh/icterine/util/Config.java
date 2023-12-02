package pm.meh.icterine.util;

import org.simpleyaml.configuration.file.YamlConfiguration;
import pm.meh.icterine.Common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Config {

    private static final String configFileName = Common.MOD_ID + ".yml";

    public final boolean DEBUG_MODE;
    public final boolean IGNORE_TRIGGERS_FOR_EMPTIED_STACKS;
    public final boolean IGNORE_TRIGGERS_FOR_DECREASED_STACKS;
    public final boolean OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER;
    public final boolean INITIALIZE_INVENTORY_LAST_SLOTS;

    public Config() {
        // Try to load config
        Path configFile = Paths.get("config", configFileName);
        YamlConfiguration config;
        try {
            if (!Files.exists(configFile)) {
                Files.copy(Objects.requireNonNull(getClass().getResourceAsStream('/' + configFileName)), configFile);
            }
            config = YamlConfiguration.loadConfiguration(configFile.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DEBUG_MODE = config.getBoolean("debug_mode");
        IGNORE_TRIGGERS_FOR_EMPTIED_STACKS = config.getBoolean("ignore_triggers_for_emptied_stacks");
        IGNORE_TRIGGERS_FOR_DECREASED_STACKS = config.getBoolean("ignore_triggers_for_decreased_stacks");
        OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER = config.getBoolean("optimize_multiple_predicate_trigger");
        INITIALIZE_INVENTORY_LAST_SLOTS = config.getBoolean("initialize_inventory_last_slots");
    }

}
