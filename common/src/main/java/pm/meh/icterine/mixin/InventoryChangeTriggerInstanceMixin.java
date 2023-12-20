package pm.meh.icterine.mixin;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.meh.icterine.Common;
import pm.meh.icterine.iface.IItemPredicateMixin;
import pm.meh.icterine.util.LogHelper;

import java.util.List;

@Mixin(InventoryChangeTrigger.TriggerInstance.class)
abstract class InventoryChangeTriggerInstanceMixin extends AbstractCriterionTriggerInstance {
    public InventoryChangeTriggerInstanceMixin(ResourceLocation $$0, EntityPredicate.Composite $$1) {
        super($$0, $$1);
    }

    /**
     * When the game checks if given inventoryChangeTrigger matches changed stack
     * and this trigger has only one predicate, game only checks if said trigger
     * matches changed stack.
     * <br>
     * However, when trigger has multiple predicates, for some reason game doesn't
     * check them against changed stack at first, but against all the items in
     * player inventory, even if changed stack doesn't match any of predicates.
     * <br>
     * So this injected code checks if any of predicates match the changed stack,
     * and if not, cancels the full inventory scan.
     */
    @Inject(method = "matches(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getContainerSize()I", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void matches(Inventory inventory, ItemStack itemStack, int i, int j, int k,
            CallbackInfoReturnable<Boolean> cir, int predicatesLength, List<ItemPredicate> predicatesList) {
        // If no predicate in list matches the changed item, the trigger not matches
        if (Common.config.OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER
                && !predicatesList.removeIf(itemPredicate -> itemPredicate.matches(itemStack))) {
            LogHelper.debug("Trigger has multiple predicates, and none of them matches changed item. Skipping full inventory check");
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Redirect(method = "matches(Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;III)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/critereon/ItemPredicate;matches(Lnet/minecraft/world/item/ItemStack;)Z"))
    public boolean itemPredicateMatches(ItemPredicate itemPredicate, ItemStack itemStack) {
        if (Common.config.CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH) {
            return ((IItemPredicateMixin) itemPredicate).icterine$fasterMatches(itemStack);
        } else {
            return itemPredicate.matches(itemStack);
        }
    }
}
