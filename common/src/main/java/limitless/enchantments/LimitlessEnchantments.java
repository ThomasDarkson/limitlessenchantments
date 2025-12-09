package limitless.enchantments;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.Enchantment;
import semantic.ver.lib.SemanticVerLib;
import semantic.ver.lib.SemanticVersion;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LimitlessEnchantments {
    private static final String URL = "https://raw.githubusercontent.com/ThomasDarkson/limitlessenchantments/refs/heads/version/version.txt";
    public static final SemanticVersion VERSION = SemanticVersion.stable(2, 0, 0, URL);

	public static final String MOD_ID = "limitlessenchantments";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static int MAX_TRADE_LEVEL = 0;
	public static int MAX_ENCHANTMENT_LEVEL = 0;
    public static int FIXED_ANVIL_COST = -1;
    public static int ANVIL_EXPERIENCE_COST_LIMIT = 40;
    public static boolean NO_INCOMPATIBILITIES = true;
    public static boolean REBALANCED_TRADES = false;
    public static boolean SHOW_ACTUAL_NUMBERS = true;

    public static ArrayList<String> blackListedEnchantments = new ArrayList<>();
    public static final HashMap<String, Enchantment> blackListedEnchantmentsMap = new HashMap<>();

	public static void init() {
        SemanticVerLib.initialize();
        LimitlessCommand.init();

        VERSION.checkForUpdates(null);
	}

    public static void loadSettings(ServerLevel world) {
        LimitlessEnchantments.blackListedEnchantments.clear();
        LimitlessEnchantments.blackListedEnchantmentsMap.clear();

        MAX_TRADE_LEVEL = 0;
        MAX_ENCHANTMENT_LEVEL = 0;
        FIXED_ANVIL_COST = -1;
        ANVIL_EXPERIENCE_COST_LIMIT = 40;
        NO_INCOMPATIBILITIES = true;
        REBALANCED_TRADES = false;
        SHOW_ACTUAL_NUMBERS = true;

        JsonObject obj = null;
        try {
            obj = EnchantmentsPersistentState.load(world, "limitlessenchantments").getAsJsonObject();
        }
        catch (Exception e) {
            obj = null;
        }

        if (obj != null) {
            try {
                MAX_ENCHANTMENT_LEVEL = obj.get("maxEnchantmentLevel").getAsInt();
            }
            catch (Exception e) {
            }

            try {
                MAX_TRADE_LEVEL = obj.get("maxTradeLevel").getAsInt();
            }
            catch (Exception e) {
            }

            try {
                ANVIL_EXPERIENCE_COST_LIMIT = obj.get("anvilExperienceCostLimit").getAsInt();
            }
            catch (Exception e) {
            }

            try {
                FIXED_ANVIL_COST = obj.get("fixedAnvilCost").getAsInt();
            }
            catch (Exception e) {
            }

            try {
                NO_INCOMPATIBILITIES = obj.get("noIncompatibilities").getAsBoolean();
            }
            catch (Exception e) {
            }

            try {
                REBALANCED_TRADES = obj.get("rebalancedTrades").getAsBoolean();
            }
            catch (Exception e) {
            }

            try {
                SHOW_ACTUAL_NUMBERS = obj.get("showActualNumbers").getAsBoolean();
            }
            catch (Exception e) {
            }

            try {
                JsonArray array = obj.get("blacklist").getAsJsonArray();
                for (JsonElement i : array.asList()) {
                    String s = i.getAsString();
                    LimitlessEnchantments.blackListedEnchantments.add(s);
                }
            }
            catch (Exception e) {
            }
        }
    }

    public static String formatInt(int i) {
        return NumberFormat.getNumberInstance(Locale.US).format(i);
    }

    public static boolean compareEnchantments(Enchantment e1, Enchantment e2) {
        if (e1 == e2) 
            return true;
        if (e1.description().getString().equals(e2.description().getString()))
            return true;
        return false;
    }

    public static String formattedList(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String l = list.get(i);
            Enchantment e = blackListedEnchantmentsMap.get(l);
            if (e != null)
                sb.append(e.description().getString());
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public static ArrayList<String> parseStr(String s) {
        s = s.trim();
        if (s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1);
        }

        ArrayList<String> list = new ArrayList<>();
        if (!s.isEmpty()) {
            for (String item : s.split(",\\s*")) {
                list.add(item);
            }
        }

        return list;
    }

	public static String convertToRoman(int number) {
        StringBuilder roman = new StringBuilder();

        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] numerals = {
            "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
        };

        if (number <= 3999) {
            for (int i = 0; i < values.length; i++) {
                while (number >= values[i]) {
                    roman.append(numerals[i]);
                    number -= values[i];
                }
            }
            return roman.toString();
        }

        StringBuilder compact = new StringBuilder();
        int thousands = number / 1000;
        int remainder = number % 1000;

        if (thousands > 0) {
            compact.append("\"M\" x ").append(thousands);
        }

        if (remainder > 0) {
            compact.append(" + \"").append(convertToRoman(remainder)).append("\"");
        }

        return compact.toString();
    }
}