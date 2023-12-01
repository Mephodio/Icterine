package pm.meh.icterine.mixin;

import pm.meh.icterine.iface.IItemStackMixin;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangeTrigger.class)
abstract class InventoryChangeTriggerMixin {
    @Inject(method = "trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "HEAD"), cancellable = true)
    public void trigger(ServerPlayer serverPlayer, Inventory inventory, ItemStack itemStack, CallbackInfo ci) {
        System.out.println("called trigger for " + itemStack);
        if (itemStack.isEmpty()
                || ((IItemStackMixin) (Object) itemStack).icterine$isLastChangeDecreasedStack()) {
            ci.cancel();
            System.out.println("cancelled trigger for " + itemStack);
        }
    }
}
