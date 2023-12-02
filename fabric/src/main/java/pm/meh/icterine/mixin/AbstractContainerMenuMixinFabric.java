package pm.meh.icterine.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.meh.icterine.iface.IItemStackMixin;
import pm.meh.icterine.util.ItemStackUtil;

import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixinFabric {

    @Inject(method = "triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void triggerSlotListeners(int slotNumber, ItemStack newStack,
                                      Supplier<ItemStack> newStackSupplier, CallbackInfo ci,
                                      ItemStack oldStack, ItemStack newStack1) {
        ItemStackUtil.processItemStackInTriggerSlotListeners(oldStack, newStack1);
    }
}
