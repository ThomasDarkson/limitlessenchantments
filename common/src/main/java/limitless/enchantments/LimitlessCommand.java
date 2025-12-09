package limitless.enchantments;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.Enchantment;
import semantic.ver.lib.SemanticVersion;

import static net.minecraft.commands.Commands.*;

import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import dev.architectury.event.events.common.CommandRegistrationEvent;

public class LimitlessCommand {
    public static final void init() {
        CommandRegistrationEvent.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("limitless").
            executes(context -> {
                context.getSource().sendSuccess(() -> { 
                    return Component.literal("Limitless Enchantments v" + LimitlessEnchantments.VERSION.toString());
                }, true);
                checkForUpdates(context);
                context.getSource().sendSuccess(() -> Component.translatable("limitless.thank.you"), true);
                return 0;
            })
            .then(literal("maxEnchantmentLevel").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    context.getSource().sendSuccess(() -> Component.translatable("command.maxEnchantmentLevel"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.maxEnchantmentLevel.description"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.maxEnchantmentLevel.value", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL)), false);
                    return 0;
                }).
                then(argument("level", IntegerArgumentType.integer(0, Integer.MAX_VALUE)).executes(context -> {
                    checkForUpdates(context);

                    LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL = IntegerArgumentType.getInteger(context, "level");
                    saveSettings(context.getSource().getLevel());

                    context.getSource().sendSuccess(() -> Component.translatable("command.maxEnchantmentLevel.set", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL)), true);
                    context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                    return 0;
                })))
            .then(literal("maxTradeLevel").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    context.getSource().sendSuccess(() -> Component.translatable("command.maxTradeLevel"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.maxTradeLevel.description"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.maxTradeLevel.value", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.MAX_TRADE_LEVEL)), false);
                    return 0;
                }).
                then(argument("level", IntegerArgumentType.integer(-1, Integer.MAX_VALUE)).executes(context -> {
                    checkForUpdates(context);

                    int level = IntegerArgumentType.getInteger(context, "level");
                    if (level > LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL) {
                        context.getSource().sendSuccess(() -> Component.translatable("command.maxTradeLevel.error").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)), true);
                        return 1;
                    }

                    LimitlessEnchantments.MAX_TRADE_LEVEL = level;
                    saveSettings(context.getSource().getLevel());

                    context.getSource().sendSuccess(() -> Component.translatable("command.maxTradeLevel.set", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.MAX_TRADE_LEVEL)), true);
                    context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                    return 0;
                })))
            .then(literal("anvilExperienceCostLimit").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    context.getSource().sendSuccess(() -> Component.translatable("command.anvilExperienceCostLimit"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.anvilExperienceCostLimit.description"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.anvilExperienceCostLimit.value", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.ANVIL_EXPERIENCE_COST_LIMIT)), false);
                    return 0;
                }).
                then(argument("level", IntegerArgumentType.integer(40, Integer.MAX_VALUE)).executes(context -> {
                    checkForUpdates(context);

                    LimitlessEnchantments.ANVIL_EXPERIENCE_COST_LIMIT = IntegerArgumentType.getInteger(context, "level");
                    saveSettings(context.getSource().getLevel());

                    context.getSource().sendSuccess(() -> Component.translatable("command.anvilExperienceCostLimit.set", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.ANVIL_EXPERIENCE_COST_LIMIT)), true);
                    context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                    return 0;
                })))
            .then(literal("fixedAnvilCost").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    context.getSource().sendSuccess(() -> Component.translatable("command.fixedAnvilCost"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.fixedAnvilCost.description"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.fixedAnvilCost.value", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.FIXED_ANVIL_COST)), false);
                    return 0;
                }).
                then(argument("level", IntegerArgumentType.integer(-1, Integer.MAX_VALUE)).executes(context -> {
                    checkForUpdates(context);

                    LimitlessEnchantments.FIXED_ANVIL_COST = IntegerArgumentType.getInteger(context, "level");
                    saveSettings(context.getSource().getLevel());

                    context.getSource().sendSuccess(() -> Component.translatable("command.anvilExperienceCostLimit.set", "" + LimitlessEnchantments.formatInt(LimitlessEnchantments.FIXED_ANVIL_COST)), true);
                    context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                    return 0;
                })))
            .then(literal("noIncompatibilities").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    context.getSource().sendSuccess(() -> Component.translatable("command.noIncompatibilities"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.noIncompatibilities.description"), false);
                    if (LimitlessEnchantments.NO_INCOMPATIBILITIES) 
                        context.getSource().sendSuccess(() -> Component.translatable("command.noIncompatibilities.on"), false);
                    else
                        context.getSource().sendSuccess(() -> Component.translatable("command.noIncompatibilities.off"), false);
                    return 0;
                }).
                then(argument("bool", BoolArgumentType.bool()).executes(context -> {
                    checkForUpdates(context);

                    LimitlessEnchantments.NO_INCOMPATIBILITIES = BoolArgumentType.getBool(context, "bool");
                    saveSettings(context.getSource().getLevel());

                    if (LimitlessEnchantments.NO_INCOMPATIBILITIES)
                        context.getSource().sendSuccess(() -> Component.translatable("command.noIncompatibilities.set"), true);
                    else
                        context.getSource().sendSuccess(() -> Component.translatable("command.noIncompatibilities.unset"), true);

                    context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                    return 0;
                })))
            .then(literal("rebalancedTrades").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    context.getSource().sendSuccess(() -> Component.translatable("command.rebalancedTrades"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.rebalancedTrades.description"), false);
                    if (LimitlessEnchantments.REBALANCED_TRADES) 
                        context.getSource().sendSuccess(() -> Component.translatable("command.rebalancedTrades.on"), false);
                    else
                        context.getSource().sendSuccess(() -> Component.translatable("command.rebalancedTrades.off"), false);
                    return 0;
                }).
                then(argument("bool", BoolArgumentType.bool()).executes(context -> {
                    checkForUpdates(context);

                    LimitlessEnchantments.REBALANCED_TRADES = BoolArgumentType.getBool(context, "bool");
                    saveSettings(context.getSource().getLevel());

                    if (LimitlessEnchantments.REBALANCED_TRADES)
                        context.getSource().sendSuccess(() -> Component.translatable("command.rebalancedTrades.set"), true);
                    else
                        context.getSource().sendSuccess(() -> Component.translatable("command.rebalancedTrades.unset"), true);

                    context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                    return 0;
                })))
            .then(literal("showActualNumbers").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    context.getSource().sendSuccess(() -> Component.translatable("command.showActualNumbers"), false);
                    context.getSource().sendSuccess(() -> Component.translatable("command.showActualNumbers.description"), false);
                    if (LimitlessEnchantments.SHOW_ACTUAL_NUMBERS) 
                        context.getSource().sendSuccess(() -> Component.translatable("command.showActualNumbers.on"), false);
                    else
                        context.getSource().sendSuccess(() -> Component.translatable("command.showActualNumbers.off"), false);
                    return 0;
                }).
                then(argument("bool", BoolArgumentType.bool()).executes(context -> {
                    checkForUpdates(context);

                    LimitlessEnchantments.SHOW_ACTUAL_NUMBERS = BoolArgumentType.getBool(context, "bool");
                    saveSettings(context.getSource().getLevel());

                    if (LimitlessEnchantments.SHOW_ACTUAL_NUMBERS)
                        context.getSource().sendSuccess(() -> Component.translatable("command.showActualNumbers.set"), true);
                    else
                        context.getSource().sendSuccess(() -> Component.translatable("command.showActualNumbers.unset"), true);

                    context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                    return 0;
                })))
            .then(literal("blacklist").requires(source -> source.hasPermission(2)).
                executes(context -> {
                    checkForUpdates(context);

                    if (!LimitlessEnchantments.blackListedEnchantments.isEmpty())
                        context.getSource().sendSuccess(() -> Component.translatable("command.blacklist.not_empty", LimitlessEnchantments.formattedList(LimitlessEnchantments.blackListedEnchantments)), true);
                    else
                        context.getSource().sendSuccess(() -> Component.translatable("command.blacklist.empty"), true);

                    saveSettings(context.getSource().getLevel());
                    return 0;
                })
                .then(literal("add")
                    .then(argument("enchantment", ResourceArgument.resource(registryAccess, Registries.ENCHANTMENT))
                        .executes(context -> {
                            checkForUpdates(context);

                            Reference<Enchantment> enchantment = ResourceArgument.getEnchantment(context, "enchantment");
                            if (!LimitlessEnchantments.blackListedEnchantments.contains(enchantment.getRegisteredName())) {
                                LimitlessEnchantments.blackListedEnchantments.add(enchantment.getRegisteredName());
                                context.getSource().sendSuccess(() -> Component.translatable("command.blacklist.blacklisted", enchantment.value().description().getString()), true);
                            } 
                            else
                                context.getSource().sendSuccess(() -> Component.translatable("command.blacklist.already.blacklisted", enchantment.value().description().getString()).setStyle(Style.EMPTY).withStyle(ChatFormatting.RED), true);
                        
                            saveSettings(context.getSource().getLevel());
                            context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                            return 0;
                        })
                    )
                )
                .then(literal("remove")
                    .then(argument("enchantment", ResourceArgument.resource(registryAccess, Registries.ENCHANTMENT))
                        .executes(context -> {
                            checkForUpdates(context);

                            Reference<Enchantment> enchantment = ResourceArgument.getEnchantment(context, "enchantment");
                            if (LimitlessEnchantments.blackListedEnchantments.contains(enchantment.getRegisteredName())) {
                                LimitlessEnchantments.blackListedEnchantments.remove(enchantment.getRegisteredName());
                                context.getSource().sendSuccess(() -> Component.translatable("command.blacklist.removed", enchantment.value().description().getString()), true);
                            } 
                            else
                                context.getSource().sendSuccess(() -> Component.translatable("command.blacklist.not_in", enchantment.value().description().getString()).setStyle(Style.EMPTY.applyFormat(ChatFormatting.RED)), true);
                            
                            saveSettings(context.getSource().getLevel());
                            context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                            return 0;
                        })
                    )
                )
                .then(literal("clear")
                    .executes(context -> {
                        checkForUpdates(context);

                        LimitlessEnchantments.blackListedEnchantments.clear();
                        context.getSource().sendSuccess(() -> Component.translatable("command.blacklist.cleared"), true);
                        
                        saveSettings(context.getSource().getLevel());
                        context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                        return 0;
                    })
                )
                .then(literal("addAll")
                    .executes(context -> {
                        checkForUpdates(context);
                        
                        ServerLevel world = context.getSource().getLevel();
                        @SuppressWarnings("unchecked")
                        ArrayList<String> copy = (ArrayList<String>) LimitlessEnchantments.blackListedEnchantments.clone();
                        LimitlessEnchantments.blackListedEnchantments.clear();
                        boolean success = true;
                        try {
                            world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).forEach((e) -> {
                                Holder<Enchantment> entry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).wrapAsHolder(e);
                                if (entry != null) {
                                    LimitlessEnchantments.blackListedEnchantments.add(entry.getRegisteredName());
                                }
                            });

                            context.getSource().sendSuccess(() -> Component.translatable("command.addAll.success"), true);
                        }
                        catch (Exception e) {
                            LimitlessEnchantments.blackListedEnchantments = copy;
                            context.getSource().sendSuccess(() -> Component.translatable("command.addAll.fail").setStyle(Style.EMPTY.applyFormat(ChatFormatting.RED)), true);
                            success = false;
                        }

                        saveSettings(world);
                        if (success)
                            context.getSource().sendSuccess(() -> Component.translatable("limitless.warning.setting.change"), false);
                        return 0;
                    })
                )
            )
        ));
    }

    private static void checkForUpdates(CommandContext<CommandSourceStack> context) {
        SemanticVersion newVersion = LimitlessEnchantments.VERSION.newVersionAvailable();
        if (!newVersion.isInvalid()) {
            context.getSource().sendSuccess(() -> Component.translatable("command.update.available", newVersion.toString()), true);
            context.getSource().sendSuccess(() -> Component.translatable("command.update.please"), true);
        }
    }

    private static void saveSettings(ServerLevel world) {
        JsonObject obj = new JsonObject();

        JsonArray array = new JsonArray();
        for (String e : LimitlessEnchantments.blackListedEnchantments) {
            array.add(e);
        }
        obj.add("blacklist", array);
        obj.addProperty("maxEnchantmentLevel", LimitlessEnchantments.MAX_ENCHANTMENT_LEVEL);
        obj.addProperty("maxTradeLevel", LimitlessEnchantments.MAX_TRADE_LEVEL);
        obj.addProperty("anvilExperienceCostLimit", LimitlessEnchantments.ANVIL_EXPERIENCE_COST_LIMIT);
        obj.addProperty("fixedAnvilCost", LimitlessEnchantments.FIXED_ANVIL_COST);
        obj.addProperty("noIncompatibilities", LimitlessEnchantments.NO_INCOMPATIBILITIES);
        obj.addProperty("rebalancedTrades", LimitlessEnchantments.REBALANCED_TRADES);
        obj.addProperty("showActualNumbers", LimitlessEnchantments.SHOW_ACTUAL_NUMBERS);

        EnchantmentsPersistentState.save(world, "limitlessenchantments", obj);
    }
}