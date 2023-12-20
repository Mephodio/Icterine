package pm.meh.icterine.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pm.meh.icterine.util.LogHelper;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixin {

    /**
     * Inventory stores previous state of each slot in lastSlots list, so it can
     * detect when slot changed and trigger slot listeners.
     * When inventory is created, this list is initialized with empty stacks, so
     * the very next time the game checks for changes, it sees that every slot has
     * changed, and triggers listeners for each of them, which also causes advancement
     * checks.
     * <br>
     * We use this mixin to redirect insertion of empty stack into lastSlots, and
     * replace it with actual item if the slot belongs to player inventory.
     */
    @Redirect(method = "addSlot(Lnet/minecraft/world/inventory/Slot;)Lnet/minecraft/world/inventory/Slot;",
            at = @At(value="INVOKE", target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z", ordinal = 1))
    protected boolean addSlot(NonNullList<ItemStack> lastSlots, Object emptyStack, Slot slot) {
        if (slot.container instanceof Inventory && slot.hasItem()) {
            LogHelper.debug(() -> "Adding %s to lastSlots".formatted(slot.getItem()));
            lastSlots.add(slot.getItem());
        } else {
            lastSlots.add(ItemStack.EMPTY);
        }
        return true;
    }
}
