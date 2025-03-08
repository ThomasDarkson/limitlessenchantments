package limitless.enchantments.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import limitless.enchantments.LimitlessEnchantments;

@Mixin(Enchantment.class)
public class EnchantmentMixin{
	@Inject(at = @At("HEAD"), method = "getMaxLevel", cancellable = true)
	private void getMaxLevel(CallbackInfoReturnable<Integer> info) {
		int maximumLevel = LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL_INT;
		if (maximumLevel > 0) {
			info.setReturnValue(maximumLevel);
		}
	}

	@Inject(at = @At("HEAD"), method = "getName", cancellable = true)
	public void getName(int level, CallbackInfoReturnable<Text> info) {
		if (level > 5) {
			Enchantment e = (Enchantment) (Object) this;
			MutableText mutableText = Text.translatable(e.getTranslationKey());
			if (e.isCursed()) {
				Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.RED));
			} else {
				Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.GRAY));
			}

			if (!LimitlessEnchantments.SHOW_ACTUAL_NUMBERS_BOOLEAN)
 				mutableText.append(ScreenTexts.SPACE).append(LimitlessEnchantments.convertToRoman(level));
 			else
 				mutableText.append(ScreenTexts.SPACE).append(LimitlessEnchantments.convertToRoman(level) + " (" + level + ")");

			info.setReturnValue(mutableText);
		}
   	}

    @Inject(at = @At("HEAD"), method = "canCombine", cancellable = true)
	public void canCombine(Enchantment other, CallbackInfoReturnable<Boolean> info) {
		if (LimitlessEnchantments.NO_INCOMPATIBILITIES_BOOLEAN) {
			Enchantment enchantment = (Enchantment) (Object) this;
			boolean isFortuneAndSilkTouch = enchantment == Enchantments.FORTUNE && other == Enchantments.SILK_TOUCH;
			if (!isFortuneAndSilkTouch)
				isFortuneAndSilkTouch = enchantment == Enchantments.SILK_TOUCH && other == Enchantments.FORTUNE;
				
			info.setReturnValue(!isFortuneAndSilkTouch && enchantment != other);
		}
	}
}