package limitless.enchantments.mixin;

import java.util.List;
import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers.EnchantBookFactory;
import net.minecraft.village.TradedItem;

@Mixin(EnchantBookFactory.class)
public class EnchantmentFactoryMixin {
    @Final @Shadow private int experience = 0;
    @Final @Shadow private List<Enchantment> possibleEnchantments;
    @Final @Shadow private int minLevel;
    @Final @Shadow private int maxLevel;

    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    public void create(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> info) {
        if (LimitlessEnchantments.REBALANCED_TRADES_BOOLEAN) {
                Enchantment enchantment = chooseEnchantment(random, entity.getWorld().getEnabledFeatures());
                int i = Math.max(enchantment.getMinLevel(), this.minLevel);
                int j = Math.min(enchantment.getMaxLevel(), this.maxLevel);
                int k = MathHelper.nextInt(random, i, j);
                ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, k));
                int l = 0;

                int baseCost = 5 + random.nextInt(5);
                int scalingCost = (int) (Math.log(k + 1) * 10);

                l = baseCost + scalingCost;

                if (enchantment.isTreasure()) {
                    l *= 2;
                }

                l = Math.min(l, 64);

            info.setReturnValue(new TradeOffer(new TradedItem(Items.EMERALD, l), Optional.of(new TradedItem(Items.BOOK)), itemStack, 12, experience, 0.2F));
        }
    }

    private Enchantment chooseEnchantment(Random random, FeatureSet enabledFeatures) {
        List<Enchantment> list = this.possibleEnchantments.stream().filter((enchantment) -> {
            return enchantment.isEnabled(enabledFeatures);
        }).toList();
        return (Enchantment)list.get(random.nextInt(list.size()));
    }
}
