package de.dafuqs.foodeffecttooltips;

import de.dafuqs.foodeffecttooltips.config.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.config.*;
import net.neoforged.neoforge.client.gui.*;

@Mod(value = FoodEffectTooltips.MOD_ID, dist = Dist.CLIENT)
public class FoodEffectTooltips {
	
	public static final String MOD_ID = "foodeffecttooltips";
	public static FoodEffectsConfig CONFIG;
	
	public FoodEffectTooltips(IEventBus modBus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, FoodEffectsConfig.CONFIG_SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}
	
}
