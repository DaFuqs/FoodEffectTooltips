package de.dafuqs.foodeffecttooltips.config;

import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.annotation.*;
import net.fabricmc.api.*;

import java.util.*;

@Environment(EnvType.CLIENT)
@Config(name = "FoodEffectTooltips")
public class FoodEffectsConfig implements ConfigData {
	
	public boolean ShowSuspiciousStewTooltips = false;
	public boolean UseAsWhitelistInstead = false;
	public List<String> BlacklistedItemIdentifiers = new ArrayList<>();
	public List<String> BlacklistedModsIDs = new ArrayList<>();
	
	@Override
	public void validatePostLoad() {
		if (BlacklistedItemIdentifiers.isEmpty()) {
			BlacklistedItemIdentifiers.add("no_mod:testitem");
		}
		if (BlacklistedModsIDs.isEmpty()) {
			BlacklistedModsIDs.add("spectrum");
			BlacklistedModsIDs.add("vinery");
			BlacklistedModsIDs.add("farmersdelight");
			BlacklistedModsIDs.add("createfood");
			BlacklistedModsIDs.add("expandeddelight");
			BlacklistedModsIDs.add("frightsdelight");
			BlacklistedModsIDs.add("moredelight");
			BlacklistedModsIDs.add("oceansdelight");
			BlacklistedModsIDs.add("silentsdelight");
			BlacklistedModsIDs.add("ubesdelight");
		}
	}
	
}
