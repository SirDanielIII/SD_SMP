# SD_SMP

A lightweight Spigot plugin that adds quality-of-life tools for your SMP server, from custom MOTDs and welcome titles to feature-rich coordinate management system and
player death/kill statistics.

### 🧪 Version Compatibility

**Tested Minecraft Versions:**

```
1.16.5 → 1.21.5
```

✅ Compatible with **Spigot**, **Paper**, and other Spigot-based forks.

---

## 📋 Table of Contents

1. [Features](#-features)
2. [Commands](#-commands)
3. [Configuration](#-configuration)
4. [Permissions](#-permissions)

---

## 🔧 Features

All of these are toggleable or editable in `config.yml`:

- 📝 **Server MOTD**
- 👋 **Join/Welcome Message** (title & subtitle)
- 💤 **Custom Join / Quit / Sleep Messages**
- ⚡ **Lightning on Player Kill**
- ⛔ **End-Portal Entry Toggle**
- ⚠️ **Netherite & Elytra Balancing**
- 🗺️ **Coordinate Manager** (`/coords`)
- ⚔️ **Death & Kill Statistics** (`/death`)
- 🐶 **"Ivan" Pet Summoner** (`/ivan`)
- 💀 **Wand of the Gods** (`/wand`)

---

## 💬 Commands

### `/smp`

> Lists all SD_SMP subcommands.

### `/coords`  — Coordinate Management System

```txt
/coords set <name> [X Y Z] [dimension]
    → Save a coordinate  
    
/coords list [name|all] [dimension]
    → List saved coords  
    
/coords clear [name|all] [dimension]
    → Clear saved coords  
    
/coords send <here [players] | <name> [players]>
    → Send coords to other players  
```

### `/death`  — Player Death & Kill Statistics

```txt
/death kdr       → Kill-death ratio  
/death kills     → Player kill count  
/death player    → PVP death count  
/death nonplayer → Mob/non-player death count  
/death total     → Lifetime death count  
```

### `/ivan`  — Summoning an "Ivan"

```txt
/ivan dog    → Summon a dog named Ivan  
/ivan donkey → Summon a donkey named Ivan  
```

### `/wand`  — The Wand of the Gods

```txt
/wand → Summon a powerful wand (one-tap, explosions, etc.)  
```

---

## ⚙️ Configuration

The following are the config files, where you can edit the plugin's settings, messages, and error messages!

<details>
<summary>config.yml</summary>

```yaml
# --- DO NOT TOUCH THIS ----------------
config-version: 2
# --------------------------------------
# COLOUR FORMATTING GUIDE (Colours require string to be surrounded by quotes)
#  &0	Black
#  &1	Dark Blue
#  &2	Dark Green
#  &3	Dark Aqua
#  &4	Dark Red
#  &5	Dark Purple
#  &6	Gold
#  &7	Gray
#  &8	Dark Gray
#  &9	Blue
#  &A	Green
#  &B	Aqua
#  &C	Red
#  &D	Light Purple
#  &E	Yellow
#  &F	White
#  &K	Obfuscated (Magic / Enchantment Table)
#  &L	Bold
#  &M	Strikethrough
#  &N	Underline
#  &O	Italic
#  &R	Reset
#  \n   Next Line

# CUSTOM COLOURS
# Add '&' before hex code (not case-sensitive)
# Example for Pink: &#FFC0CB or &#ffc0cb
# --------------------------------------

# --------------------------------------
# General Configuration
# --------------------------------------
# Name that appears in /smp command
name: '&#f50057&LSMP'

# Mostly for logging purposes
cmd_header: '&A[&FSD_SMP&A]'

# Message Of The Day - Individual Servers ONLY
# If your server is running in a proxy server (BungeeCord, Waterfall, etc),
# the following WILL NOT SHOW IN THE SERVER LISTING!!!
motd:
  enabled: true
  line_1: A Minecraft SMP
  line_2: ''

# Welcome message when you join
# The numbers below is time in ticks
welcome:
  enabled: true
  title: '&FHello There'
  subtitle: '&6Welcome to the SMP :)'
  fade_in: 20
  stay: 70
  fade_out: 20

# How many lines in the body are shown at a time when text in the chat is paginated
# Note 1: This ignores the two lines from the header and footer
# Note 2: /coords list will require this
lines_per_paginated_chat: 18

# --------------------------------------
# Gameplay Changes
# --------------------------------------
# Enable colour codes (E.G. &C) and hex values (E.G. &#0085F5) for signs.
sign_colour_codes: true

# Like PVP servers. The lightning won't destroy anything and/or create fire
lightning_on_player_kill: true

# Prevents a player from entering a portal
disable_nether_portal: false
disable_end_portal: false

# Netherite related toggles
disable_mining_netherite: false
disable_smithing_table: false
disable_crafting_netherite_tools: false
disable_crafting_netherite_armour: false

# Option to disable players flying with an Elytra
elytra_flight:
  overworld: true
  nether: true
  the_end: true

# --------------------------------------
# Scoreboards
# --------------------------------------
# Health Display Under Nametag
# update_interval is in ticks
health_under_name:
  enabled: true
  text_after_health: '&C❤'
  update_interval: 5

# --------------------------------------
# Custom Messages
# → See messages.yml to add/remove specific messages
# --------------------------------------
custom-join-messages-enable: true
custom-join-messages-clr: '&E'
# Put a ':)' at the end of each join message
custom-join-messages-smiley-face: true

custom-quit-messages-enable: true
custom-quit-messages-clr: '&C'

custom-sleep-messages-enable: true
custom-sleep-messages-clr: '&B'
```

</details>

<details>
<summary>error_messages.yml</summary>

```yaml
# Has not been finalized for coords-rework branch
# https://github.com/SirDanielIII/SD_SMP/blob/master/src/main/resources/messages.yml
```

</details>

<details>
<summary>messages.yml</summary>

```yaml
# Has not been finalized for coords-rework branch
# https://github.com/SirDanielIII/SD_SMP/blob/master/src/main/resources/error_messages.yml
```

</details>

### Default Player Config

<details>
<summary>Click here to see a player's default config!</summary>

```yaml
# --- DO NOT TOUCH THIS ----------------
config-version: 2
# --------------------------------------
uuid: ''
name: ''
kills: 0
death_by_player: 0
death_by_nonplayer: 0
death_total: 0
coordinates: [ ]
```

</details>

---

## 🔐 Permissions

| Node                     | Description                  | Default |
|--------------------------|------------------------------|---------|
| `sd_smp.god.wand`        | Use the Wand of the Gods     | `op`    |
| `sd_smp.ivan.*`          | All Ivan summoning commands  | `op`    |
| `sd_smp.ivan.dog`        | Summon a dog                 | `op`    |
| `sd_smp.ivan.donkey`     | Summon a donkey              | `op`    |
| `sd_smp.coords.*`        | All coords commands          | `true`  |
| `sd_smp.coords.set`      | `/coords set`                | `true`  |
| `sd_smp.coords.list`     | `/coords list`               | `true`  |
| `sd_smp.coords.clear`    | `/coords clear`              | `true`  |
| `sd_smp.coords.send`     | `/coords send`               | `true`  |
| `sd_smp.coords.teleport` | Click-to-teleport permission | `op`    |
| `sd_smp.death.*`         | All death commands           | `true`  |
| `sd_smp.death.kdr`       | `/death kdr`                 | `true`  |
| `sd_smp.death.kills`     | `/death kills`               | `true`  |
| `sd_smp.death.player`    | `/death player`              | `true`  |
| `sd_smp.death.nonplayer` | `/death nonplayer`           | `true`  |
| `sd_smp.death.total`     | `/death total`               | `true`  |

---
