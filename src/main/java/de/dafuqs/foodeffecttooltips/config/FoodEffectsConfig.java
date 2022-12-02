package de.dafuqs.foodeffecttooltips.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Config(name = "FoodEffectTooltips")
public class FoodEffectsConfig implements ConfigData {
	
	public boolean UseAsWhitelistInstead = false;
	
	public List<String> BlacklistedItemIdentifiers = new ArrayList<>();
	
	public List<String> BlacklistedModsIDs = new ArrayList<>();
	
	@Override
	public void validatePostLoad() {
		if (BlacklistedItemIdentifiers.isEmpty()) {
			BlacklistedItemIdentifiers.add("no_mod:testitem");
		}
		if (BlacklistedModsIDs.isEmpty()) {
			BlacklistedModsIDs.add("vinery");
		}
	}
	
}
