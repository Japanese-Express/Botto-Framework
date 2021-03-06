package express.japanese.botto.core.modules.preInstalled;

import express.japanese.botto.core.modules.enums.ICategory;
import express.japanese.botto.core.modules.enums.Language;
import express.japanese.botto.core.modules.interfaces.annotations.ILanguage;
import express.japanese.botto.core.modules.module.CmdModule;
import express.japanese.botto.core.modules.module.Module;
import express.japanese.botto.core.modules.interfaces.Author;
import express.japanese.botto.core.modules.interfaces.annotations.IModule;
import express.japanese.botto.core.modules.enums.BotCategory;
import express.japanese.botto.core.modules.interfaces.IsDefault;
import express.japanese.botto.misc.modulistic.RichEmbed;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

@IModule(   names={"help", "helpme", "helpp", "help-me"},
        tinyDescription = {
                @ILanguage(language = Language.ENGLISH,
                        value = "Gives some help")},
        category= BotCategory.Bot,
        channelTypes = {
                ChannelType.TEXT, ChannelType.GROUP, ChannelType.PRIVATE
        })
@IsDefault
@Author("cutezyash#7654")
public class CmdHelp extends CmdModule {
    @Override
    public void run(String cmd, @NotNull String[] args, Message msg) {
        //PrivateChannel channel = msg.getAuthor().openPrivateChannel().complete();
        MessageChannel channel = msg.getChannel();
        Map.Entry<ICategory, List<Module>> selectedCategory = null;
        if (args.length > 0) {
            Map<ICategory, List<Module>> arrangedMap = this.botControllerInst.modulesByCategory;
            for (Map.Entry<ICategory, List<Module>> entry : arrangedMap.entrySet()) {
                if(entry.getValue().isEmpty())
                    continue;
                if(args[0].equalsIgnoreCase(entry.getKey().getName())) {
                    selectedCategory = entry;
                    break;
                }
            }
            if(selectedCategory == null) {
                String modulestring = args[0];
                getHelpFrom(modulestring, msg);
                return;
            }
        }
        channel.sendMessage(createRichEmbed(msg, selectedCategory).build()).queue();
        //channel.close().queue();
    }

    private RichEmbed createRichEmbed(Message msg, Map.Entry<ICategory, List<Module>> selectedCategory) {
        String desc =   "**A powerful yet simple Discord Bot framework: http://github.com/Japanese-Express/Botto-Framework*"
                +       "\n\n__Use `"+getPrefix()[0]+"help <cmd>` for help on any module__";
        RichEmbed richEmbed = new RichEmbed()
                .setFooterAsCurrentTime()
                .setColor(Color.PINK)
                .setDescription(desc)
                .setTitle(msg.getAuthor().getName())
                .setThumbnail(getJDA().getSelfUser().getAvatarUrl())
                .setAvatar(msg.getAuthor().getAvatarUrl());
                //.setDescription(this.getmodules().toString());
        Map<ICategory, List<Module>> arrangedMap = this.botControllerInst.modulesByCategory;
        if(selectedCategory == null) {
            for (Map.Entry<ICategory, List<Module>> entry : arrangedMap.entrySet()) {
                if (entry.getValue().isEmpty())
                    continue;
                String category = entry.getKey().getName();
                StringBuilder help = new StringBuilder();
                List<Module> modules = entry.getValue();
                for(int i = 0; i< modules.size(); i++) {
                    Module module = modules.get(i);
                    IModule annotation = module.getModuleInterface();
                    if(!annotation.useInHelp())
                        continue;
                    if (i < modules.size()-1) {
                        help.append(annotation.names()[0]).append(", ");
                    } else
                        help.append(annotation.names()[0]);
                }
                if(!help.toString().trim().isEmpty())
                    richEmbed.addField(category, help.toString(), false);
            }
        } else {
            String category = selectedCategory.getKey().getName();
            StringBuilder help = new StringBuilder();
            List<Module> modules = selectedCategory.getValue();
            for(int i=0;i < modules.size();i++) {
                Module module = modules.get(i);
                IModule annotation = module.getModuleInterface();
                if(!annotation.useInHelp())
                    continue;
                if (i < modules.size()-1) {
                    help.append("**").append(annotation.names()[0]).append("**\n");
                    help.append(annotation.tinyDescription()).append("\n\n");
                } else
                    help.append(annotation.names()[0]);
            }
            richEmbed.addField(category, help.toString(), false);
        }
        return richEmbed;
    }

    public void getHelpFrom(String cmd, Message msg) {
        Module module = this.botControllerInst.getModule(cmd);
        if (module != null) {
            MessageChannel channel = msg.getChannel();
            //PrivateChannel channel = msg.getAuthor().openPrivateChannel().complete();
            IModule annotation = module.getModuleInterface();
            StringBuilder text = new StringBuilder("Help information for **" + annotation.names()[0] + "**:");
            if (annotation.names().length > 1) {
                text.append("\n    *Alt Names*: ");
                for (int i = 1; i < annotation.names().length; ++i) {
                    text = new StringBuilder(i == annotation.names().length - 1 ? text + annotation.names()[i] : text + annotation.names()[i] + ", ");
                }
            }
            if(annotation.permissions().usePermissions()) {
                text.append(" \n **Required Permissions**: ");
                for(Permission permission : annotation.permissions().permissionsToUse()) {
                    text.append("\n  - *").append(permission.getName()).append("*");
                }
            }
            String tiny = this.getTinyDescByLang(Language.ENGLISH).value();
            String full = this.getFullDescByLang(Language.ENGLISH).value();
            text.append("\n\n")
                    .append(tiny)
                    .append(!full.isEmpty() ? "\n\n" + full : "");
            channel.sendMessage(text.toString()).queue();
            //channel.close().queue();
        } else
            msg.getChannel().sendMessage("That module does not exist, did you misspell it?").queue();
    }

    public StringBuilder getmodules() {
        StringBuilder help = new StringBuilder("All the premodules you can use:\n\n\n");
        TreeMap<ICategory, List<Module>> arrangedMap = new TreeMap<>(this.botControllerInst.modulesByCategory);
        for (Map.Entry<ICategory, List<Module>> entry : arrangedMap.entrySet()) {
            help.append(entry.getKey().getName()).append(" Category:\n");
            List<Module> modules = entry.getValue();
            for (int i = 0; i < modules.size() - 1; ++i) {
                Module module = modules.get(i);
                IModule annotation = module.getModuleInterface();
                help = new StringBuilder(i == modules.size() - 1 ? help + annotation.names()[0] : help + annotation.names()[0] + ", ");
            }
            help.append("\n\n");
        }
        return help;
    }
}

