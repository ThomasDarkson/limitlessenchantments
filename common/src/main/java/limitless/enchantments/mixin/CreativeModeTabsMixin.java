package limitless.enchantments.mixin;

import java.util.stream.IntStream;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    @Overwrite
    private static void generateEnchantmentBookTypesOnlyMaxLevel(CreativeModeTab.Output entries, HolderLookup<Enchantment> registryWrapper, CreativeModeTab.TabVisibility stackVisibility) {
        registryWrapper.listElements().map((enchantmentEntry) -> {
            return EnchantmentHelper.createBook(new EnchantmentInstance(enchantmentEntry, Math.min(255, ((Enchantment)enchantmentEntry.value()).getMaxLevel())));
        }).forEach((stack) -> {
            entries.accept(stack, stackVisibility);
        });
    }

    @Overwrite
    private static void generateEnchantmentBookTypesAllLevels(CreativeModeTab.Output entries, HolderLookup<Enchantment> registryWrapper, CreativeModeTab.TabVisibility stackVisibility) {
        registryWrapper.listElements().flatMap((enchantmentEntry) -> {
            return IntStream.rangeClosed(((Enchantment)enchantmentEntry.value()).getMinLevel(), Math.min(255, ((Enchantment)enchantmentEntry.value()).getMaxLevel())).mapToObj((level) -> {
                return EnchantmentHelper.createBook(new EnchantmentInstance(enchantmentEntry, level));
            });
        }).forEach((stack) -> {
            entries.accept(stack, stackVisibility);
        });
    }
}
