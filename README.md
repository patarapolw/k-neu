# k-neu

Run websites in Windowed mode

## How to build

<!-- markdownlint-disable MD014 -->
```shell script
$ ./gradlew shadowJar  # or ./gradlew.bat shadowJar
```

You can also build for associated platforms by setting environment variable `OSGI_PLATFORM`

- `cocoa.macosx.x86_64`
- `win32.win32.x86_64`
- `gtk.linux.x86_64`
- `gtk.linux.ppc64le`

Resulting build is in `/build/libs/*-${OSGI_PLATFORM}.jar`

## Personalizing the app

Create [config.json](/src/main/resources/config.json) and put in alongside `*.jar`

```json
{
  "title": "app1",
  "url": "index.html",
  "maximized": true
}
```

Or,

```json
{
  "title": "app2",
  "server": ["java", "-jar", "server.jar"],
  "url": "http://localhost:${PORT}",
  "maximized": true
}
```

Of course, you can just edit [src/main/kotlin/App.kt](/src/main/kotlin/App.kt) directly. So that you can run server without multiple JAR files.
