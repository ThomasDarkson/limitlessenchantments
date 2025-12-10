package limitless.enchantments.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

@SuppressWarnings("unused")
@Mixin(targets = {"net.minecraft.world.entity.npc.villager.VillagerTrades.EnchantBookForEmeralds"})
public class EnchantBookForEmeraldsMixin {
    @Final @Mutable @Shadow private int villagerXp;
    @Final @Mutable @Shadow private TagKey<Enchantment> tradeableEnchantments;
    @Final @Mutable @Shadow private int minLevel;
    @Final @Mutable @Shadow private int maxLevel;

    @Inject(at = @At("HEAD"), method = "getOffer", cancellable = true)
    public void create(Entity entity, RandomSource random, CallbackInfoReturnable<MerchantOffer> info) {
            Optional<Holder<Enchantment>> optional = entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getRandomElementOf(this.tradeableEnchantments, random);
            int l;
            ItemStack itemStack;

            if (!optional.isEmpty()) {
                Holder<Enchantment> registryEntry = optional.get();
                Enchantment enchantment = registryEntry.value();
                int i = Math.max(enchantment.getMinLevel(), this.minLevel);
                int j = Math.min(enchantment.getMaxLevel(), this.maxLevel);
                if (LimitlessEnchantments.blackListedEnchantments.contains(registryEntry.getRegisteredName()))
                    j = enchantment.definition().maxLevel();

                int k = Mth.nextInt(random, i, j);
                if (LimitlessEnchantments.MAX_TRADE_LEVEL > 0)
                    k = Mth.nextInt(random, Math.min(i, LimitlessEnchantments.MAX_TRADE_LEVEL), LimitlessEnchantments.MAX_TRADE_LEVEL);
                else if (LimitlessEnchantments.MAX_TRADE_LEVEL == -1)
                    k = Mth.nextInt(random, i, Math.min(enchantment.definition().maxLevel(), this.maxLevel));

                itemStack = EnchantmentHelper.createBook(new EnchantmentInstance(registryEntry, k));

                int baseCost = 5 + random.nextInt(5);
                int scalingCost = (int) (Math.log(k + 1) * 10);

                if (LimitlessEnchantments.REBALANCED_TRADES) {
                    l = baseCost + scalingCost;

                    if (registryEntry.is(EnchantmentTags.DOUBLE_TRADE_PRICE)) {
                        l *= 2;
                    }
                } else {
                    l = 2 + random.nextInt(5 + k * 10) + 3 * k;
                    if (registryEntry.is(EnchantmentTags.DOUBLE_TRADE_PRICE)) {
                        l *= 2;
                    }
                }

                l = Math.min(l, 64);
            } else {
                l = 1;
                itemStack = new ItemStack(Items.BOOK);
            }

            info.setReturnValue(new MerchantOffer(new ItemCost(Items.EMERALD, l), Optional.of(new ItemCost(Items.BOOK)), itemStack, 12, villagerXp, 0.2F));
    }
}
