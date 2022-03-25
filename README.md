# Kingdoms - Minecraft Plugin

---

##Use Kingdoms in your project - Maven and Gradle
###Using maven with pom.xml :
> Kingdoms Plugin is available in the public Jitpack repos : [![](https://jitpack.io/v/Bl0ckincraft/Kingdoms.svg)](https://jitpack.io/#Bl0ckincraft/Kingdoms) <br>
> To add it, you must add this into your pom.xml :
```xml
	    <repositories>
		    <repository>
		        <id>jitpack.io</id>
		        <url>https://jitpack.io</url>
		    </repository>
	    </repositories>
```
> Finally, add this into the dependencies part and replace `%version%` by this number : [![](https://jitpack.io/v/Bl0ckincraft/Kingdoms.svg)](https://jitpack.io/#Bl0ckincraft/Kingdoms)
```xml
        <dependency>
            <groupId>com.github.Bl0ckincraft</groupId>
            <artifactId>Kingdoms</artifactId>
            <version>%version%</version>
        </dependency>
```
###Using gradle with build.gradle :
> Kingdoms Plugin is available in the public Jitpack repos : [![](https://jitpack.io/v/Bl0ckincraft/Kingdoms.svg)](https://jitpack.io/#Bl0ckincraft/Kingdoms) <br>
> To add it, you must add the repos into your build.gradle at the end of the repositories :
```gradle
        allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }
```
> Finally, add this into the dependencies part and replace `%version%` by this number : [![](https://jitpack.io/v/Bl0ckincraft/Kingdoms.svg)](https://jitpack.io/#Bl0ckincraft/Kingdoms)
```gradle
        dependencies {
	        implementation 'com.github.Bl0ckincraft:Kingdoms:%version%'
	    }
```