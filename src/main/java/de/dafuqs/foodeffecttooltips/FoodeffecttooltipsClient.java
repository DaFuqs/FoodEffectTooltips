package de.dafuqs.foodeffecttooltips;

import de.dafuqs.foodeffecttooltips.config.*;
import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.serializer.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.item.v1.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.consume_effects.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class FoodeffecttooltipsClient implements ClientModInitializer {
	
	public static FoodEffectsConfig CONFIG;
	
	@Override
	public void onInitializeClient() {
		AutoConfig.register(FoodEffectsConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(FoodEffectsConfig.class).getConfig();
		
		ItemTooltipCallback.EVENT.register((stack, context, tooltipType, lines) -> {
			@Nullable Consumable foodComponent = stack.get(DataComponents.CONSUMABLE);
			if (foodComponent != null && shouldShowTooltip(stack)) {
				TooltipHelper.addFoodComponentEffectTooltip(stack, foodComponent, lines, context.tickRate());
			}
			
			if (FoodeffecttooltipsClient.CONFIG.ShowSuspiciousStewTooltips && !tooltipType.isCreative()) {
				@Nullable SuspiciousStewEffects sus = stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
				if (sus != null && !sus.effects().isEmpty()) {
					List<MobEffectInstance> list = new ArrayList<>();
					for (SuspiciousStewEffects.Entry stewEffect : sus.effects()) {
						list.add(stewEffect.createEffectInstance());
					}
					PotionContents.addPotionTooltip(list, lines::add, 1.0F, context.tickRate());
				}
			}
			
			@Nullable DeathProtection deathProtection = stack.get(DataComponents.DEATH_PROTECTION);
			if (deathProtection != null && shouldShowTooltip(stack)) {
				List<ConsumeEffect> consumeEffects = deathProtection.deathEffects();
				TooltipHelper.addConsumeEffectsTooltip(consumeEffects, lines, context.tickRate(), Component.translatable("foodeffecttooltips.food.whenTriggered"));
			}
		});
	}
	
	public static boolean shouldShowTooltip(ItemStack stack) {
		if (CONFIG == null) {
			return false;
		}
		
		Item item = stack.getItem();
		Identifier identifier = BuiltInRegistries.ITEM.getKey(item);
		
		boolean isWhitelist = CONFIG.UseAsWhitelistInstead;
		if (CONFIG.BlacklistedItemIdentifiers.contains(identifier.toString())) {
			return isWhitelist;
		}
		if (CONFIG.BlacklistedModsIDs.contains(identifier.getNamespace())) {
			return isWhitelist;
		}
		return !isWhitelist;
	}
	
}
