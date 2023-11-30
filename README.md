# InventoryChangeTrigger Optimization

Changes:

* [ ] Do not perform scan if new item is air
* [x] Do not perform scan when opening inventory
  * Do not call `triggerSlotListeners` in `broadcastChanges` when called from `addSlotListener`
* [ ] When checking criterion and it has zero item requirements, skip criterion
* [ ] When checking criterion and it has single item requirement, check only changed item instead of full inventory
* [ ] In slotChanged: if new slot is air or same item with smaller amount, do not call slotChanged

## Do not perform scan when opening inventory

* net.minecraftforge.network.NetworkHooks.openScreen() 1.14%
  * net.minecraft.server.level.ServerPlayer.initMenu() 1.13%
    * net.minecraft.world.inventory.AbstractContainerMenu.addSlotListener() 1.13%
      * net.minecraft.world.inventory.AbstractContainerMenu.broadcastChanges() 1.13%
        * net.minecraft.world.inventory.AbstractContainerMenu.triggerSlotListeners() 1.13%
          * net.minecraft.server.level.ServerPlayer$2.slotChanged() 1.13% **OMIT**
            * net.minecraft.advancements.critereon.InventoryChangeTrigger.trigger() 1.13%
              * net.minecraft.advancements.critereon.SimpleCriterionTrigger.trigger() 1.13%
                * net.minecraft.advancements.critereon.EntityPredicate$Composite.matches() 1.07%
                  * net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition.test() 1.07%
                    * net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition.test() 1.07%
                      * net.minecraft.advancements.critereon.EntityPredicate.matches() 1.07%
                        * net.minecraft.advancements.critereon.NbtPredicate.matches() 1.07%
                          * net.minecraft.advancements.critereon.NbtPredicate.getEntityTagToCompare() 1.07%

`net.minecraft.server.level.ServerPlayer$2.slotChanged()` call might be omitted - it only triggers advancement