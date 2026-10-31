# DBeaver Agent 26

## 依赖

由于使用了来自 DBeaver 的代码,所以你需要准备好 DBeaver 的一些包  
把下列的包放入到 lib 文件夹,并更新 pom.xml

- `com.dbeaver.lm.api` 许可核心
- `org.jkiss.utils` 提供一些组件供许可生成

## 怎么用?

直接 `mvn package` 构建就可以了.
生成的 `dbeaver-agent-26.2-SNAPSHOT-jar-with-dependencies.jar` 重命名为 `dbeaver-agent.jar` 放到 DBeaver 安装目录

运行以下命令生成许可证密钥:

- Windows: `java -cp "./dbeaver-agent.jar;plugins/*" com.dbeaver.agent.Main`
- macOS/Linux: `java -cp "./dbeaver-agent.jar:plugins/*" com.dbeaver.agent.Main`

修改 DBeaver 安装目录的 `dbeaver.ini` 给他加点参数  

- 在 `-vmargs` 上面一行加

    ```ini
    -vm
    <你的 jre21/jdk21 java 路径，如 /usr/lib/jvm/java-21-openjdk-amd64/bin/java>
    ```
    
    > 也可以不添加 -vm 参数,直接使用系统默认的 java, 或者自己下载 jre21 替换到安装目录
    > 
    > Windows 下使用 javaw.exe 避免弹出小黑框

- 在 `-vmargs` 下面一行加 

    ```ini
    -javaagent:<你的jar路径，如 ./dbeaver-agent.jar>
    ```

就像这样

```ini
-startup
plugins/org.jkiss.dbeaver.launcher_1.0.54.202606291110.jar
--launcher.library
plugins/org.eclipse.equinox.launcher.gtk.linux.x86_64_1.2.1500.v20250801-0854
-vm
/usr/lib/jvm/java-21-openjdk-amd64/bin/java
-vmargs
-javaagent:./dbeaver-agent.jar
-XX:+IgnoreUnrecognizedVMOptions
-Dosgi.requiredJavaVersion=21
-Xms128m
-Xmx2048m
```

## Team Docker

生成的 `dbeaver-agent-26.2-SNAPSHOT-jar-with-dependencies.jar` 重命名为 `dbeaver-agent.jar` 放到 team 目录

## 支持版本

下列是已经测试过的版本:

- 26.1.0 (Windows, Ubuntu)
- 26.2.0 (macOS)
