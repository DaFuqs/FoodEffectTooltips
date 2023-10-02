package de.dafuqs.foodeffecttooltips;

import de.dafuqs.foodeffecttooltips.config.*;
import me.shedaniel.autoconfig.*;
import me.shedaniel.autoconfig.serializer.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.item.v1.*;
import net.minecraft.block.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.registry.*;
import net.minecraft.util.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class FoodeffecttooltipsClient implements ClientModInitializer {
	
	public static FoodEffectsConfig CONFIG;
	
	@Override
	public void onInitializeClient() {
		
		AutoConfig.register(FoodEffectsConfig.class, JanksonConfigSerializer::new);
		CONFIG = AutoConfig.getConfigHolder(FoodEffectsConfig.class).getConfig();
		
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if (stack.isOf(Items.SUSPICIOUS_STEW) && FoodeffecttooltipsClient.CONFIG.ShowSuspiciousStewTooltips && !context.isCreative()) {
				List<StatusEffectInstance> list = new ArrayList();
				NbtCompound nbtCompound = stack.getNbt();
				if (nbtCompound != null && nbtCompound.contains("effects", 9)) {
					SuspiciousStewIngredient.StewEffect.LIST_CODEC.parse(NbtOps.INSTANCE, nbtCompound.getList("effects", 10)).result().ifPresent((list1) -> {
						list1.forEach((effect) -> {
							list.add(effect.createStatusEffectInstance());
						});
					});
				}
				PotionUtil.buildTooltip(list, lines, 1.0F);
			} else if (stack.isFood() && shouldShowTooltip(stack)) {
				TooltipHelper.addFoodComponentEffectTooltip(stack, lines);
			}
		});
	}
	
	public static boolean shouldShowTooltip(ItemStack stack) {
		if (CONFIG == null) {
			return false;
		}
		
		Item item = stack.getItem();
		Identifier identifier = Registries.ITEM.getId(item);
		
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
