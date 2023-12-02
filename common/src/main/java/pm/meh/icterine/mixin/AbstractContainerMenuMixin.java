package pm.meh.icterine.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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
}
