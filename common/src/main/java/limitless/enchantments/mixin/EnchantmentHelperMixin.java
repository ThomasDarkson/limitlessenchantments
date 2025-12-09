package limitless.enchantments.mixin;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyConstant(method = "getEnchantmentLevel", constant = @Constant(intValue = 255))
    private static int getEnchantmentLevel(int i) {
        return Integer.MAX_VALUE;
    }
}
