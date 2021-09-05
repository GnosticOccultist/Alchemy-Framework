# Alchemy-Utilities Changelog

# 0.2.0 (Released September 5, 2021)
- Instantiator now also supports instantiation of Enum and can check that the class to instantiate is an implementation of a specific class or interface.
- Added more Collection methods to the Pool (forEach, size), as well as a method to retrieve, execute and release a pooled object in one call.
- Added an Interface for Pool and ReusablePool.
- Added a static method to create an empty ReadOnlyArray.
- Added a concurrent version of Pool which is using a StampedLockArray for thread-safe access.
- Added a concurrent version of Array which is using a StampedLock for read/write lock.
- Added a ArrayCollector to collect stream elements to an array (concurrent, readonly or fast).
- Added an unsynchronized and fast implementation of Writer to replace the BufferedWriter from Java.
- EventBus is no longer a singleton, an actual singleton extension exist : SingletonEventBus.
- Added a RunnableDispatcher to use with the EventBus, which can either run an action when multiple events have been received (without ordering) or run 
  multiple actions when a specific event has been received.
- Added WritingBuffer which is a fast non-closeable Writer implementation.
- Added StringUtils which contains utility methods for String manipulation.
- EventBus now also support publishing events asynchronously, it can be used along the RunnableDispatcher to gather the events from multiple threads before running the action.
- Some JavaDoc polishing and rewriting as always.

# 0.1.1 (Released April 4, 2020)
- Fixed a bug where protected constructor couldn't be accessed by Instantiator (it fixes instantiating of AlchemyLogger too).
- Changed method that were using an argument to instantiate or invoke a method to now also use multiple ones.
- Added SafeVoidAction and SafeBooleanAction which is the same as VoidAction and BooleanAction except it can handle method throwing exception (made safeClose() method 
  use this functional interface and added a more generic safe execution method to FileUtils).
- BiModifierAction and ModifierAction now extends BiFunction and Function for better usability.
- Implemented shutdownNow() method of SynchronousExecutorService.
- Moved fr.alchemy.utilities.pool package to fr.alchemy.utilities.collections.pool.
- Added Version class to represent and compare sequence-based software versioning schemes.
- Instantiator can now invoke a method using its name directly (without having the Method instance).
- Tested Instantiator and Version class using appropriate test-cases.
- Some JavaDoc polishing and rewriting as always.
 
# 0.1.0 (Released March 27, 2020)
- Alchemy-Utilities can be setup with both Gradle and Maven.
- Added specific utility methods class : ByteUtils, ArrayUtil, SystemUtils, FileUtils, Validator and Instantiator.
- Added logging package to setup and retrieve Logger.
- Added event package to support event listening and publishing using an EventBus.
- Added a collection package for Collection and Iterable specific implementations (needs to be refactored).
- Added task package which contain FunctionalInterface to perform specific actions using lambas.