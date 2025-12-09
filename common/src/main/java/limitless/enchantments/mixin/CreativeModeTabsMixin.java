package limitless.enchantments.mixin;

import java.util.Set;
import java.util.stream.IntStream;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {
    @Overwrite
    private static void generateEnchantmentBookTypesOnlyMaxLevel(CreativeModeTab.Output output, HolderLookup<Enchantment> enchantments, Set<EnchantmentCategory> categories, CreativeModeTab.TabVisibility tabVisibility) {
        enchantments.listElements()
                .map(Holder::value)
                .filter((enchantment) -> categories.contains(enchantment.category))
                .map((enchantment) -> {
                    int clampedLevel = Math.min(
                            enchantment.getMaxLevel(),
                            255
                    );
                    return EnchantedBookItem.createForEnchantment(
                            new EnchantmentInstance(enchantment, clampedLevel)
                    );
                })
                .forEach((itemStack) -> output.accept(itemStack, tabVisibility));
    }

    @Overwrite
    private static void generateEnchantmentBookTypesAllLevels(CreativeModeTab.Output output, HolderLookup<Enchantment> enchantments, Set<EnchantmentCategory> categories, CreativeModeTab.TabVisibility tabVisibility) {
        enchantments.listElements()
                .map(Holder::value)
                .filter((enchantment) -> categories.contains(enchantment.category))
                .flatMap((enchantment) -> {
                    int minLevel = enchantment.getMinLevel();
                    int maxLevel = Math.min(
                            enchantment.getMaxLevel(),
                            255
                    );

                    IntStream levels = (maxLevel < minLevel)
                            ? IntStream.empty()
                            : IntStream.rangeClosed(minLevel, maxLevel);

                    return levels.mapToObj((level) ->
                            EnchantedBookItem.createForEnchantment(
                                    new EnchantmentInstance(enchantment, level)
                            )
                    );
                })
                .forEach((itemStack) -> output.accept(itemStack, tabVisibility));
    }
}
