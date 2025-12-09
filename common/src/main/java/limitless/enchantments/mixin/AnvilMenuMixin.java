package limitless.enchantments.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {  
    @Final @Mutable @Shadow private DataSlot cost;

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40), require = 0)
    public int updateResult(int i) {
        return LimitlessEnchantments.ANVIL_EXPERIENCE_COST_LIMIT;
    }

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 39), require = 0)
    public int updateResultTwo(int i) {
        return LimitlessEnchantments.ANVIL_EXPERIENCE_COST_LIMIT - 1;
    }

    @Inject(at = @At("TAIL"), method = "createResult")
    public void createResult(CallbackInfo info) {
        if (LimitlessEnchantments.FIXED_ANVIL_COST != -1) {
            cost.set(LimitlessEnchantments.FIXED_ANVIL_COST);
        }
    }
}
