package pm.meh.icterine.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import pm.meh.icterine.Common;

import java.util.Map;

public class ReloadListenerHandlerBase extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).create();
    private static final String FOLDER = "advancements";

    public ReloadListenerHandlerBase() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        if (Common.config.OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS) {
            StackSizeThresholdManager.clear();

            String inventoryChangedTriggerId = CriteriaTriggers.INVENTORY_CHANGED.getId().toString();

            for (JsonElement advancementElement : object.values()) {
                JsonObject advancementCriteria = advancementElement.getAsJsonObject().getAsJsonObject("criteria");
                if (advancementCriteria != null) {
                    for (var criterionEntry : advancementCriteria.entrySet()) {
                        JsonObject criterion = criterionEntry.getValue().getAsJsonObject();
                        JsonElement criterionTrigger = criterion.get("trigger");
                        JsonObject criterionConditions = criterion.getAsJsonObject("conditions");
                        if (criterionTrigger != null && criterionConditions != null
                                && criterionTrigger.getAsString().equals(inventoryChangedTriggerId)
                                && criterionConditions.has("items")) {
                            for (JsonElement itemElement : criterionConditions.getAsJsonArray("items")) {
                                JsonElement itemCount = itemElement.getAsJsonObject().get("count");
                                if (itemCount != null) {
                                    int itemCountMinValue = 0;

                                    if (itemCount.isJsonObject()) {
                                        JsonElement itemCountMin = itemCount.getAsJsonObject().get("min");
                                        if (itemCountMin != null) {
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