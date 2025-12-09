package limitless.enchantments.mixin;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ArrowPiercingEnchantment;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import limitless.enchantments.EnchantmentInterface;
import limitless.enchantments.LimitlessEnchantments;

@Mixin(ArrowPiercingEnchantment.class)
public class ArrowPiercingEnchantmentMixin {
    @Inject(at = @At("TAIL"), method = "getMaxLevel", cancellable = true)
    private void getMaxLevel(CallbackInfoReturnable<Integer> info) {
        Enchantment e = (Enchantment) (Object) this;
        if (((EnchantmentInterface) e).getOptionalInt().isEmpty())
            ((EnchantmentInterface) e).setOgMaxLevel(info.getReturnValue());

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
}
