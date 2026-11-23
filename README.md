# Laser
Have you ever wanted yet another command framework? Look no further, I created Laser!

Laser is a one of kind, highly opinionated annotated command framework that wraps around Brigadier.

No, the internals are not simple. It is very much a spiderweb but it's my spiderweb. 

## What is the goal?
- [ ] Support Fabric
- [ ] Support Velocity
- [X] Support Paper
  - [X] Add support for Paper's argument types.
  - [X] Add support for Paper's permission system.
  - [X] Add extension mapping support for Bukkit's CommandSender/Player
    - [X] Add support for only allowing ConsoleSender, or only allowing Player to run a command. 
- [X] Add native brigadier support using mojang's brigadier dependency for easier platform expansion.
- [X] Add custom permission checks like `any_of` and `all_of`.
- [X] Add the ability to customize internal messages using a registry.
- [X] Add the ability to read the meta keys like PermissionMeta, or ArgumentMeta
- [X] Allow registering dynamically updating suggestions.
