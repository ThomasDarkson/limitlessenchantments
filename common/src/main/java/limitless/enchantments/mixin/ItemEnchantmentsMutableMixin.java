package limitless.enchantments.mixin;

import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(ItemEnchantments.Mutable.class)
public class ItemEnchantmentsMutableMixin {
    @ModifyConstant(method = "set", constant = @Constant(intValue = 255))
    public int set(int i) {
        return Integer.MAX_VALUE;
    }

    @ModifyConstant(method = "upgrade", constant = @Constant(intValue = 255))
    public int add(int i) {
        return Integer.MAX_VALUE;
    }
}
