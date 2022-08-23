# Sir Daniel's General Purpose SMP Plugin

This Spigot plugin aims to provide basic quality of life additions to an SMP, making it a must have for your SMP servers!

# Features
#### SMP Commands
```
/smp
  → Returns all available commands from this plugin
/coords
  → Returns all available /coords commands
/death
  → Returns all available /death commands
```
#### COORDS - Basic Coordinate Saving System
```
/coords clear <name | all> [dimension(s)]
  → Clears saved coordinate(s)

/coords set <name> [X Y Z] <dimension>
  → Saves specific coordinate under given name

/coords list <name | all> <dimension>
  → Lists saved coordinate(s)

/coords send <here [players] | <name> <dimension> [players]>
  → Sends a saved coordinate or your current location to other player(s)
```
#### DEATH - Death & Player Kills Statistics
```
/death kdr
  → Returns your kill-death (K/D) ratio
  → Calculated using player kills & PVP deaths
/death kills
  → Returns your player kill count
/death nonplayer
  → Returns your death count, excluding PVP
/death player
  → Returns your PVP death count
/death total
  → Returns your lifetime death count
```
#### Other
```
/ivan dog
  → Summons a dog named "Ivan"
/ivan donkey
  → Summons a donkey named "Ivan"
/wand
  → Summons a powerful wand of the Gods
  → Use this to one-tap or explode anything in the game, or cyberbully your server members
```

## Customizable Features
* Server MoTD
* Title & subtitle on join
* Messages for joining, leaving, sleeping and death statistics
