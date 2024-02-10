package pm.meh.icterine;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import pm.meh.icterine.impl.ReloadListenerHandlerBase;

@Mod(Common.MOD_ID)
public class Icterine {
    
    public Icterine(IEventBus eventBus) {
        Common.init();

        eventBus.addListener((AddReloadListenerEvent event) -> event.addListener(new ReloadListenerHandlerBase()));
    }
}