# SkriptIDE
An IDE for Skript written in Java.
## FEATURES
- Syntax Highlighting

##Compiled JARs
[TeamCity CI](https://ci.scrumplex.ovh/)

##How to compile
```
mvn clean compile package assembly:single
```

SkriptIDE-x.x.x.jar: JAR without dependencies
SkriptIDE-x.x.x-final.jar: JAR(obfuscated) with dependencies
SkriptIDE-x.x.x-jar-with-dependencies.jar: JAR with dependencies
SkriptIDE-x.x.x_proguard_result.jar: JAR(obfuscated) without dependencies