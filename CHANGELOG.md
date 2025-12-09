# 2.0.0
## Additions
- Added a new setting `fixedAnvilCost`
- Neoforge (and Forge for version 1.20.1) support!

## Changes
- Setting Maximum Trade Level to -1 now makes trades use vanilla maximum levels for enchantments no matter what Maximum Enchantment Level is
- Blacklist will now function a bit faster

## Fixes
- Fixed Rebalanced Trades settings being loaded incorrectly
- Fixed settings resetting after updating

## Other
- Architectury API is now a dependency

# 1.3.1
- Fixed a typo

# 1.3.0
- Added a new setting `maxTradeLevel`
- Numbers higher than 999 are now formatted like **6,180**

# 1.2.2
- Added a version checker using [Semantic Version Library](https://github.com/ThomasDarkson/SemanticVerLib)
    - This version checker runs once every startup and it will tell you to update if you use `/limitless` command and there is a newer version available

# 1.2.1
- Fixed settings resetting to their default values
- Improved compatability with other mods

# 1.2.0
- Moved settings to `/limitlessenchantments` command
- Added two new settings `anvilExperienceCostLimit` and `allAll` for `blacklist`
- Added a new save file system 
- Fixed blacklisted enchantments not working as intended
- Added translations for /limitlessenchantments command

# 1.1.0
- Added a blacklist feature that allows you to exlude certain enchantments to be affected by the `maxEnchantmentLevel` game rule
- Support for 1.21.6

# 1.0.3
- Added a rule to show actual levels with the roman numerals

# 1.0.2
- Added a rule to rebalance Librarian Villager trades
- Mending now restores 2 more durabilites for every level
- Fixed Fortune and Silk Touch incompatabilites
- Default value of maxEnchantmentLevel is now 0

# 1.0.1
- Added `noIncompatibilities` rule
- Default value of rule `maxEnchantmentLevel` is now 10 instead 255
- Added descriptions to game rules