package pm.meh.icterine.mixin.neoforge;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.meh.icterine.util.ItemStackUtil;

import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixin {

    /**
     * See {@link pm.meh.icterine.util.ItemStackUtil#processItemStackInTriggerSlotListeners}
     */
    @Inject(method = "triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void triggerSlotListeners(int slotNumber, ItemStack newStack,
                                      Supplier<ItemStack> newStackSupplier, CallbackInfo ci,
                                      ItemStack oldStack, boolean clientStackChanged, ItemStack newStack1) {
        ItemStackUtil.processItemStackInTriggerSlotListeners(oldStack, newStack1);
    }
}
