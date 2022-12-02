package de.dafuqs.foodeffecttooltips.config;

import java.util.ArrayList;
import java.util.List;

@Config(name = "FoodEffectTooltips")
public class FoodEffectsConfig implements ConfigData {
	
	@Comment("")
	public boolean UseAsWhitelistInstead = false;
	
	@Comment("")
	public List<String> BlacklistedItemIdentifiers = new ArrayList<>();
	
	@Comment("")
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
