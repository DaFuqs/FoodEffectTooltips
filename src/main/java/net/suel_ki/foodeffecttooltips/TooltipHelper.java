package net.suel_ki.foodeffecttooltips;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.UseAnim;

import com.mojang.datafixers.util.Pair;
import net.suel_ki.foodeffecttooltips.config.FoodEffectsConfig;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TooltipHelper {

    public static boolean shouldShowTooltip(ItemStack stack) {
        Item item = stack.getItem();
        ResourceLocation id = Registry.ITEM.getKey(item);

        boolean isWhitelist = FoodEffectsConfig.UseAsWhitelistInstead.get();
        if (FoodEffectsConfig.BlacklistedItemIdentifiers.get().contains(id.toString())) {
            return isWhitelist;
        }
        if (FoodEffectsConfig.BlacklistedModsIDs.get().contains(id.getNamespace())) {
            return isWhitelist;
        }
        return !isWhitelist;
    }

    public static void addFoodComponentEffectTooltip(ItemStack stack, List<Component> tooltip) {
        FoodProperties foodProperties = stack.getItem().getFoodProperties();
        if (foodProperties != null) {
            boolean isDrink = stack.getUseAnimation() == UseAnim.DRINK;
            if (stack.getItem() instanceof SuspiciousStewItem)
                buildStewFoodEffectTooltip(tooltip, stack.getTag(), isDrink);
            else buildFoodEffectTooltip(tooltip, foodProperties.getEffects(), isDrink);
        }
    }

    public static void buildFoodEffectTooltip(List<Component> tooltip, List<Pair<MobEffectInstance, Float>> effectsWithChance, boolean drink) {
        if (effectsWithChance.isEmpty()) {
            return;
        }

        List<Pair<Attribute, AttributeModifier>> modifiersList = Lists.newArrayList();
        MutableComponent translatableComponent;
        MobEffect mobEffect;
        for (Iterator<Pair<MobEffectInstance, Float>> var5 = effectsWithChance.iterator(); var5.hasNext(); tooltip.add(translatableComponent.withStyle(mobEffect.getCategory().getTooltipFormatting()))) {
            Pair<MobEffectInstance, Float> entry = var5.next();
            MobEffectInstance mobEffectInstance = entry.getFirst();
            Float chance = entry.getSecond();

            translatableComponent = new TranslatableComponent(mobEffectInstance.getDescriptionId());
            mobEffect = mobEffectInstance.getEffect();
            Map<Attribute, AttributeModifier> map = mobEffect.getAttributeModifiers();
            if (!map.isEmpty()) {
                for (Map.Entry<Attribute, AttributeModifier> entityAttributeEntityAttributeModifierEntry : map.entrySet()) {
                    AttributeModifier entityAttributeModifier = entityAttributeEntityAttributeModifierEntry.getValue();
                    AttributeModifier entityAttributeModifier2 = new AttributeModifier(entityAttributeModifier.getName(), mobEffect.getAttributeModifierValue(mobEffectInstance.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
                    modifiersList.add(new Pair<>(entityAttributeEntityAttributeModifierEntry.getKey(), entityAttributeModifier2));
                }
            }

            if (mobEffectInstance.getAmplifier() > 0) {
                translatableComponent = new TranslatableComponent("potion.withAmplifier", translatableComponent, new TranslatableComponent("potion.potency." + mobEffectInstance.getAmplifier()));
            }
            if (mobEffectInstance.getDuration() > 20) {
                translatableComponent = new TranslatableComponent("potion.withDuration", translatableComponent, StringUtil.formatTickDuration(mobEffectInstance.getDuration()));
            }
            if (chance < 1.0F) {
                translatableComponent = new TranslatableComponent("foodeffecttooltips.food.withChance", translatableComponent, Math.round(chance * 100));
            }
        }

        if (!modifiersList.isEmpty()) {
            tooltip.add(TextComponent.EMPTY);
            if (drink) {
                tooltip.add(new TranslatableComponent("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
            } else {
                tooltip.add(new TranslatableComponent("foodeffecttooltips.food.whenEaten").withStyle(ChatFormatting.DARK_PURPLE));
            }

            for (Pair<Attribute, AttributeModifier> entityAttributeEntityAttributeModifierPair : modifiersList) {
                AttributeModifier entityAttributeModifier3 = entityAttributeEntityAttributeModifierPair.getSecond();
                double d = entityAttributeModifier3.getAmount();
                double e;
                if (entityAttributeModifier3.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && entityAttributeModifier3.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    e = entityAttributeModifier3.getAmount();
                } else {
                    e = entityAttributeModifier3.getAmount() * 100.0D;
                }

                if (d > 0.0D) {
                    tooltip.add((new TranslatableComponent("attribute.modifier.plus." + entityAttributeModifier3.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(e), new TranslatableComponent((entityAttributeEntityAttributeModifierPair.getFirst()).getDescriptionId()))).withStyle(ChatFormatting.BLUE));
                } else if (d < 0.0D) {
                    e *= -1.0D;
                    tooltip.add((new TranslatableComponent("attribute.modifier.take." + entityAttributeModifier3.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(e), new TranslatableComponent((entityAttributeEntityAttributeModifierPair.getFirst()).getDescriptionId()))).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    public static void buildStewFoodEffectTooltip(List<Component> tooltip, CompoundTag compoundTag, boolean drink) {
        List<Pair<MobEffectInstance, Float>> effects = Lists.newArrayList();
        if (compoundTag != null && compoundTag.contains(SuspiciousStewItem.EFFECTS_TAG, ListTag.TAG_LIST)) {
            ListTag listtag = compoundTag.getList(SuspiciousStewItem.EFFECTS_TAG, ListTag.TAG_COMPOUND);

            for (int i = 0; i < listtag.size(); ++i) {
                int duration = 160;
                CompoundTag effectTag = listtag.getCompound(i);
                if (effectTag.contains(SuspiciousStewItem.EFFECT_DURATION_TAG, ListTag.TAG_INT)) {
                    duration = effectTag.getInt(SuspiciousStewItem.EFFECT_DURATION_TAG);
                }

                MobEffect mobeffect = MobEffect.byId(effectTag.getByte(SuspiciousStewItem.EFFECT_ID_TAG));
                mobeffect = net.minecraftforge.common.ForgeHooks.loadMobEffect(effectTag, "forge:effect_id", mobeffect);
                if (mobeffect != null) {
                    effects.add(Pair.of(new MobEffectInstance(mobeffect, duration), 1.0F));
                    buildFoodEffectTooltip(tooltip, effects, drink);
                }
            }
        }
    }
}
