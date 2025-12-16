package de.dafuqs.foodeffecttooltips;

import com.google.common.collect.*;
import com.mojang.datafixers.util.*;
import net.fabricmc.api.*;
import net.minecraft.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.consume_effects.*;
import org.jetbrains.annotations.*;

import java.util.*;

@Environment(EnvType.CLIENT)
public class TooltipHelper {
	
	public static void addFoodComponentEffectTooltip(@NotNull ItemStack stack, @NotNull Consumable consumableComponent, @NotNull List<Component> tooltip, float tickRate) {
		if (consumableComponent.onConsumeEffects().isEmpty()) {
			return;
		}
		
		MutableComponent c = stack.getUseAnimation() == ItemUseAnimation.DRINK ? Component.translatable("potion.whenDrank") : Component.translatable("foodeffecttooltips.food.whenEaten");
		addConsumeEffectsTooltip(consumableComponent.onConsumeEffects(), tooltip, tickRate, c);
	}
	
	public static void addConsumeEffectsTooltip(@NotNull List<ConsumeEffect> consumeEffects, @NotNull List<Component> tooltip, float tickRate, MutableComponent component) {
		buildFoodEffectTooltip(tooltip, consumeEffects, tickRate, component);
	}
	
	private static void buildFoodEffectTooltip(@NotNull List<Component> tooltip, List<ConsumeEffect> effects, float tickRate, MutableComponent description) {
		List<Pair<Holder<Attribute>, AttributeModifier>> modifiers = Lists.newArrayList();
		
		MutableComponent mutableText;
		Holder<MobEffect> registryEntry;
		for (ConsumeEffect entry : effects) {
			if (!(entry instanceof ApplyStatusEffectsConsumeEffect applyEffectsConsumeEffect)) {
				continue;
			}
			
			for (MobEffectInstance statusEffectInstance : applyEffectsConsumeEffect.effects()) {
				mutableText = Component.translatable(statusEffectInstance.getDescriptionId());
				registryEntry = statusEffectInstance.getEffect();
				registryEntry.value().createModifiers(statusEffectInstance.getAmplifier(), (attribute, modifier) -> {
					modifiers.add(new Pair<>(attribute, modifier));
				});
				if (statusEffectInstance.getAmplifier() > 0) {
					mutableText = Component.translatable("potion.withAmplifier", mutableText, Component.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
				}
				
				if (!statusEffectInstance.endsWithin(20)) {
					mutableText = Component.translatable("potion.withDuration", mutableText, MobEffectUtil.formatDuration(statusEffectInstance, 1.0F, tickRate));
				}
				if (applyEffectsConsumeEffect.probability() < 1.0F) {
					mutableText = Component.translatable("foodeffecttooltips.food.withChance", mutableText, Math.round(applyEffectsConsumeEffect.probability() * 100));
				}
				
				tooltip.add(mutableText.withStyle(registryEntry.value().getCategory().getTooltipFormatting()));
			}
		}
		
		if (!modifiers.isEmpty()) {
			tooltip.add(CommonComponents.EMPTY);
			tooltip.add(description.withStyle(ChatFormatting.DARK_PURPLE));
			
			for (Pair<Holder<Attribute>, AttributeModifier> modifier : modifiers) {
				AttributeModifier entityAttributeModifier = modifier.getSecond();
				double d = entityAttributeModifier.amount();
				double e;
				if (entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && entityAttributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
					e = entityAttributeModifier.amount();
				} else {
					e = entityAttributeModifier.amount() * 100.0;
				}
				
				if (d > 0.0) {
					tooltip.add(Component.translatable("attribute.modifier.plus." + entityAttributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e), Component.translatable(modifier.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.BLUE));
				} else if (d < 0.0) {
					e *= -1.0;
					tooltip.add(Component.translatable("attribute.modifier.take." + entityAttributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(e), Component.translatable(modifier.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.RED));
				}
			}
		}
	}
	
}
