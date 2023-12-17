package pm.meh.icterine.util;

import net.minecraft.world.item.ItemStack;
import pm.meh.icterine.iface.IItemStackMixin;

public class ItemStackUtil {
    /**
     * Compare old and new stacks and mark new stack with `lastChangeDecreasedStack`
     * if those stacks contain the same item, and the size of the new stack is smaller
     * than the old one.
     * <br>
     * When slot content changes, this method is used to determine if change was caused
     * by decreasing stack size.
     * <br>
     * For some reason (differences in decompiling/mapping maybe?), on Forge and Fabric
     * this mixin detects different local variables.
     * <br> On forge: `ItemStack`, `boolean`, `ItemStack`
     * <br> On fabric: `ItemStack`, `ItemStack`
     * <br>
     * So we need platform-specific mixins with different locals sets.
     *
     * @param oldStack previous ItemStack
     * @param newStack new ItemStack
     */
    public static void processItemStackInTriggerSlotListeners(ItemStack oldStack, ItemStack newStack) {
        LogHelper.debug("Stack changed from " + oldStack + " to " + newStack);
        if (newStack.sameItem(oldStack)) {
            ((IItemStackMixin) (Object) newStack).icterine$setPreviousStackSize(oldStack.getCount());
        }
    }
}
