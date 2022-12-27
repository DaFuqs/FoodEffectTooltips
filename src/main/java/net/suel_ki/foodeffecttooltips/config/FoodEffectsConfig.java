package net.suel_ki.foodeffecttooltips.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class FoodEffectsConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.BooleanValue UseAsWhitelistInstead;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> BlacklistedItemIdentifiers;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>>  BlacklistedModsIDs;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("food effects tooltip");

        UseAsWhitelistInstead = builder.
                comment("Use Lists as Whitelists instead.").
                define("whitelistInstead", false);

        BlacklistedItemIdentifiers = builder.
                comment("Blacklisted Item.").
                defineList("blacklistItems", List.of("modid:testitem"), itemName -> itemName instanceof String);

        BlacklistedModsIDs = builder.
                comment("Blacklisted Mod IDs.").
                defineList("blacklistModsIDs", List.of("modid"), modName -> modName instanceof String);

        builder.pop();

        CLIENT_SPEC = builder.build();
    }

}
