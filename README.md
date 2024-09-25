# Pojlib HEF | Minecraft Launcher Core Based on [Pojlib](https://github.com/QuestCraftPlusPlus/Pojlib)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

A Minecraft: Java Edition launcher library partially made with elements from PojavLauncher.

This library was initially meant for use in QuestCraft but has turned into the perfect library for Minecraft: Java Edition launchers. This includes everything needed for a basic and (soon) even advanced MCJE launchers written in Java (or any other language with interop).

## Contributing

Contributions are always welcome!

Please ensure your code follows the language's naming conventions, here's a list of a few of the most common languages used in our projects:

- [Java's Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html)
- [C++'s Conventions](https://google.github.io/styleguide/cppguide.html)

Make sure your pull request describes exactly what the code does and explains why you're making the pull request!


## Credits & Third Party Components
### Pojlib HEF Developers:

* [@Novampr](https://github.com/Novampr) | Maintainer
### Pojlib Developers:

* [@TheJudge156](https://github.com/thejudge156) | Senior Maintainer

* [@CADIndie](https://github.com/CADIndie) | Jr. Maintainer

* [@MrNavaStar](https://github.com/MrNavaStar) | Previous Main Feature Implementor

### Components:
- [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher) (Pojlib Base application): [GNU GPLv3](https://github.com/khanhduytran0/PojavLauncher/blob/master/LICENSE).

- [OpenComposite](https://gitlab.com/znixian/OpenOVR) (Used for OpenVR to OpenXR interpretation): [GNU GPLv3](https://gitlab.com/znixian/OpenOVR/-/blob/openxr/LICENSE.txt).

# Changes compared to Pojlib
- Moved to Wrapper
  - Logging in
  - Instance Management
  - Mod Manager (Currently not reimplemented)
- Added in Pojlib HEF
  - Support for FML based loaders (Lex/NeoForge)
  - Better API call to launch the game with full parameter control
