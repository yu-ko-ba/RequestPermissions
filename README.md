# RequestPermissions

## Installation
settings.gradle.kts
```settings.gradle.kts
dependencyResolutionManagement {
    ...
    repositories {
        ...
+       maven { setUrl("https://jitpack.io") }
    }
}
```

build.gradle.kts
```app/build.gradle.kts
dependencies {
    ...
+   implementation("com.github.yu-ko-ba:RequestPermissions:0.0.1")
}
```
