package net.suel_ki.foodeffecttooltips;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import net.suel_ki.foodeffecttooltips.config.ConfigScreen;
import net.suel_ki.foodeffecttooltips.config.FoodEffectsConfig;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FoodEffectTooltips.MODID)
@Mod(FoodEffectTooltips.MODID)
public class FoodEffectTooltips
{
    public static final String MODID = "foodeffecttooltips";

    public FoodEffectTooltips()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FoodEffectsConfig.CLIENT_SPEC);
        if (ModList.get().isLoaded("cloth_config")) {
            ConfigScreen.register();
        }
    }

    @SubscribeEvent
    public static void onFoodEffectTooltips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack.getItem().isEdible() && TooltipHelper.shouldShowTooltip(stack))
            TooltipHelper.addFoodComponentEffectTooltip(stack, event.getToolTip());
    }

}
