package limitless.enchantments;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimitlessEnchantments implements ModInitializer {
	public static final String MOD_ID = "limitlessenchantments";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final CustomGameRuleCategory ENCHANTMENTS_CATEGORY = new CustomGameRuleCategory(Identifier.of(MOD_ID, "enchantments"), Text.translatable("category.gamerule.enchantments").fillStyle(Style.EMPTY.withColor(16579668).withBold(true)));

	public static final GameRules.Key<GameRules.IntRule> MAX_ENCHANTMENT_LEVEL =
	GameRuleRegistry.register("maxEnchantmentLevel", ENCHANTMENTS_CATEGORY, GameRuleFactory.createIntRule(0, 0, 255));

    public static final GameRules.Key<GameRules.BooleanRule> NO_INCOMPATIBILITIES =
	GameRuleRegistry.register("noIncompatibilities", ENCHANTMENTS_CATEGORY, GameRuleFactory.createBooleanRule(true));

    public static final GameRules.Key<GameRules.BooleanRule> REBALANCED_TRADES =
    GameRuleRegistry.register("rebalancedTrades", ENCHANTMENTS_CATEGORY, GameRuleFactory.createBooleanRule(false));

    public static final GameRules.Key<GameRules.BooleanRule> SHOW_ACTUAL_NUMBERS =
    GameRuleRegistry.register("showActualNumbers", ENCHANTMENTS_CATEGORY, GameRuleFactory.createBooleanRule(true));

	public static int MAX_ENCHANTMENT_LEVEL_INT = 255;
    public static boolean NO_INCOMPATIBILITIES_BOOLEAN = false;
    public static boolean REBALANCED_TRADES_BOOLEAN = false;
    public static boolean SHOW_ACTUAL_NUMBERS_BOOLEAN = false;
    
	@Override
	public void onInitialize() {
        
	}

	public static String convertToRoman(int number) {
        StringBuilder roman = new StringBuilder();
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] numerals = {
                "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
        };

        for (int i = 0; i < values.length; i++) {
            while (number >= values[i]) {
                roman.append(numerals[i]);
                number -= values[i];
            }
        }

        return roman.toString();
    }
}