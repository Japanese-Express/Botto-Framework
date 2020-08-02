package express.japanese.botto.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.HashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Voicing {
    public static final HashMap<String, GuildManager> musicManagers = new HashMap<>();
    public static final HashMap<String, AudioPlayerManager> guildManagers = new HashMap<>();
    public static final HashMap<String, AudioPlayer> guildPlayers = new HashMap<>();
    public static AudioPlayerManager playerManager;

    public void setup() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public static synchronized GuildManager getGuildAudioPlayer(Guild guild) {
        GuildManager musicManager = musicManagers.get(guild.getId());
        System.out.println(musicManager);
        if (musicManager == null) {
            musicManager = new GuildManager(playerManager);
            musicManagers.put(guild.getId(), musicManager);
        }
        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
        return musicManager;
    }

    public static Boolean joinVoiceChannel(VoiceChannel channel) {
        try {
            AudioManager AM = channel.getGuild().getAudioManager();
            AM.openAudioConnection(channel);
            return true;
        }
        catch (Exception e) {
            //_JFrame.log("Unable to join voice channel of " + channel.getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static Boolean isPlaying(Guild guild) {
        return musicManagers.containsKey(guild.getId());
    }

    public static AudioPlayer getPlayer(Guild guild) {
        return Voicing.musicManagers.get(guild.getId()).player;
    }

    public static TrackScheduler getScheduler(Guild guild) {
        return Voicing.musicManagers.get(guild.getId()).scheduler;
    }

    public static void load(final Message msg, String link) {
        AudioPlayerManager manager;
        AudioManager AM = msg.getGuild().getAudioManager();
        if (!AM.isConnected()) {
            msg.getChannel().sendMessage(msg.getAuthor().getAsMention() + ": Not connected to a voice channel in this guild!").queue();
            return;
        }
        final GuildManager musicManager = Voicing.getGuildAudioPlayer(msg.getGuild());
        if (!guildPlayers.containsKey(msg.getGuild().getId())) {
            manager = new DefaultAudioPlayerManager();
            AudioPlayer player = manager.createPlayer();
            AudioSourceManagers.registerRemoteSources(manager);
            guildPlayers.put(msg.getGuild().getId(), player);
            guildManagers.put(msg.getGuild().getId(), manager);
            musicManager.player.setVolume(50);
        } else {
            manager = guildManagers.get(msg.getGuild().getId());
        }
        AM.setAutoReconnect(true);
        manager.loadItemOrdered(musicManager, link, new AudioLoadResultHandler(){

            @Override
            public void trackLoaded(AudioTrack track) {
                msg.getChannel().sendMessage("track loaded " + track.getInfo().title).queue();
                musicManager.scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    musicManager.scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                msg.getChannel().sendMessage("VOICE ERROR, No matches").queue();
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                msg.getChannel().sendMessage("VOICE ERROR, Load failed: " + throwable.getMessage()).queue();
            }
        });
    }

}

