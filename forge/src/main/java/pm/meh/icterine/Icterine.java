package pm.meh.icterine;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import pm.meh.icterine.forge.ReloadListenerHandler;

@Mod(Common.MOD_ID)
public class Icterine {
    
    public Icterine() {
        Common.init();

        MinecraftForge.EVENT_BUS.register(ReloadListenerHandler.class);
    }
}