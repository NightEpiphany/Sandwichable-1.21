package com.moigferdsrte.sandwichable.compact.modmenu;

import com.moigferdsrte.sandwichable.config.ConfigInABarrel;
import com.moigferdsrte.sandwichable.config.SandwichableConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SandwichableModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigInABarrel.screen(SandwichableConfig.class, parent);
    }
}
