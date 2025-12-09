package limitless.enchantments.mixin;

import java.util.ArrayList;
import java.util.OptionalInt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import limitless.enchantments.EnchantmentInterface;
import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EnchantmentInterface {
	private OptionalInt originalMaxLevel = OptionalInt.empty();

	@Inject(at = @At("TAIL"), method = "getMaxLevel", cancellable = true)
	private void getMaxLevel(CallbackInfoReturnable<Integer> info) {
		Enchantment e = (Enchantment) (Object) this;
		if (originalMaxLevel.isEmpty())
			originalMaxLevel = OptionalInt.of(info.getReturnValue());

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
	public void getName(int level, CallbackInfoReturnable<Component> info) {
		Enchantment e = (Enchantment) (Object) this;
		if (LimitlessEnchantments.blackListedEnchantments.contains(LimitlessEnchantments.registeredName(e)) && e.getMaxLevel() == 1 && e.getMaxLevel() != 1) {
			MutableComponent mutableText = Component.translatable(e.getDescriptionId());
			if (e.isCurse()) {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.RED));
			} else {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.GRAY));
			}		
			
			info.setReturnValue(mutableText);
		}
		if (level > 5 && !LimitlessEnchantments.blackListedEnchantments.contains(LimitlessEnchantments.registeredName(e))) {
			MutableComponent mutableText = Component.translatable(e.getDescriptionId());
			if (e.isCurse()) {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.RED));
			} else {
				ComponentUtils.mergeStyles(mutableText, Style.EMPTY.withColor(ChatFormatting.GRAY));
			}

			if (level != 1 || e.getMaxLevel() != 1) {
				if (!LimitlessEnchantments.SHOW_ACTUAL_NUMBERS)
					mutableText.append(CommonComponents.SPACE).append(LimitlessEnchantments.convertToRoman(level));
				else
					mutableText.append(CommonComponents.SPACE).append(LimitlessEnchantments.convertToRoman(level) + " (" + (LimitlessEnchantments.formatInt(level)) + ")");
			}

			info.setReturnValue(mutableText);
		}
   	}

	@Inject(at = @At("HEAD"), method = "checkCompatibility", cancellable = true)
	protected void checkCompatibility(Enchantment other, CallbackInfoReturnable<Boolean> info) {
		Enchantment e = (Enchantment) (Object) this;
		if (LimitlessEnchantments.NO_INCOMPATIBILITIES) {
			boolean isFortuneAndSilkTouch = false; try { 
				var firstKey = BuiltInRegistries.ENCHANTMENT.getKey(e);
				var secondKey = BuiltInRegistries.ENCHANTMENT.getKey(other);

				isFortuneAndSilkTouch = (firstKey.equals(BuiltInRegistries.ENCHANTMENT.getKey(Enchantments.BLOCK_FORTUNE)) && secondKey.equals(BuiltInRegistries.ENCHANTMENT.getKey(Enchantments.SILK_TOUCH))) || (firstKey.equals(BuiltInRegistries.ENCHANTMENT.getKey(Enchantments.SILK_TOUCH)) && secondKey.equals(BuiltInRegistries.ENCHANTMENT.getKey(Enchantments.BLOCK_FORTUNE)));
			} catch(Exception ex) { 
				isFortuneAndSilkTouch = true;
			}

			info.setReturnValue(!e.equals(other) && !isFortuneAndSilkTouch);
		}
	}

	@Override
	public int getOgMaxLevel() {
		return originalMaxLevel.isEmpty() ? 5 : originalMaxLevel.getAsInt();
	}

	@Override
	public void setOgMaxLevel(int level) {
		originalMaxLevel = OptionalInt.of(level);
	}

	@Override
	public OptionalInt getOptionalInt() {
		return this.originalMaxLevel;
	}
}