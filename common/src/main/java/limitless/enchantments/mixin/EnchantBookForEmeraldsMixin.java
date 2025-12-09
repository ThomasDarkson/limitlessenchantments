package limitless.enchantments.mixin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import limitless.enchantments.EnchantmentInterface;
import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;

@SuppressWarnings("unused")
@Mixin(targets = {"net.minecraft.world.entity.npc.VillagerTrades.EnchantBookForEmeralds"})
public class EnchantBookForEmeraldsMixin {
    @Final @Mutable @Shadow private int villagerXp;

    @Inject(at = @At("HEAD"), method = "getOffer", cancellable = true)
    public void create(Entity entity, RandomSource random, CallbackInfoReturnable<MerchantOffer> info) {
            List<Enchantment> list = (List)BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
            Enchantment enchantment = (Enchantment)list.get(random.nextInt(list.size()));
            int l;
            ItemStack itemStack;

            int j = enchantment.getMaxLevel();
            if (LimitlessEnchantments.blackListedEnchantments.contains(LimitlessEnchantments.registeredName(enchantment)))
                j = ((EnchantmentInterface) enchantment).getOgMaxLevel();

            int k = Mth.nextInt(random, enchantment.getMinLevel(), j);
            if (LimitlessEnchantments.MAX_TRADE_LEVEL > 0)
                k = Mth.nextInt(random, Math.min(enchantment.getMinLevel(), LimitlessEnchantments.MAX_TRADE_LEVEL), LimitlessEnchantments.MAX_TRADE_LEVEL);
            else if (LimitlessEnchantments.MAX_TRADE_LEVEL == -1)
                k = Mth.nextInt(random, enchantment.getMinLevel(), ((EnchantmentInterface) enchantment).getOgMaxLevel());

            itemStack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, k));

            int baseCost = 5 + random.nextInt(5);
            int scalingCost = (int) (Math.log(k + 1) * 10);

            if (LimitlessEnchantments.REBALANCED_TRADES) {
                l = baseCost + scalingCost;

                if (enchantment.isTreasureOnly()) {
                    l *= 2;
                }
            } else {
                l = 2 + random.nextInt(5 + k * 10) + 3 * k;
                if (enchantment.isTreasureOnly()) {
                    l *= 2;
                }
            }

            l = Math.min(l, 64);

        info.setReturnValue(new MerchantOffer(new ItemStack(Items.EMERALD, l), new ItemStack(Items.BOOK), itemStack, 12, this.villagerXp, 0.2F));
    }
}
