package pm.meh.icterine.mixin;

import pm.meh.icterine.iface.IItemStackMixin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IItemStackMixin {
    @Unique
    private boolean icterine$lastChangeDecreasedStack;

    @Override
    public void icterine$setLastChangeDecreasedStack(boolean value) {
        icterine$lastChangeDecreasedStack = value;
    }

    @Override
    public boolean icterine$isLastChangeDecreasedStack() {
        return icterine$lastChangeDecreasedStack;
    }
}
