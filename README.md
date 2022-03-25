# Kingdoms - Minecraft Plugin - 1.17
This plugin adds a new team system with GUI using the claims of [GriefPrevention](https://www.spigotmc.org/resources/griefprevention.1884/). <br>
It is a fully configurable plugin created by [Blockincraft](https://github.com/Bl0ckincraft) and [Kellyan](https://github.com/TchaineKems).

---

## Plugin features

* Members of kingdoms are all trusted players who have the build claim permission.
* New bank system. When kingdom deleted, the money was given to the owner.
* New collections system with rewards and fully configurable.
* Management GUI with personalised items.
* Configurables messages and commands.
* Commands permissions.

> For more explanations, you can take a look at the [wiki](https://github.com/Bl0ckincraft/Kingdoms/wiki).

---

## Plugin dependencies

To work, this plugin need three others plugin :

- [Vault](https://www.spigotmc.org/resources/vault.34315/) - 1.7 +
- [GriefPrevention](https://www.spigotmc.org/resources/griefprevention.1884/) - 16.17.1 +
- A plugin which add an economy like [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/)
---

## Use Kingdoms in your project - Maven and Gradle

> Kingdoms Plugin is available in the public **Jitpack** repo : [![](https://jitpack.io/v/Bl0ckincraft/Kingdoms.svg)](https://jitpack.io/#Bl0ckincraft/Kingdoms)

### Using maven with pom.xml :

> To add it, you must add this into your pom.xml :
```xml
	    <repositories>
		    <repository>
		        <id>jitpack.io</id>
		        <url>https://jitpack.io</url>
		    </repository>
	    </repositories>
```
> Finally, add this into the dependencies part and replace `%tag%` by this tag : [![](https://jitpack.io/v/Bl0ckincraft/Kingdoms.svg)](https://jitpack.io/#Bl0ckincraft/Kingdoms)
```xml
        <dependency>
            <groupId>com.github.Bl0ckincraft</groupId>
            <artifactId>Kingdoms</artifactId>
            <version>%tag%</version>
        </dependency>
```
### Using gradle with build.gradle :

> To add it, you must add the repos into your build.gradle at the end of the repositories :
```gradle
        allprojects {
            repositories {
                ...
                maven { url 'https://jitpack.io' }
            }
        }
```
> Finally, add this into the dependencies part and replace `%tag%` by this tag : [![](https://jitpack.io/v/Bl0ckincraft/Kingdoms.svg)](https://jitpack.io/#Bl0ckincraft/Kingdoms)
```gradle
        dependencies {
	        implementation 'com.github.Bl0ckincraft:Kingdoms:%tag%'
	    }
```
