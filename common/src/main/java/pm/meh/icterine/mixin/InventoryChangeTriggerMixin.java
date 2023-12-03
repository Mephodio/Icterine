package pm.meh.icterine.mixin;

import pm.meh.icterine.Common;
import pm.meh.icterine.iface.IItemStackMixin;
import pm.meh.icterine.util.LogHelper;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
abstract class InventoryChangeTriggerMixin {
    /**
     * This injection cancels triggering advancement scan for changed slot
     * if this trigger was caused by emptying or decreasing the stack size.
     */
    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void trigger(ServerPlayer serverPlayer, Inventory inventory, ItemStack itemStack, CallbackInfo ci) {
        if ((itemStack.isEmpty() && Common.config.IGNORE_TRIGGERS_FOR_EMPTIED_STACKS)
                || (Common.config.IGNORE_TRIGGERS_FOR_DECREASED_STACKS
                    && ((IItemStackMixin) (Object) itemStack).icterine$isLastChangeDecreasedStack())) {
            ci.cancel();
            LogHelper.debug("InventoryChangeTrigger cancelled for " + itemStack);
        } else {
            LogHelper.debug("InventoryChangeTrigger passed for " + itemStack);
        }
    }
}
