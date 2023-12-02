package pm.meh.icterine.util;

import net.minecraft.world.item.ItemStack;
import pm.meh.icterine.Constants;
import pm.meh.icterine.iface.IItemStackMixin;

public class ItemStackUtil {
    public static void processItemStackInTriggerSlotListeners(ItemStack oldStack, ItemStack newStack) {
        LogHelper.debug("Stack changed from " + oldStack + " to " + newStack);
        if (newStack.sameItem(oldStack) && newStack.getCount() < oldStack.getCount()) {
            ((IItemStackMixin) (Object) newStack).icterine$setLastChangeDecreasedStack(true);
        }
    }
}
