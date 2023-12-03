package pm.meh.icterine.util;

import net.minecraft.world.item.ItemStack;
import pm.meh.icterine.iface.IItemStackMixin;

public class ItemStackUtil {
    /**
     * Compare old and new stacks and mark new stack with `lastChangeDecreasedStack`
     * if those stacks contain the same item, and the size of the new stack is smaller
     * than the old one.
     * @param oldStack previous ItemStack
     * @param newStack new ItemStack
     */
    public static void processItemStackInTriggerSlotListeners(ItemStack oldStack, ItemStack newStack) {
        LogHelper.debug("Stack changed from " + oldStack + " to " + newStack);
        if (newStack.sameItem(oldStack) && newStack.getCount() < oldStack.getCount()) {
            ((IItemStackMixin) (Object) newStack).icterine$setLastChangeDecreasedStack(true);
        }
    }
}
