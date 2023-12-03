package pm.meh.icterine.util;

import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import pm.meh.icterine.Common;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This plugin disables loading specific mixins based on settings in config file.
 */
public class MixinPlugin implements IMixinConfigPlugin {

    private static final Map<String, Boolean> CONDITIONS = ImmutableMap.of(
            "pm.meh.icterine.mixin.AbstractContainerMenuMixin", Common.config.INITIALIZE_INVENTORY_LAST_SLOTS,
            "pm.meh.icterine.mixin.InventoryChangeTriggerInstanceMixin", Common.config.OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER,
            "pm.meh.icterine.mixin.InventoryChangeTriggerMixin", Common.config.IGNORE_TRIGGERS_FOR_EMPTIED_STACKS
                    || Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS,
            "pm.meh.icterine.mixin.ItemStackMixin", Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS,
            "pm.meh.icterine.mixin.AbstractContainerMenuMixinPlatform", Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS
    );

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean status = CONDITIONS.getOrDefault(mixinClassName, true);
        LogHelper.debug("Apply mixin " + mixinClassName + ": " + status);
        return status;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
