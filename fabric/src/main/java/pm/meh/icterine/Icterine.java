package pm.meh.icterine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import pm.meh.icterine.fabric.ReloadListenerHandler;

public class Icterine implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Common.init();

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ReloadListenerHandler());
    }
}
