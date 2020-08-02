package express.japanese.botto.misc;

import java.awt.Color;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class RichEmbed {
    private List<MessageEmbed.Field> fields;
    private Color color = Color.WHITE;
    private String description = null;
    private String title = null;
    private String avatarRedirect = null;
    private String avatar = null;
    private String footerText = null;
    private String footerIcon = null;
    private TemporalAccessor time = null;
    private String image = null;
    private String thumbnail = null;
    public RichEmbed() {
        fields = new ArrayList<>();
    }

    public RichEmbed setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public RichEmbed setTime(TemporalAccessor time) {
        this.time = time;
        return this;
    }

    public RichEmbed setColor(Color color) {
        this.color = color;
        return this;
    }

    public RichEmbed setDescription(String description) {
        this.description = description;
        return this;
    }

    public RichEmbed setTitle(String authorName) {
        this.title = authorName;
        return this;
    }

    public RichEmbed setImage(String image) {
        this.image = image;
        return this;
    }

    public RichEmbed setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public RichEmbed setFields(MessageEmbed.Field[] fields) {
        this.fields.addAll(Arrays.asList(fields));
        return this;
    }
    public RichEmbed setFields(List<MessageEmbed.Field> fields) {
        this.fields = fields;
        return this;
    }

    public RichEmbed setAvatarRedirect(String avatarRedirect) {
        this.avatarRedirect = avatarRedirect;
        return this;
    }

    public RichEmbed setFooterAsCurrentTime() {
        this.footerText = String.valueOf(new Date());
        return this;
    }
    public static String getCurrentTime() {
        return String.valueOf(new Date());
    }

    public RichEmbed setFooterText(String footerText) {
        this.footerText = footerText;
        return this;
    }

    public RichEmbed setFooterIcon(String footerIcon) {
        this.footerIcon = footerIcon;
        return this;
    }

    public RichEmbed addField(String name, String desc, boolean inline) {
        MessageEmbed.Field field = new MessageEmbed.Field(name, desc, inline);
        this.fields.add(field);
        return this;
    }

    public MessageEmbed build() {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(this.title, this.avatarRedirect, this.avatar);
        for(MessageEmbed.Field field : fields) {
            eb.addField(field);
        }
        eb.setThumbnail(this.thumbnail);
        eb.setColor(this.color);
        eb.setDescription(this.description);
        eb.setTimestamp(this.time);
        eb.setFooter(this.footerText, this.footerIcon);
        eb.setImage(this.image);
        return eb.build();
    }

    @Deprecated
    public static EmbedBuilder create(User user, String desc, String imageUrl, Color color, MessageEmbed.Field[] field) {
        EmbedBuilder eb = new EmbedBuilder().setAuthor(user.getName(), null, user.getAvatarUrl()).setDescription(desc).setImage(imageUrl).setColor(color);
        eb.setFooter(String.valueOf(new Date()), null);
        if (field != null) {
            for (MessageEmbed.Field s : field) {
                eb.addField(s);
            }
        }
        return eb;
    }

    @Deprecated
    public static EmbedBuilder create(User user, String url, String desc, String imageUrl, Color color, MessageEmbed.Field[] field) {
        EmbedBuilder eb = new EmbedBuilder().setAuthor(user.getName(), url, user.getAvatarUrl()).setDescription(desc).setImage(imageUrl).setColor(color);
        eb.setFooter(String.valueOf(new Date()), null);
        if (field != null) {
            for (MessageEmbed.Field s : field) {
                eb.addField(s);
            }
        }
        return eb;
    }

    @Deprecated
    public static EmbedBuilder create(String desc, String imageUrl, Color color, MessageEmbed.Field[] field) {
        EmbedBuilder eb = new EmbedBuilder().setDescription(desc).setImage(imageUrl).setColor(color);
        eb.setFooter(String.valueOf(new Date()), null);
        if (field != null) {
            for (MessageEmbed.Field s : field) {
                eb.addField(s);
            }
        }
        return eb;
    }
}

