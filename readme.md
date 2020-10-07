# Botto-Framework
###### A framework made for the Japanese Express brand, and it's bots

## Features
1. **Easy to use and to understand for anyone**
2. **Built in version checker once the bot is initialized**
3. **Instantly create a module for your bot, along with interfaces to easily read information about each module**
4. **Built in JSON readers and generic Java styling for JSON responses**
5. **Built in configurations including global and user config styles**
6. **Creating multiple bots including shards & detailing as much as you need for your project**
7. **Custom interfaces including defining who owns what module, defining default modules, and more**
8. **Create as much detail as you want with annotations, making your modules yours**
9. **Latest version of the JDA core**


## How to Use
Using this bot framework is pretty simple actually!<br/>
1. Grab a jar for the framework from the [releases](http://github.com/Japanese-Express/Botto-Framework) tab, or build, and add it as a library to your project
2. Create a main class for your bot, where the bot will automatically start.
   * Import the `Botto` class and use the following to start work on your first bot:
   * ```java
      // Create new bot
      BotBuilder builder = Botto.initialize();
      // Set token for that bot
      builder.setToken("YourToken");
      // And build it (making it start)
      BotController controller = builder.build();
      ``` 
   * You now have a bot controller class. This is the class that's going to control the modules and the commands for your bot
3. Create a command module for your bot
   * Create a new class for the module, and import `IModule` and `CmdModule`
   * Here is an example CmdModule using IModule and CmdModule together
   * ```java
     @IModule(
             names = {"google", "g"},
             category = Category.Utility,
             tinyDescription = "Returns back to the user a google link",
             channelTypes = {ChannelType.TEXT, ChannelType.PRIVATE}
     )
     public class google extends CmdModule {
         public void run(String cmd, String[] args, Message msg) {
             msg.getChannel().sendMessage("https://google.com/?q="+args[0]).queue();
         }
     }
     ```
   * You can edit your Module interface however you want, this is just a base class. There are also built in modules such as "HelpCmd" and "VersionCmd"
4. Import your modules!
   * Importing modules to your controller is easy! On the `BotController` you created earlier, just do the following:
   * ```java
     // This is a good example because you can actually see
     // all the built in commands inside this directory. This
     // is just the package of where all your commands are located. 
     controller.initializeModulesFromPackage("express.japanese.botto.modules.preInstalled");
     
     // You can also do just one at a time with the following:
     controller.initializeModule(HelpCmd.class);
     ```
4. Disabling default modules, ignoring version check or custom listeners?
   * You can do all these things, ignoring version is a bit different though.
   * For custom listeners however, you must
   * ```java
     builder.setShouldCreateDefaultCmds(false);
     builder.setListener(BotCmdListener.class)
     ```
     