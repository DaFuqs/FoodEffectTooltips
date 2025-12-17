package de.dafuqs.foodeffecttooltips;

import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.core.component.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.component.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.event.entity.player.*;
import org.jetbrains.annotations.*;

import java.util.*;

@EventBusSubscriber(modid = FoodEffectTooltips.MOD_ID)
public class FoodEffectTooltipsEventHandler {
	
	private static float getTickRate() {
		ClientLevel world = Minecraft.getInstance().level;
		return world == null ? 20.0F : world.tickRateManager().tickrate();
	}
	
	@SubscribeEvent
	public static void onFoodEffectTooltips(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		
		@Nullable Consumable consumable = stack.get(DataComponents.CONSUMABLE);
		if (consumable != null && shouldShowTooltip(stack)) {
			TooltipHelper.addFoodComponentEffectTooltip(stack, consumable, event.getToolTip(), getTickRate());
		}
		
		if (FoodEffectTooltips.CONFIG.ShowSuspiciousStewTooltips && !event.getFlags().isCreative()) {
			@Nullable SuspiciousStewEffects sus = stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
			if (sus != null && !sus.effects().isEmpty()) {
				List<MobEffectInstance> list = new ArrayList<>();
				for (SuspiciousStewEffects.Entry stewEffect : sus.effects()) {
					list.add(stewEffect.createEffectInstance());
				}
				PotionContents.addPotionTooltip(list, event.getToolTip()::add, 1.0F, getTickRate());
			}
		}
	}
	
	public static boolean shouldShowTooltip(ItemStack stack) {
		if (FoodEffectTooltips.CONFIG == null) {
			return false;
		}
		
		Item item = stack.getItem();
		Identifier identifier = BuiltInRegistries.ITEM.getKey(item);
		
		boolean isWhitelist = FoodEffectTooltips.CONFIG.UseAsWhitelistInstead;
		if (FoodEffectTooltips.CONFIG.BlacklistedItemIdentifiers.contains(identifier.toString())) {
			return isWhitelist;
		}
		if (FoodEffectTooltips.CONFIG.BlacklistedModsIDs.contains(identifier.getNamespace())) {
			return isWhitelist;
		}
		return !isWhitelist;
	}
	
}
