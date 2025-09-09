package de.dafuqs.foodeffecttooltips;

import de.dafuqs.foodeffecttooltips.config.*;
import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.serializer.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.client.gui.*;

@Mod(value = FoodEffectTooltips.MOD_ID, dist = Dist.CLIENT)
public class FoodEffectTooltips {
	
	public static final String MOD_ID = "foodeffecttooltips";
	public static FoodEffectsConfig CONFIG;
	
	public FoodEffectTooltips(IEventBus modBus, ModContainer modContainer) {
		AutoConfig.register(FoodEffectsConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(FoodEffectsConfig.class).getConfig();
		
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, (modCont, parent) -> AutoConfig.getConfigScreen(FoodEffectsConfig.class, parent).get());
	}
	
}
