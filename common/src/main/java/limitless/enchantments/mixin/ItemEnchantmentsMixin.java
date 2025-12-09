package limitless.enchantments.mixin;

import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsMixin {
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = 255))
    public int init(int i) {
        return Integer.MAX_VALUE;
    }

    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 255))
    private static int clinit(int i) {
        return Integer.MAX_VALUE;
    }
}
