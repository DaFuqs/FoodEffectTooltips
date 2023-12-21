package de.dafuqs.foodeffecttooltips;

import de.dafuqs.foodeffecttooltips.config.FoodEffectsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FoodeffecttooltipsClient implements ClientModInitializer {
	
	public static FoodEffectsConfig CONFIG;
	
	@Override
	public void onInitializeClient() {
		
		AutoConfig.register(FoodEffectsConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(FoodEffectsConfig.class).getConfig();
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if(stack.isOf(Items.SUSPICIOUS_STEW) && FoodeffecttooltipsClient.CONFIG.ShowSuspiciousStewTooltips) {
				List<StatusEffectInstance> effects = getStewEffects(stack);
				if(effects.size() > 0) {
					PotionUtil.buildTooltip(stack, lines, 1.0F);
				}
			} else if(stack.isFood() && shouldShowTooltip(stack)) {
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
	
	private static List<StatusEffectInstance> getStewEffects(ItemStack stew) {
		List<StatusEffectInstance> effects = new ArrayList<>();
		NbtCompound nbtCompound = stew.getNbt();
		if (nbtCompound != null && nbtCompound.contains(SuspiciousStewItem.EFFECTS_KEY, 9)) {
			NbtList nbtList = nbtCompound.getList(SuspiciousStewItem.EFFECTS_KEY, 10);
			
			for(int i = 0; i < nbtList.size(); ++i) {
				NbtCompound nbtCompound2 = nbtList.getCompound(i);
				int j = 160;
				if (nbtCompound2.contains(SuspiciousStewItem.EFFECT_DURATION_KEY, 3)) {
					j = nbtCompound2.getInt(SuspiciousStewItem.EFFECT_DURATION_KEY);
				}
				
				StatusEffect statusEffect = StatusEffect.byRawId(nbtCompound2.getInt(SuspiciousStewItem.EFFECT_ID_KEY));
				if (statusEffect != null) {
					effects.add(new StatusEffectInstance(statusEffect, j));
				}
			}
		}
		return effects;
	}
	
	
}
