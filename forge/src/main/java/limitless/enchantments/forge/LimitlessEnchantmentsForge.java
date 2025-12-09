package limitless.enchantments.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import limitless.enchantments.LimitlessEnchantments;

@Mod(LimitlessEnchantments.MOD_ID)
public final class LimitlessEnchantmentsForge {
    @SuppressWarnings("removal")
    public LimitlessEnchantmentsForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(LimitlessEnchantments.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        LimitlessEnchantments.init();
    }
}
