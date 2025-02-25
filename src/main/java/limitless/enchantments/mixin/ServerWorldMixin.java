package limitless.enchantments.mixin;

import java.util.function.BooleanSupplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        @SuppressWarnings("resource")
        ServerWorld world = (ServerWorld) (Object) this;

        LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL_INT = world.getGameRules().getInt(LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL);
        LimitlessEnchantments.NO_INCOMPATIBILITIES_BOOLEAN = world.getGameRules().getBoolean(LimitlessEnchantments.NO_INCOMPATIBILITIES);
        LimitlessEnchantments.REBALANCED_TRADES_BOOLEAN = world.getGameRules().getBoolean(LimitlessEnchantments.REBALANCED_TRADES);
    }
}
