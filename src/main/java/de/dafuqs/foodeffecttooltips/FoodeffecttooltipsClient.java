package de.dafuqs.foodeffecttooltips;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class FoodeffecttooltipsClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
			if(stack.isFood()) {
				TooltipHelper.addFoodComponentEffectTooltip(stack, lines);
			}
		});
	}
}
