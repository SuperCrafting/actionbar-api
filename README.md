# actionbar-api

A lightweight Spigot library for managing multi-entry action bars in Minecraft plugins. Supports multiple concurrent
entries per player with optional time-based expiry, rendered as a sorted line using a configurable Adventure
`JoinConfiguration`.

## Features

* Per-player `Actionbar` instances holding multiple named entries
* Optional `Duration`-based expiry — entries auto-remove when they expire
* Sorted display order via Adventure `Key` comparison
* Asynchronous tick loop powered by Bukkit's scheduler
* Composable with any Adventure `Audience` provider
* Customizable entry composition via Adventure `JoinConfiguration`

## Installation

**Gradle (Kotlin DSL)**

Add the repository and dependency to your `build.gradle.kts`:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.kuraiwtf:actionbar-api:0.0.8")
}
```

## Usage

### 1. Create the service

Instantiate `ActionbarService` once during plugin startup. The second parameter is an `Audience` provider — pass
`player -> player` if you are using the Paper/Adventure native API, or wrap with your own provider.

```java
ActionbarService actionbarService =
        ActionbarService.actionbarService(plugin, player -> player);
```

To use a custom join configuration, pass a `JoinConfiguration` as the third argument:

```java
JoinConfiguration joinConfiguration =
        JoinConfiguration.builder()
                .prefix(Component.text("»", NamedTextColor.DARK_GRAY).appendSpace())
                .separator(
                        Component.space()
                                .append(Component.text("❘", NamedTextColor.DARK_GRAY))
                                .appendSpace())
                .suffix(
                        Component.space()
                                .append(Component.text("«", NamedTextColor.DARK_GRAY)))
                .build();

ActionbarService actionbarService =
        ActionbarService.actionbarService(plugin, player -> player, joinConfiguration);
```

This immediately starts the async tick task that refreshes online players' action bars with active entries each tick.

### 2. Register entries

Get the `Actionbar` for a player by UUID and register entries:

```java
// Persistent entry (stays until manually removed)
Actionbar actionbar = actionbarService.actionbar(player.getUniqueId());
actionbar.registerActionbarEntry(Key.key("myplugin", "health"), Component.text("❤ 20", NamedTextColor.RED));

// Timed entry (auto-expires after 5 seconds)
actionbar.registerActionbarEntry(Key.key("myplugin", "notification"), Component.text("Quest completed!", NamedTextColor.GREEN), Duration.ofSeconds(5));
```

### 3. Remove entries

```java
actionbar.unregisterActionbarEntry(Key.key("myplugin", "health"));
```

### Display format

Entries are sorted by their `Key` and rendered according to the configured `JoinConfiguration`.

For example, with the custom configuration shown above:

```
» entry1 ❘ entry2 ❘ entry3 «
```

## API

### `ActionbarService`

| Method                                                                                     | Description                                                                      |
| ------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------- |
| `ActionbarService.actionbarService(Plugin, Function<Player, Audience>)`                    | Creates the service with `JoinConfiguration.spaces()` and starts the update task |
| `ActionbarService.actionbarService(Plugin, Function<Player, Audience>, JoinConfiguration)` | Creates the service with a custom join configuration and starts the update task  |
| `actionbar(UUID)`                                                                          | Returns (or lazily creates) the `Actionbar` for the given holder UUID            |
| `joinConfiguration()`                                                                      | Returns the `JoinConfiguration` used when rendering entries                      |

### `Actionbar`

| Method                                                    | Description                                                    |
| --------------------------------------------------------- | -------------------------------------------------------------- |
| `Actionbar.actionbar()`                                   | Creates a standalone `Actionbar` instance                      |
| `registerActionbarEntry(Key, Component)`                  | Registers a persistent entry                                   |
| `registerActionbarEntry(Key, Component, Duration)`        | Registers a timed entry that expires after `duration`          |
| `registerActionbarEntry(ActionbarEntry)`                  | Registers a pre-built entry                                    |
| `unregisterActionbarEntry(Key)`                           | Removes an entry by key                                        |
| `unregisterActionbarEntriesIf(Predicate<ActionbarEntry>)` | Removes all entries matching the given predicate               |
| `actionbarEntry(Key)`                                     | Retrieves an entry by key                                      |
| `actionbarEntries()`                                      | Returns an unmodifiable sequenced view of entries in key order |

### `ActionbarEntry`

| Field            | Description                                            |
| ---------------- | ------------------------------------------------------ |
| `key`            | Adventure `Key` uniquely identifying this entry        |
| `valueComponent` | Adventure `Component` to display                       |
| `duration`       | How long the entry lives (`Duration.ZERO` = permanent) |
| `creationTime`   | When the entry was created                             |
| `expired()`      | Returns `true` if the entry has outlived its duration  |

### `JoinConfiguration`

Adventure `JoinConfiguration` controls how entry components are composed.

The default service configuration is `JoinConfiguration.spaces()`.

A custom configuration can define values such as a prefix, separator, and suffix:

| Element     | Example         | Description                                    |
| ----------- | --------------- | ---------------------------------------------- |
| `prefix`    | `»` (dark gray) | Component rendered before the first entry      |
| `separator` | `❘` (dark gray) | Component rendered between consecutive entries |
| `suffix`    | `«` (dark gray) | Component rendered after the last entry        |

Pass a custom `JoinConfiguration` to `ActionbarService.actionbarService(...)` to customize rendering.
