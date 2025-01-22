package limitless.enchantments;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LimitlessEnchantments implements ModInitializer {
	public static final String MOD_ID = "limitlessenchantments";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final GameRules.Key<GameRules.IntRule> MAX_ENCHANTMENT_LEVEL =
	GameRuleRegistry.register("maxEnchantmentLevel", Category.MISC, GameRuleFactory.createIntRule(255, 0, 255));

	public static int MAX_ENCHANTMENT_LEVEL_INT = 255;

	@Override
	public void onInitialize() {
	}

	public static String convertToRoman(int number) {
        if (number > 255)
			number = 255;

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