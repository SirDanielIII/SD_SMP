# Sir Daniel's General Purpose SMP Plugin

This Spigot plugin aims to provide basic quality of life additions to an SMP, making it a must have for your SMP servers! 😎

Verion Compatibility: 1.16, 1.17, 1.18, 1.19

## Customizable Features
All features listed below are editable in SD_SMP's main config file!

∘ Server MoTD (does not work on BungeeCord)

∘ Welcome message on join (title & subtitle)

∘ Custom player messages for joining, quiting & sleeping (all configurable)
  
∘ Lightning on player kill

∘ End portal entry toggle

∘ Netherite & elytra balancing toggles


## Command List
### SMP Commands
```
/smp
  → Returns all available commands from this plugin
  
/coords
  → Returns all available /coords commands
  
/death
  → Returns all available /death commands

/ivan
  → Returns all available /ivan commands
  
/wand
  → Summons a powerful wand of the Gods
  → Use this to one-tap or explode anything in the game, or cyberbully your server members
```
### COORDS - Basic Coordinate Saving System
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
### DEATH - Death & Player Kills Statistics
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
### Ivan
```
/ivan dog
  → Summons a dog named "Ivan"
  
/ivan donkey
  → Summons a donkey named "Ivan"
```


# The Config Files
Main Config: https://github.com/SirDanielIII/SD_SMP/blob/master/src/main/resources/config.yml

Error Messages: https://github.com/SirDanielIII/SD_SMP/blob/master/src/main/resources/error_messages.yml

Player Config: https://github.com/SirDanielIII/SD_SMP/blob/master/src/main/resources/default_player_config.yml


# PERMISSIONS (as of Version 2.0)
```
sd_smp.god.wand:
  description: Lets you use the wand
  default: op
  
sd_smp.ivan.*:
  description: Gives access to all Ivan commands
  children:
    sd_smp.ivan.dog: true
    sd_smp.ivan.donkey: true
    
sd_smp.ivan.dog:
  description: Spawns a dog named Ivan
  default: op
  
sd_smp.ivan.donkey:
  description: Spawns a donkey named Ivan
  default: op
  
sd_smp.coords.*:
  description: Gives access to all Coords commands
  children:
    sd_smp.coords.clear: true
    sd_smp.coords.list: true
    sd_smp.coords.send: true
    sd_smp.coords.set: true
    
sd_smp.coords.clear:
  description: Clears a saved coordinate
  default: true
  
sd_smp.coords.list:
  description: Lists a saved coordinate
  default: true
  
sd_smp.coords.send:
  description: Sends a saved coordinate
  default: true
  
sd_smp.coords.set:
  description: Sets a saved coordinate
  default: true
  
sd_smp.death.*:
  description: Gives access to all Death commands
  children:
    sd_smp.death.kdr: true
    sd_smp.death.murders: true
    sd_smp.death.nonplayer: true
    sd_smp.death.player: true
    sd_smp.death.total: true
    
sd_smp.death.kdr:
  description: Returns KDR value
  default: true
  
sd_smp.death.murders:
  description: Returns murder count
  default: true
  
sd_smp.death.nonplayer:
  description: Returns death count (No PVP)
  default: true
  
sd_smp.death.player:
  description: Returns death count (Only PVP)
  default: true
  
sd_smp.death.total:
  description: Returns lifetime death count
  default: true
```

#### Libraries Used
* BoostedYAML
