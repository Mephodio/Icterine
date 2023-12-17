package pm.meh.icterine.mixin;

import pm.meh.icterine.iface.IItemStackMixin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IItemStackMixin {
    @Unique
    private int icterine$previousStackSize;

    @Override
    public void icterine$setPreviousStackSize(int value) {
        icterine$previousStackSize = value;
    }

    @Override
    public int icterine$getPreviousStackSize() {
        return icterine$previousStackSize;
    }
}
