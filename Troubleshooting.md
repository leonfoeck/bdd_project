# Troubleshooting

### Table of contents

 - [Maven not working](#maven)
 - [Not a valid Tomcat home](#tomcat)
 - ["Ein Fehler ist aufgetreten": stack trace in web-app](#web-app)

### Maven not working
<a name="maven"/>

If it is behaving strangely like

  - 
    ```
    Compilation failure error: invalid target release: 16   Target level is invalid or missing in pom.xml. Specify target level.`
    ```
  
  - 
    ```
    The JAVA_HOME environment variable is not defined correctly
    This environment variable is needed to run this program
    NB: JAVA_HOME should point to a JDK not a JRE
    ```
    
  - 
    ```
    [ERROR] Error executing Maven.
    [ERROR] java.lang.IllegalStateException: Unable to load cache item
    [ERROR] Caused by: Unable to load cache item
    [ERROR] Caused by: Could not initialize class com.google.inject.internal.cglib.core.$MethodWrapper
    ```

check `mvn -version` in the terminal.
If it is also behaving strangely/telling you it is on `Apache Maven 3.6.3` you might need a newer version
(`Apache Maven 3.8.2`).
Unfortunately, `sudo apt-get --only-upgrade install maven` will get you nowhere (on September 23, 2021).
You need to upgrade it manually.

A really good tutorial can be found here:
https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-20-04/

Please notice that you have to replace all the `3.6.3` specific stuff in the tutorial with `3.8.2` paths/names to make
it work.
Furthermore, I use my installation of the `openjdk-16` as `$JAVA_HOME`.

### "Warning: The selected directory is not a valid Tomcat home"
<a name="tomcat"/>

#### Linux

If you are **really sure** that the directory is actually a valid Tomcat home there might be an issue with the permissions.
This can happen if you install Tomcat globally (like, e.g., in `/opt/tomcat/`) and not simply unpack it somewhere
in your home directory (e.g., in `/home/opt/tomcat/`).

You can fix this changing the permissions with typing the following command in your shell:

```
sudo chmod 755 -R /opt/tomcat
```

where `/opt/tomcat` is just an example for a path that is not reachable for `IntelliJ`. You should replace it with your
Tomcat location.

Link to answer: https://stackoverflow.com/questions/33055786/warning-the-selected-directory-is-not-a-valid-tomcat-home

#### Mac

Try selecting the libexec subdirectory in IntelliJ, i.e., `/usr/local/Cellar/tomcat/8.5.9/libexec`

Link to answer: https://stackoverflow.com/questions/33055786/warning-the-selected-directory-is-not-a-valid-tomcat-home

### "Ein Fehler ist aufgetreten": stack trace in web-app
<a name="web-app"/>

Look at the full stack trace in IntelliJ.

- if you can find the following exception (at `Configuration.getStoragePath()`), there might be something wrong with 
setting the `tmp` location.

  ```
  Caused by: java.lang.RuntimeException
  at util.global.de.uni_passau.fim.se2.st.mensawebapp.Configuration.getStoragePath(Configuration.java:151)
  at util.global.de.uni_passau.fim.se2.st.mensawebapp.Configuration$Proxy$_$$_WeldClientProxy.getStoragePath(Unknown Source)
  at service.business.de.uni_passau.fim.se2.st.mensawebapp.DishService.<init>(DishService.java:35)
  at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
  at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:78)
  at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
  at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:499)
  at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:480)
  ```

    This can be fixed in two different ways:

    - in the `Edit Configurations` menu, change to the Startup/Connection tab. Check the `Pass environment variables` box,
    and add a new one with the name `CATALINA_TMPDIR` and the value `/tmp/` on Linux (on Windows it should be something
    like `C:\Users\<username>\AppData\Local\Temp` (?), but you can find a suitable directory typing `ECHO %Temp%` 
    in your `CMD` prompt).
    
      Please be sure to add the variable for the *Run, Debug, Coverage, and Profile* configuration to avoid problems with configuration
      inconsistencies later. You can change the mode for which you add the environment variables in the upper part of
      the menu.
    - add a value for the `storagePath` in the `src/main/resources/mensawebapp.properties` file. This bypasses the
    automated selection of the `tmp` directory. Add the line `application.storagePath=/tmp/` on Linux (or any other
    suitable tmp directory on any other operating system).
    - maybe this (https://stackoverflow.com/questions/66054983/system-getpropertyjava-io-tmpdir-not-returning-correct-location)
    could also work (but did not work for me).
    