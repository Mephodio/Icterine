package com.example.examplemod.mixin;

import com.example.examplemod.iface.IItemStackMixin;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemStack.class)
public class ItemStackMixin implements IItemStackMixin {
    @Unique
    private boolean icto$lastChangeDecreasedStack;

    @Override
    public void icto$setLastChangeDecreasedStack(boolean value) {
        icto$lastChangeDecreasedStack = value;
    }

    @Override
    public boolean icto$isLastChangeDecreasedStack() {
        return icto$lastChangeDecreasedStack;
    }
}
