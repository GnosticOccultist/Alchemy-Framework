# Alchemy-Framework

## Alchemy-Utilities

Alchemy Utilities is a library containing various utility methods concerning collections, files, stream or testing.

### Gradle Setup

```groovy
repositories {
    maven {
        url  "https://gnosticocculitst.jfrog.io/artifactory/default-maven-virtual/" 
    }
}

dependencies {
    compile 'com.github.gnosticoccultist:alchemy-utilities:0.2.0'
}
```

### Maven Setup

```xml
<repositories>
	<repository>
		<id>default-maven-virtual</id>
		<name>default-maven-virtual</name>
		<url>https://gnosticocculitst.jfrog.io/artifactory/default-maven-virtual/</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.gnosticoccultist</groupId>
	<artifactId>alchemy-utilities</artifactId>
	<version>0.2.0</version>
	<type>pom</type>
</dependency>

```