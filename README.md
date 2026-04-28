# HungerCore

A Minecraft plugin for managing **Hunger Games ** events.

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
