package limitless.enchantments.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    @ModifyConstant(method = "renderLabels", constant = @Constant(intValue = 40), require = 0)
    public int renderLabels(int i) {
        return LimitlessEnchantments.ANVIL_EXPERIENCE_COST_LIMIT;
    }
}
