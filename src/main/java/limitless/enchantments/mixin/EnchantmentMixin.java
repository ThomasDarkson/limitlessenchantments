package limitless.enchantments.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
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
	private static void getName(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> info) {
		if (level > 5) {
			MutableText mutableText = ((Enchantment)enchantment.value()).description().copy();
			if (enchantment.isIn(EnchantmentTags.CURSE)) {
				Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.RED));
			} else {
				Texts.setStyleIfAbsent(mutableText, Style.EMPTY.withColor(Formatting.GRAY));
			}

			if (level != 1 || ((Enchantment)enchantment.value()).getMaxLevel() != 1) {
				mutableText.append(ScreenTexts.SPACE).append(LimitlessEnchantments.convertToRoman(level) + " (" + level + ")");
			}

			info.setReturnValue(mutableText);
		}
   	}

	@Inject(at = @At("HEAD"), method = "canBeCombined", cancellable = true)
	private static void canBeCombined(RegistryEntry<Enchantment> first, RegistryEntry<Enchantment> second, CallbackInfoReturnable<Boolean> info) {
		if (LimitlessEnchantments.NO_INCOMPATIBILITIES_BOOLEAN) {
			boolean isFortuneAndSilkTouch = false; try { 
				var firstKey = first.getKey().get();
				var secondKey = second.getKey().get();

				isFortuneAndSilkTouch = (firstKey.equals(Enchantments.FORTUNE) && secondKey.equals(Enchantments.SILK_TOUCH)) || (firstKey.equals(Enchantments.SILK_TOUCH) && secondKey.equals(Enchantments.FORTUNE));
			} catch(Exception e) { 
				isFortuneAndSilkTouch = true;
			}

			info.setReturnValue(!first.equals(second) && !isFortuneAndSilkTouch);
		}
	}
}