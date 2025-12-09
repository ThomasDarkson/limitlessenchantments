[<img alt="curseforge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg">](https://www.curseforge.com/minecraft/mc-mods/limitlessenchantments) [<img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg">](https://modrinth.com/mod/limitlessenchantments) 

[<img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg">](https://fabricmc.net/)
[<img alt="fabric" height="56" src="https://i.ibb.co/n8qdgVg6/cozy-64h.png">](https://neoforged.net/)
[<img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/forge_vector.svg">](https://files.minecraftforge.net/net/minecraftforge/forge/)

[<img alt="semantic-ver-lib" src="https://raw.githubusercontent.com/ThomasDarkson/SemanticVerLib/refs/heads/main/user_project.png">](https://github.com/ThomasDarkson/SemanticVerLib)

![416740905-a9b728d7-91ab-447d-a571-6bee10046851 (2)](https://github.com/user-attachments/assets/9b5432b1-ed7c-4615-8f68-c5eb80b2aaa2)


This mod requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and [Architectury API](https://www.curseforge.com/minecraft/mc-mods/architectury-api).

# Limitless Enchantments

Limitless Enchantments is a mod that lets you mess with enchantments all you want. Only 1.21+ is supported.
This mod bumps enchantment level cap to 2,147,483,648 from 255.

## Settings
Settings are managed with the `/limitless [SETTING]` command. Available options are:

- `maxEnchantmentLevel`
- `maxTradeLevel`
- `blacklist`
- `rebalancedTrades`
- `anvilExperienceCostLimit`
- `fixedAnvilCost`
- `noIncompatibilities`
- `showActualNumbers`

If `limitless` command is executed without any arguments, it will show mod's version.

### Maximum Enchantment Level (`maxEnchantmentLevel`)
- Setting Type: Integer
- Avaliable value(s): Between 0 and 2147483648
- Subcommands: None
- Executing for example: `limitless maxEnchantmentLevel 255`

Maximum Enchantment Level is a setting that lets you remove the limit of maximum enchantment levels.

For example; with the setting Maximum Enchantment Level set to 255, any librarian trades with enchanted books' level will be between 1 and 255, ignoring the book's maximum level.
![image](https://github.com/user-attachments/assets/d523c2c0-56cc-477a-a3d4-0b342151af65)

If Maximum Enchantment Level is set to 0, the maximum levels will work normally.

For technical reasons, in creative inventory screen enchanted books will only have up to level 255 even if `maxEnchantmentLevel` is higher than 255. This does not mean you can't get higher level books than 255 ones.

### Maximum Trade Level (`maxTradeLevel`)
- Setting Type: Integer
- Avaliable value(s): Between -1 and 2147483648 (Must be smaller than `maxEnchantmentLevel`)
- Subcommands: None
- Executing for example: `limitless maxTradeLevel 255`

Maximum Trade Level is a setting that acts as a limiter for `maxEnchantmentLevel` in librarian trades.

For example; with the setting Maximum Enchantment Level set to 255 and Maximum Trade Level set to 103, librarians won't trade enchanted books with a higher level than 103. If Maximum Trade Level is set to 0, librarian trades will not be limited. This setting **must** be lower than Maximum Enchantment Level.

If this setting is set to -1, villagers will not trade books with higher levels than their vanilla maximum levels even if Maximum Enchantment Level is set higher.

### Blacklist (`blacklist`)
- Setting Type: List of enchantments
- Avaliable value(s): Enchantments
- Subcommands: `add`, `addAll`, `remove` and `clear`
- Executing for example: `limitless blacklist`

With this setting, you can blacklist certain enchantments to make them not be affected by the `maxEnchantmentLevel` setting. Executing only `limitless blacklist` will show the current blacklist of enchantments.

### `add`
- Setting Type: Subcommand 
- Avaliable value(s): Enchantment
- Executing for example: `limitless blacklist add minecraft:mending`

This subcommand adds an enchantment to blacklist. You can't add enchantments that are already in the blacklist.

### `addAll`
- Setting Type: Subcommand 
- Avaliable value(s): None
- Executing for example: `limitless blacklist addAll`

This subcommand adds all available enchantments to the blacklist. If something unexpected happens, the changes will be reverted.

### `remove`
- Setting Type: Subcommand 
- Avaliable value(s): Enchantment
- Executing for example: `limitless blacklist remove minecraft:mending`

This subcommand removes an enchantment from the blacklist. You can't remove nonexistent enchantments in the blacklist.

### `clear`
- Setting Type: Subcommand 
- Avaliable value(s): None
- Executing for example: `limitless blacklist clear`

This subcommand clears all enchantments off the blacklist.

### Rebalanced Trades (`rebalancedTrades`)
- Setting Type: Boolean
- Avaliable value(s): `true` or `false`
- Subcommands: None
- Executing for example: `limitless rebalancedTrades true`

With Rebalanced Trades setting, you can let the mod rebalance Librarian Villager trades. This is recommended if Maximum Enchantment Level is higher than 10.

Without balances:
![image](https://github.com/user-attachments/assets/644a2382-5023-432b-87ed-9bd9c8753ab8)

With balances:
![416743882-804f79b7-b84c-4e5a-a414-6327773d655d (1)](https://github.com/user-attachments/assets/11f3089b-044c-47e3-957b-e6a72fa75eb3)

### Anvil Experience Cost Limit (`anvilExperienceCostLimit`)
- Setting Type: Integer
- Avaliable value(s): Between 40 and 2147483648
- Subcommands: None
- Executing for example: `limitless anvilExperienceCostLimit 255`

Anvil Experience Cost Limit is a setting that allows you to modify anvil operation experience level cost limit that makes anvils refuse to work. (Too expensive!)

![](https://i.ibb.co/V0BmdsR4/image.png)

### Fixed Anvil Cost (`fixedAnvilCost`)
- Setting Type: Integer
- Avaliable value(s): Between -1 and 2147483648
- Subcommands: None
- Executing for example: `limitless fixedAnvilCost 20`

Fixed Anvil Cost is a setting that forces all anvil experience costs to a specified value. If this setting is not set to -1, anvils will always use this value for their cost. However, if the specified value is higher than the Anvil Experience Cost Limit, the anvil will display the “Too Expensive!” message regardless of what you try to do.

### No Incompatibilities (`noIncompatibilities`)
- Setting Type: Boolean
- Avaliable value(s): `true` or `false`
- Subcommands: None
- Executing for example: `limitless noIncompatibilities true`

If No Incompatibilities setting is turned on, the game will allow you to to combine mutually exclusive enchantments like Infinity and Mending together.

![image](https://github.com/user-attachments/assets/8ab8427a-ccca-4b6d-9eb2-14fdafc96759)

### Showing Actual Numbers
- Setting Type: Boolean
- Avaliable value(s): `true` or `false`
- Subcommands: None
- Executing for example: `limitless showActualNumbers false`
With setting `showActualNumbers`, you can choose to have actual numbers of the level of an enchantment near the roman numerals.