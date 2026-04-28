# HungerCore

A Minecraft plugin for managing **Hunger Games / Hunger Craft 3** events.

---

## ✨ Features

- Tributes system (players tracking & stats)
- Kill tracking
- Bounty system (head rewards)
- Random loot chests
- Spawn points system
- Lobby setup
- Countdown & event timers
- Death spectator system (players stay where they die)
- Teams system (Lead-API support)
- PlaceholderAPI support

---

## ⚙️ Commands

### Core
- `/hc spawnpoints` — Manage spawn points
- `/hc chests` — Manage loot chests
- `/hc tributes` — Manage tributes
- `/hc lobby` — Set lobby location
- `/hc timer` — Configure timers
- `/hc bounty` — Manage head bounties

### Admin
- `/hc revive <player>` — Revive a player (OP)
- `/hc reviveall` — Revive all players (OP)
- `/hc resetkills` — Reset all kills (OP)
- `/hc resetplayerkills <player>` — Reset player kills (OP)
- `/hc cleardeaths` — Clear death locations (OP)
- `/hc updatealive` — Update alive counter (OP)

---

## 👥 Teams (Lead-API)

- `/hc teams create <player> <id>` — Create team
- `/hc teams list` — List teams
- `/hc teams info <player>` — View team info
- `/hc teams delete <player>` — Delete team
- `/hc teams add <player> <leader>` — Add player to team
- `/hc teams remove <player>` — Remove player from team
- `/hc teams random <size>` — Create random teams

### Examples
- `/hc teams random 1` → solo players
- `/hc teams random 4` → squads of 4
- `/hc teams random 5` → large teams

---

## 🔤 Placeholders

- `%hc_tribute%` — Tribute number
- `%hc_tributeid%` — Tribute ID
- `%hc_kills%` — Player kills
- `%hc_latest_death%` — Last death
- `%hc_bounty%` — Active bounty target
- `%hc_alive%` — Alive players count

---

## 📦 Requirements

- Paper 1.21+
- PlaceholderAPI (optional)
- Lead-API (optional, for teams)

---

## 📥 Installation

1. Build the plugin using Gradle
2. Place the `.jar` file into your `/plugins` folder
3. Restart your server
4. (Optional) install Lead-API for team features

---

## 📁 Config Files

Generated automatically on first run:

- `config.yml`
- `spawnpoints.yml`
- `chests.yml`

---

## 🧠 Notes

- Teams use numeric IDs for stability
- Random teams auto-balance online players
- Dead players remain in spectator mode at death location

---

## 🛠 Build

```bash
./gradlew build
