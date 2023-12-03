package pm.meh.icterine.mixin;

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
abstract class AbstractContainerMenuMixinPlatform {

    /**
     * When slot content changes, this injected code compares old and new stack
     * to determine if change was caused by decreasing stack size.
     * <br>
     * For some reason (differences in decompiling/mapping maybe?), on Forge and Fabric
     * this mixin detects different local variables.
     * <br> On forge: `ItemStack`, `boolean`, `ItemStack`
     * <br> On fabric: `ItemStack`, `ItemStack`
     * <br>
     * So we need platform-specific mixins with different locals sets.
     */
    @Inject(method = "triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void triggerSlotListeners(int slotNumber, ItemStack newStack,
                                      Supplier<ItemStack> newStackSupplier, CallbackInfo ci,
                                      ItemStack oldStack, ItemStack newStack1) {
        ItemStackUtil.processItemStackInTriggerSlotListeners(oldStack, newStack1);
    }
}
