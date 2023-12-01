package com.example.examplemod.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixin {

    @Redirect(method = "addSlot", at = @At(value="INVOKE", target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z", ordinal = 1))
    protected boolean addSlot(NonNullList<ItemStack> lastSlots, Object emptyStack, Slot slot) {
        if (slot.container instanceof Inventory) {
            /*
                 YOU CANT DO THIS, you cannot check class of containerMenu, player slots are in all inventories.
                 Check slot container, if available

                 And in `triggerSlotListeners` you somehow need to detect is it ServerPlayer or not,
                 and same thing with RecipeBookMenu check
             */
            lastSlots.add(slot.getItem());
        } else {
            lastSlots.add(ItemStack.EMPTY);
        }
        return true;
    }

    @Inject(method = "triggerSlotListeners",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void triggerSlotListenersSaveOld(int slotNumber, ItemStack newStack,
            Supplier<ItemStack> newStackSupplier, CallbackInfo ci, ItemStack oldStack) {
//        System.out.println("stack changed: " + oldStack.toString() + " " + newStack.toString());
    }
}
