# DBeaver Agent 26

## Dependency

Since the code is sourced from DBeaver, you need to prepare some packages for DBeaver.
Place the following packages in the "lib" folder and update the "pom.xml"

- `com.dbeaver.lm.api` - Core for licensing
- `org.jkiss.utils` - Provides some components for generating licenses

## How to use

Simply run `mvn package` to build.
The generated "dbeaver-agent-26.1-SNAPSHOT-jar-with-dependencies.jar" should be renamed to "dbeaver-agent.jar" and placed in the DBeaver installation directory.

Run `java -cp "./dbeaver-agent.jar;plugins/*" com.dbeaver.agent.Main` to generate the license key

Modify the "dbeaver.ini" file in the DBeaver installation directory and add some parameters

- Add the following line above `-vmargs`

  ```ini
  -vm
  Your JRE21/JDK21 Java path, such as /usr/lib/jvm/java-21-openjdk-amd64/bin/java
  ```

  > You can also omit the -vm parameter and directly use the system's default Java, or download jre21 and replace it to the installation directory. >
  > 
  > On Windows, use `javaw.exe` to avoid the terminal popping up

- Add the following line below `-vmargs` 

  ```ini
  -javaagent: <Your jar path, such as ./dbeaver-agent.jar>
  ```

Just like this

```ini
-startup
plugins/org.jkiss.dbeaver.launcher_1.0.54.202606291110.jar --launcher.library
plugins/org.eclipse.equinox.launcher.gtk.linux.x86_64_1.2.1500.v20250801-0854 -vm
/usr/lib/jvm/java-21-openjdk-amd64/bin/java
-vmargs
-javaagent:./dbeaver-agent.jar
-XX:+IgnoreUnrecognizedVMOptions
-Dosgi.requiredJavaVersion=21
-Xms128m
-Xmx2048m
```

## Supported Versions

The following are the versions that have been tested:

- 26.1.0 (Windows, Ubuntu)