# FarFarAway

A PaperMC plugin that spawns players in a random location far away from the
world origin. First-time joiners and players respawning without a bed or anchor
are teleported to a safe random spot within a configurable distance ring.

## Compatibility

- PaperMC 1.21.x
- PaperMC 26.x

## Installation

1. Download the latest `FarFarAway` JAR file from the [latest release](https://github.com/gabmontes/FarFarAway/releases) assets.
2. Place the JAR file into your server's `plugins` folder.
3. Restart your server.

## Configuration

Edit `plugins/FarFarAway/config.yml`:

```yaml
# Minimum distance from world origin (0,0) in blocks
min-distance: 1000

# Maximum distance from world origin (0,0) in blocks
max-distance: 10000
```

## Building from Source

To build the plugin run:

```sh
mvn clean package
```

The compiled JAR will be located in the `target/` directory.

## Release

Tag the commit, push the tag, create a new release and publish it. The `jar` will be build and uploaded to the release asserts automatically.
