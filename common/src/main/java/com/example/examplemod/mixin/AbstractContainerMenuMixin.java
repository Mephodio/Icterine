package com.example.examplemod.mixin;

import com.google.common.base.Suppliers;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

@Mixin(AbstractContainerMenu.class)
abstract class AbstractContainerMenuMixin {

    @Final
    @Shadow
    public NonNullList<Slot> slots;

    @Final
    @Shadow
    private List<DataSlot> dataSlots;

    @Shadow
    protected abstract void triggerSlotListeners(int i, ItemStack itemStack, Supplier<ItemStack> supplier);

    @Shadow
    protected abstract void synchronizeSlotToRemote(int i, ItemStack itemStack, Supplier<ItemStack> supplier);

    @Shadow
    protected abstract void synchronizeCarriedToRemote();

    @Shadow
    protected abstract void updateDataSlotListeners(int i, int j);

    @Shadow
    protected abstract void synchronizeDataSlotToRemote(int i, int j);

    @Redirect(method = "addSlot", at = @At(value="INVOKE", target = "Lnet/minecraft/core/NonNullList;add(Ljava/lang/Object;)Z", ordinal = 1))
    protected boolean addSlot(NonNullList lastSlots, Object emptyStack, Slot slot) {
        lastSlots.add(slot.getItem());
        return true;
    }

//    @Redirect(method = "addSlotListener", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;broadcastChanges()V"))
//    public void addSlotListenerBroadcastRedirect(AbstractContainerMenu obj) {
//        ((AbstractContainerMenuMixin) (Object) obj).initBroadcastChanges();
//    }

//    @Unique
//    public void initBroadcastChanges() {
//        int i;
//        for(i = 0; i < this.slots.size(); ++i) {
//            Slot slot = (Slot)this.slots.get(i);
//            ItemStack itemStack = slot.getItem();
//            Objects.requireNonNull(itemStack);
//            Supplier<ItemStack> supplier = Suppliers.memoize(itemStack::copy);
//            // Slots haven't changed yet, not reason to trigger listeners?
////            if (!(slot.container instanceof Inventory) && !itemStack.isEmpty()) {
////                this.triggerSlotListeners(i, itemStack, supplier);
////            }
//            this.synchronizeSlotToRemote(i, itemStack, supplier);
//        }
//
//        this.synchronizeCarriedToRemote();
//
//        for(i = 0; i < this.dataSlots.size(); ++i) {
//            DataSlot dataSlot = (DataSlot)this.dataSlots.get(i);
//            int j = dataSlot.get();
//            if (dataSlot.checkAndClearUpdateFlag()) {
//                this.updateDataSlotListeners(i, j);
//            }
//
//            this.synchronizeDataSlotToRemote(i, j);
//        }
//
//    }
}
