package de.dafuqs.foodeffecttooltips.config;

import net.neoforged.neoforge.common.*;
import org.apache.commons.lang3.tuple.*;

import java.util.*;

public class FoodEffectsConfig {
	
	public static final FoodEffectsConfig CONFIG;
	public static final ModConfigSpec CONFIG_SPEC;
	
	public ModConfigSpec.BooleanValue ShowSuspiciousStewTooltips;
	public ModConfigSpec.BooleanValue UseAsWhitelistInstead;
	public ModConfigSpec.ConfigValue<List<?>> BlacklistedItemIdentifiers;
	public ModConfigSpec.ConfigValue<List<?>> BlacklistedModsIDs;
	
	static {
		Pair<FoodEffectsConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(FoodEffectsConfig::new);
		
		CONFIG = pair.getLeft();
		CONFIG_SPEC = pair.getRight();
	}
	
	private FoodEffectsConfig(ModConfigSpec.Builder builder) {
		UseAsWhitelistInstead = builder.define("use_as_whitelists_instead", false);
		ShowSuspiciousStewTooltips = builder.define("show_suspicious_stew_tooltips", false);
		BlacklistedItemIdentifiers = builder.defineList("blacklisted_item_identifiers", List.of(), () -> "mymod:myid", s -> true);
		BlacklistedModsIDs = builder.defineList(
				"blacklisted_mod_ids",
				List.of("spectrum", "vinery", "farmersdelight", "createfood", "expandeddelight", "frightsdelight", "moredelight", "oceansdelight", "silentsdelight", "ubesdelight"),
				() -> "mymod", s -> true
		);
	}
	
}
