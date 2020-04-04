# Alchemy-Utilities Changelog

# 0.1.1 (Not released yet)
- Fixed a bug where protected constructor couldn't be accessed by Instantiator (it fixes instantiating of AlchemyLogger too).
- Changed method that were using an argument to instantiate or invoke a method to now also use multiple ones.
- Added SafeVoidAction and SafeBooleanAction which is the same as VoidAction and BooleanAction except it can handle method throwing exception (made safeClose() method 
  use this functional interface and added a more generic safe execution method to FileUtils).
- BiModifierAction and ModifierAction now extends BiFunction and Function for better usability.
- Implemented shutdownNow() method of SynchronousExecutorService.
- Moved fr.alchemy.utilities.pool package to fr.alchemy.utilities.collections.pool.
- Added Version class to represent and compare sequence-based software versioning schemes.
- Some JavaDoc polishing and rewriting as always.
 
# 0.1.0 (Released March 27, 2020)
- Alchemy-Utilities can be setup with both Gradle and Maven.
- Added specific utility methods class : ByteUtils, ArrayUtil, SystemUtils, FileUtils, Validator and Instantiator.
- Added logging package to setup and retrieve Logger.
- Added event package to support event listening and publishing using an EventBus.
- Added a collection package for Collection and Iterable specific implementations (needs to be refactored).
- Added task package which contain FunctionalInterface to perform specific actions using lambas.