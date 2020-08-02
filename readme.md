# Botto-Framework
###### A framework made for the Japanese Express brand, and it's bots

## Features
* **Instantly create any type of module, being a command or not**
* **Built in configurations**
* **Create multiple bots with ease! Even on the same jar!**
* **Easy to use and to understand for anyone**
* **Latest version of the JDA core**
* **Built in version checker once the bot is initialized**
* **Custom interfaces including authors, default modules, and more**
* **Create as much detail as you want with annotations, making your modules yours**

## How to Use
Using this bot framework is pretty simple actually!<br/>
Here's a list on how to use it!
1. Build the project.<br/>
1.1. the pom.xml should have all the dependencies you need for it
2. Pack into a JAR and use as a dependency on your own project
3. Create a command module, extending `CmdModule` and use the annotation above it `@IModule()`
4. In your main class, use the method `Botto::initialize` and follow the bot builder as you want it to be then use `build()`!
5. Your bot will automatically start itself once `build()` is ran, as long as everything is correct!  