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

import java.util.Optional;

@Mixin(ItemPredicate.class)
public abstract class ItemPredicateMixin implements IItemPredicateMixin {
    @Final
    @Shadow
    private MinMaxBounds.Ints count;

    @Shadow
    public abstract boolean matches(ItemStack itemStack);

    /**
     * Item matching, especially against tag, is very heavy. By comparing the stack count first,
     * we could avoid unneeded tag matching.
     * We also could use previous stack count to avoid even more unneeded matching.
     */
    @Override
    public boolean icterine$fasterMatches(ItemStack itemStack) {
        Optional<Integer> minThr = count.min();
        Optional<Integer> maxThr = count.max();
        int stackCount = itemStack.getCount();
        int prevStackCount = ((IItemStackMixin) (Object) itemStack).icterine$getPreviousStackSize();

        LogHelper.debug(() -> "Checking stack %d for range [%d; %d]".formatted(stackCount, minThr.orElse(null), maxThr.orElse(null)));

        if ((minThr.map(integer -> (prevStackCount < integer && integer <= stackCount)).orElseGet(() -> prevStackCount == 0))
                && (maxThr.isEmpty() || stackCount <= maxThr.get())) {
            return matches(itemStack);
        }
        return false;
    }
}
