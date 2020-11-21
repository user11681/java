Common formatting is a library based on [phormat](https://github.com/user11681/phormat) that implements multiple types of formatting in order to allow multiple mods to reuse them without conflicts.

### use
build.gradle:
```groovy
repositories {
    . . .
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    . . .
    modImplementation "com.github.user11681:commonformatting:1.16-SNAPSHOT"
}
```
