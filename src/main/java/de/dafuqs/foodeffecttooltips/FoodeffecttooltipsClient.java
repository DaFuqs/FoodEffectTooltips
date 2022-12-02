package de.dafuqs.foodeffecttooltips;

import de.dafuqs.foodeffecttooltips.config.FoodEffectsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class FoodeffecttooltipsClient implements ClientModInitializer {
	
	public static FoodEffectsConfig CONFIG;
	
	@Override
	public void onInitializeClient() {
		
		AutoConfig.register(FoodEffectsConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(FoodEffectsConfig.class).getConfig();
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if(stack.isFood() && shouldShowTooltip(stack)) {
				TooltipHelper.addFoodComponentEffectTooltip(stack, lines);
			}
		});
	}
	
	public static boolean shouldShowTooltip(ItemStack stack) {
		if(CONFIG == null) {
			return false;
		}
		
		Item item = stack.getItem();
		Identifier identifier = Registry.ITEM.getId(item);
		
		boolean isWhitelist = CONFIG.UseAsWhitelistInstead;
		if(CONFIG.BlacklistedItemIdentifiers.contains(identifier.toString())) {
			return isWhitelist;
		}
		if(CONFIG.BlacklistedModsIDs.contains(identifier.getNamespace())) {
			return isWhitelist;
		}
		return !isWhitelist;
	}
	
	
}
