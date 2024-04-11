# Hypixel Mod API

The Hypixel Mod API is an implementation of custom packets for communicating with the Hypixel Server via plugin messages.

At this time the API is in an early preview state to obtain feedback from the community. The API is subject to change and may be changed or disabled at any time. You can read more about on the [announcement forum thread](https://hypixel.net/threads/hypixel-mod-api-developer-preview-feedback.5635119/).


## Usage

You can use this API as a dependency via the public Hypixel maven repo. You can also use
the [Example Code](https://github.com/HypixelDev/PublicAPI/tree/master/hypixel-api-example) as a good starting point.

#### Hypixel Maven Repo

```xml
<repository>
    <id>Hypixel</id>
    <url>https://repo.hypixel.net/repository/Hypixel/</url>
</repository>
```

This repo can also be used with Gradle.

```gradle
repositories {
    maven { url 'https://repo.hypixel.net/repository/Hypixel/' }
}
```

You can then include the dependency in your project.

```xml
<dependency>
    <groupId>net.hypixel</groupId>
    <artifactId>mod-api</artifactId>
    <version>0.1.6</version>
</dependency>
```

```gradle
implementation 'net.hypixel:mod-api:0.1.6'
```
