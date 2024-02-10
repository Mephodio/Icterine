package pm.meh.icterine.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import pm.meh.icterine.Common;

import java.util.Map;
import java.util.Objects;

public class ReloadListenerHandlerBase extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final String FOLDER = "advancements";

    public ReloadListenerHandlerBase() {
        super(GSON, FOLDER);
    }

    /**
     * Collects all item count thresholds from loaded advancements.
     * For example, if there are advancements for obtaining any amount of stone,
     * 5 emeralds and 64 sticks, thresholds will be [1, 5, 64].
     * We then could use these thresholds to prevent unneeded advancement scanning.
     * For example, there is no need to check advancements when dirt stack size
     * increases from 52 to 53 if there's no advancement for getting 53 dirt.
     */
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        if (Common.config.OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS) {
            StackSizeThresholdManager.clear();

            String inventoryChangedTriggerId = Objects.requireNonNull(BuiltInRegistries.TRIGGER_TYPES.getKey(CriteriaTriggers.INVENTORY_CHANGED)).toString();

            for (JsonElement advancementElement : object.values()) {
                JsonObject advancementCriteria = advancementElement.getAsJsonObject().getAsJsonObject("criteria");
                if (advancementCriteria != null && !advancementCriteria.isJsonNull()) {
                    for (var criterionEntry : advancementCriteria.entrySet()) {
                        JsonObject criterion = criterionEntry.getValue().getAsJsonObject();
                        JsonElement criterionTrigger = criterion.get("trigger");
                        JsonObject criterionConditions = criterion.getAsJsonObject("conditions");
                        if (criterionTrigger != null && criterionConditions != null
                                && !criterionTrigger.isJsonNull() && !criterionConditions.isJsonNull()
                                && criterionTrigger.getAsString().equals(inventoryChangedTriggerId)
                                && criterionConditions.has("items")) {
                            for (JsonElement itemElement : criterionConditions.getAsJsonArray("items")) {
                                JsonElement itemCount = itemElement.getAsJsonObject().get("count");
                                if (itemCount != null && !itemCount.isJsonNull()) {
                                    int itemCountMinValue = 0;

                                    if (itemCount.isJsonObject()) {
                                        JsonElement itemCountMin = itemCount.getAsJsonObject().get("min");
                                        if (itemCountMin != null && !itemCountMin.isJsonNull()) {
                                            itemCountMinValue = itemCountMin.getAsInt();
                                        }
                                    } else {
                                        itemCountMinValue = itemCount.getAsInt();
                                    }

                                    if (itemCountMinValue > 1) {
                                        StackSizeThresholdManager.add(itemCountMinValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            StackSizeThresholdManager.debugPrint();
        }
    }
}