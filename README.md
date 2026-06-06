# actionbar-api

A lightweight Spigot library for managing multi-entry action bars in Minecraft plugins. Supports multiple concurrent entries per player with optional time-based expiry, rendered as a sorted, separated line: `» entry1 ❘ entry2 «`.

## Features

- Per-player `Actionbar` instances holding multiple named entries
- Optional `Duration`-based expiry — entries auto-remove when they expire
- Sorted display order via Adventure `Key` comparison
- Asynchronous tick loop powered by Bukkit's scheduler
- Composable with any Adventure `Audience` provider
- Customizable `ActionbarStyle` — configure prefix, separator, and suffix components

## Installation

**Gradle (Kotlin DSL)**

Add the repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.kuraiwtf:actionbar-api:0.0.6")
}
```

## Usage

### 1. Create the service

Instantiate `ActionbarService` once during plugin startup. The second parameter is an `Audience` provider — pass `player -> player` if you are using the Paper/Adventure native API, or wrap with your own provider.

```java
ActionbarService actionbarService = ActionbarService.create(plugin, player -> player);
```

To use a custom visual style, pass an `ActionbarStyle` as the third argument:

```java
ActionbarStyle style = ActionbarStyle.builder()
    .prefix(Component.text("[", NamedTextColor.GOLD))
    .separator(Component.text("|", NamedTextColor.GRAY))
    .suffix(Component.text("]", NamedTextColor.GOLD))
    .build();

ActionbarService actionbarService = ActionbarService.create(plugin, player -> player, style);
```

This immediately starts the async tick task that refreshes every online player's action bar each tick.

### 2. Register entries

Get the `Actionbar` for a player by UUID and register entries:

```java
// Persistent entry (stays until manually removed)
Actionbar actionbar = actionbarService.actionbar(player.getUniqueId());
actionbar.registerEntry(
    Key.key("myplugin", "health"),
    Component.text("❤ 20", NamedTextColor.RED)
);

// Timed entry (auto-expires after 5 seconds)
actionbar.registerEntry(
    Key.key("myplugin", "notification"),
    Component.text("Quest completed!", NamedTextColor.GREEN),
    Duration.ofSeconds(5)
);
```

### 3. Remove entries

```java
actionbar.unregisterEntry(Key.key("myplugin", "health"));
```

### Display format

Entries are sorted by their `Key` and rendered as:

```
» entry1 ❘ entry2 ❘ entry3 «
```

## API

### `ActionbarService`

| Method | Description |
|--------|-------------|
| `ActionbarService.create(Plugin, Function<Player, Audience>)` | Creates the service with the default style and starts the update task |
| `ActionbarService.create(Plugin, Function<Player, Audience>, ActionbarStyle)` | Creates the service with a custom style and starts the update task |
| `actionbar(UUID)` | Returns (or lazily creates) the `Actionbar` for the given player UUID |
| `style()` | Returns the `ActionbarStyle` used when rendering entries |

### `Actionbar`

| Method | Description |
|--------|-------------|
| `Actionbar.create()` | Creates a standalone `Actionbar` instance |
| `registerEntry(Key, Component)` | Registers a persistent entry |
| `registerEntry(Key, Component, Duration)` | Registers a timed entry that expires after `duration` |
| `registerEntry(ActionbarEntry)` | Registers a pre-built entry |
| `unregisterEntry(Key)` | Removes an entry by key |
| `unregisterEntriesIf(Predicate<ActionbarEntry>)` | Removes all entries matching the given predicate |
| `entry(Key)` | Retrieves an entry by key |
| `entries()` | Returns all current entries |

### `ActionbarEntry`

| Field | Description |
|-------|-------------|
| `key` | Adventure `Key` uniquely identifying this entry |
| `value` | Adventure `Component` to display |
| `duration` | How long the entry lives (`Duration.ZERO` = permanent) |
| `creationTime` | When the entry was created |
| `expired()` | Returns `true` if the entry has outlived its duration |

### `ActionbarStyle`

Built via the Lombok builder. All fields are optional; omitted fields fall back to the dark-gray defaults.

| Field | Default | Description |
|-------|---------|-------------|
| `prefix` | `»` (dark gray) | Component rendered before the first entry |
| `separator` | `❘` (dark gray) | Component rendered between consecutive entries |
| `suffix` | `«` (dark gray) | Component rendered after the last entry |

Use `ActionbarStyle.DEFAULT` to get the built-in style without constructing a builder.
