package limitless.enchantments.mixin;

import java.util.List;
import java.util.stream.Collectors;

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
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers.EnchantBookFactory;

@Mixin(EnchantBookFactory.class)
public class EnchantmentFactoryMixin {
    @Final @Shadow private int experience = 0;

    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    public void create(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> info) {
        if (LimitlessEnchantments.REBALANCED_TRADES_BOOLEAN) {
                List<Enchantment> list = (List<Enchantment>)Registries.ENCHANTMENT.stream().filter(Enchantment::isAvailableForEnchantedBookOffer).collect(Collectors.toList());
                Enchantment enchantment = (Enchantment)list.get(random.nextInt(list.size()));
                int k = MathHelper.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
                ItemStack itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, k));
                int l = 0;

                int baseCost = 5 + random.nextInt(5);
                int scalingCost = (int) (Math.log(k + 1) * 10);

                l = baseCost + scalingCost;

                if (enchantment.isTreasure()) {
                    l *= 2;
                }

                l = Math.min(l, 64);

            info.setReturnValue(new TradeOffer(new ItemStack(Items.EMERALD, l), new ItemStack(Items.BOOK), itemStack, 12, this.experience, 0.2F));
        }
    }
}
