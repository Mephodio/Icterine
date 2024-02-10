package pm.meh.icterine;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import pm.meh.icterine.impl.ReloadListenerHandlerBase;

@Mod(Common.MOD_ID)
public class Icterine {
    
    public Icterine() {
        Common.init();

        NeoForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> event.addListener(new ReloadListenerHandlerBase()));
    }
}