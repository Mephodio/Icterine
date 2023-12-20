package pm.meh.icterine.mixin;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pm.meh.icterine.iface.IItemPredicateMixin;
import pm.meh.icterine.iface.IItemStackMixin;
import pm.meh.icterine.util.LogHelper;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin implements IItemPredicateMixin {
    @Final
    @Shadow
    private MinMaxBounds.Ints count;

    @Shadow
    public abstract boolean matches(ItemStack itemStack);

    @Override
    public boolean icterine$fasterMatches(ItemStack itemStack) {
        Integer minThr = count.getMin();
        Integer maxThr = count.getMax();
        int stackCount = itemStack.getCount();
        int prevStackCount = ((IItemStackMixin) (Object) itemStack).icterine$getPreviousStackSize();

        LogHelper.debug("Checking stack %d for range [%d; %d]".formatted(stackCount, minThr, maxThr));

        if ((minThr == null ? prevStackCount == 0 :
                (prevStackCount < minThr && minThr <= stackCount))
                && (maxThr == null || stackCount <= maxThr)) {
            return matches(itemStack);
        }
        return false;
    }
}