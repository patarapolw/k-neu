# k-neu

Run websites in Windowed mode

## How to build

```shell script
$ ./gradlew shadowJar  # or ./gradlew.bat shadowJar
```

You can also build for associated platforms by setting environment variable `OSGI_PLATFORM`

- `cocoa.macosx.x86_64`
- `win32.win32.x86_64`
- `gtk.linux.x86_64`
- `gtk.linux.ppc64le`

Resulting build is in `/build/libs/*-${OSGI_PLATFORM}.jar`
