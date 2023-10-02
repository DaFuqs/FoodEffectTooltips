package de.dafuqs.foodeffecttooltips;

import com.google.common.collect.*;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.text.*;
import net.minecraft.util.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TooltipHelper {
	
	public static void addFoodComponentEffectTooltip(ItemStack stack, List<Text> tooltip) {
		FoodComponent foodComponent = stack.getItem().getFoodComponent();
		if (foodComponent != null) {
			boolean isDrink = stack.getUseAction() == UseAction.DRINK;
			buildFoodEffectTooltip(tooltip, foodComponent.getStatusEffects(), isDrink);
		}
	}
	
	public static void buildFoodEffectTooltip(List<Text> tooltip, List<Pair<StatusEffectInstance, Float>> effectsWithChance, boolean drink) {
		if (effectsWithChance.isEmpty()) {
			return;
		}
		
		List<Pair<EntityAttribute, EntityAttributeModifier>> modifiersList = Lists.newArrayList();
		MutableText translatableText;
		StatusEffect statusEffect;
		for (Iterator<Pair<StatusEffectInstance, Float>> var5 = effectsWithChance.iterator(); var5.hasNext(); tooltip.add(translatableText.formatted(statusEffect.getCategory().getFormatting()))) {
			Pair<StatusEffectInstance, Float> entry = var5.next();
			StatusEffectInstance statusEffectInstance = entry.getFirst();
			Float chance = entry.getSecond();
			
			translatableText = Text.translatable(statusEffectInstance.getTranslationKey());
			statusEffect = statusEffectInstance.getEffectType();
			Map<EntityAttribute, AttributeModifierCreator> map = statusEffect.getAttributeModifiers();
			if (!map.isEmpty()) {
				for (Map.Entry<EntityAttribute, AttributeModifierCreator> entityAttributeEntityAttributeModifierEntry : map.entrySet()) {
					modifiersList.add(new Pair<>(entityAttributeEntityAttributeModifierEntry.getKey(), entityAttributeEntityAttributeModifierEntry.getValue().createAttributeModifier(statusEffectInstance.getAmplifier())));
				}
			}
			
			if (statusEffectInstance.getAmplifier() > 0) {
				translatableText = Text.translatable("potion.withAmplifier", translatableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
			}
			if (statusEffectInstance.getDuration() > 20) {
				translatableText = Text.translatable("potion.withDuration", translatableText, StringHelper.formatTicks(statusEffectInstance.getDuration()));
			}
			if (chance < 1.0F) {
				translatableText = Text.translatable("foodeffecttooltips.food.withChance", translatableText, Math.round(chance * 100));
			}
		}
		
		if (!modifiersList.isEmpty()) {
			tooltip.add(ScreenTexts.EMPTY);
			if (drink) {
				tooltip.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));
			} else {
				tooltip.add(Text.translatable("foodeffecttooltips.food.whenEaten").formatted(Formatting.DARK_PURPLE));
			}
			
			for (Pair<EntityAttribute, EntityAttributeModifier> entityAttributeEntityAttributeModifierPair : modifiersList) {
				EntityAttributeModifier entityAttributeModifier3 = entityAttributeEntityAttributeModifierPair.getSecond();
				double d = entityAttributeModifier3.getValue();
				double e;
				if (entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_BASE && entityAttributeModifier3.getOperation() != EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
					e = entityAttributeModifier3.getValue();
				} else {
					e = entityAttributeModifier3.getValue() * 100.0D;
				}
				
				if (d > 0.0D) {
					tooltip.add((Text.translatable("attribute.modifier.plus." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.BLUE));
				} else if (d < 0.0D) {
					e *= -1.0D;
					tooltip.add((Text.translatable("attribute.modifier.take." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable((entityAttributeEntityAttributeModifierPair.getFirst()).getTranslationKey()))).formatted(Formatting.RED));
				}
			}
		}
	}
	
}
