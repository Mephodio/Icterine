package pm.meh.icterine.mixin;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Surrogate;
import pm.meh.icterine.iface.IItemStackMixin;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixin {

    @Redirect(method = "addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
            at = @At(value="INVOKE", target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z", ordinal = 1))
    protected boolean addSlot(NonNullList<ItemStack> lastSlots, Object emptyStack, Slot slot) {
        if (slot.container instanceof Inventory) {
            lastSlots.add(slot.getItem());
        } else {
            lastSlots.add(ItemStack.EMPTY);
        }
        return true;
    }

    // Fabric for some reason doesn't have separate local variable clientStackChanged, so we have overload
    @Surrogate
    @Inject(method = "triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void triggerSlotListeners(int slotNumber, ItemStack newStack,
                                      Supplier<ItemStack> newStackSupplier, CallbackInfo ci,
                                      ItemStack oldStack, boolean clientStackChanged, ItemStack newStack1) {
        icterine$processItemStackInTriggerSlotListeners(oldStack, newStack1);
    }

    @Surrogate
    @Inject(method = "triggerSlotListeners(ILnet/minecraft/world/item/ItemStack;Ljava/util/function/Supplier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void triggerSlotListeners(int slotNumber, ItemStack newStack,
                                      Supplier<ItemStack> newStackSupplier, CallbackInfo ci,
                                      ItemStack oldStack, ItemStack newStack1) {
        icterine$processItemStackInTriggerSlotListeners(oldStack, newStack1);
    }

    @Unique
    private void icterine$processItemStackInTriggerSlotListeners(ItemStack oldStack, ItemStack newStack) {
        System.out.println("stack changed: " + oldStack.toString() + " " + newStack.toString());
        if (newStack.sameItem(oldStack) && newStack.getCount() < oldStack.getCount()) {
            ((IItemStackMixin) (Object) newStack).icterine$setLastChangeDecreasedStack(true);
        }
    }
}
