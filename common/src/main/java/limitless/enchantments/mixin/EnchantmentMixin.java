package limitless.enchantments.mixin;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Inject(at = @At("TAIL"), method = "getMaxLevel", cancellable = true)
	private void getMaxLevel(CallbackInfoReturnable<Integer> info) {
		Enchantment e = (Enchantment) (Object) this;
		if (LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL > 0) {
			ArrayList<Enchantment> list = new ArrayList<>();
			LimitlessEnchantments.blackListedEnchantmentsMap.forEach((s, en) -> {
				list.add(en);
			});
			
			boolean isBlacklisted = false;
			for (Enchantment l : list) {
				if (LimitlessEnchantments.compareEnchantments(e, l)) {
					isBlacklisted = true;
					break;
				}
			}

			info.setReturnValue(isBlacklisted ? info.getReturnValue() : LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL);
		}
	}

	@Inject(at = @At("HEAD"), method = "getFullname", cancellable = true)
	private static void getName(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Component> info) {
		Enchantment e = enchantment.value();
		if (LimitlessEnchantments.blackListedEnchantments.contains(enchantment.getRegisteredName()) && e.definition().maxLevel() == 1 && e.getMaxLevel() != 1) {
			MutableComponent mutableText = ((Enchantment)enchantment.value()).description().copy();
			if (enchantment.is(EnchantmentTags.CURSE)) {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.RED));
			} else {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.GRAY));
			}		
			
			info.setReturnValue(mutableText);
		}
		if (level > 5 && !LimitlessEnchantments.blackListedEnchantments.contains(enchantment.getRegisteredName())) {
			MutableComponent mutableText = ((Enchantment)enchantment.value()).description().copy();
			if (enchantment.is(EnchantmentTags.CURSE)) {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.RED));
			} else {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.GRAY));
			}

			if (level != 1 || ((Enchantment)enchantment.value()).getMaxLevel() != 1) {
				if (!LimitlessEnchantments.SHOW_ACTUAL_NUMBERS)
					mutableText.append(CommonComponents.SPACE).append(LimitlessEnchantments.convertToRoman(level));
				else
					mutableText.append(CommonComponents.SPACE).append(LimitlessEnchantments.convertToRoman(level) + " (" + (LimitlessEnchantments.formatInt(level)) + ")");
			}

			info.setReturnValue(mutableText);
		}
   	}

	@Inject(at = @At("HEAD"), method = "areCompatible", cancellable = true)
	private static void canBeCombined(Holder<Enchantment> first, Holder<Enchantment> second, CallbackInfoReturnable<Boolean> info) {
		if (LimitlessEnchantments.NO_INCOMPATIBILITIES) {
			boolean isFortuneAndSilkTouch = false; try { 
				var firstKey = first.unwrapKey().get();
				var secondKey = second.unwrapKey().get();

				isFortuneAndSilkTouch = (firstKey.equals(Enchantments.FORTUNE) && secondKey.equals(Enchantments.SILK_TOUCH)) || (firstKey.equals(Enchantments.SILK_TOUCH) && secondKey.equals(Enchantments.FORTUNE));
			} catch(Exception e) { 
				isFortuneAndSilkTouch = true;
			}

			info.setReturnValue(!first.equals(second) && !isFortuneAndSilkTouch);
		}
	}
}