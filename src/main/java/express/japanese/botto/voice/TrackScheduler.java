package express.japanese.botto.voice;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.LinkedList;
import java.util.Queue;

public class TrackScheduler
extends AudioEventAdapter {
    public final AudioPlayer player;
    public final Queue<AudioTrack> queue = new LinkedList<>();

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    public void queue(AudioTrack track) {
        if (!this.player.startTrack(track, true))
            this.queue.offer(track);
    }

    public void nextTrack() {
        AudioTrack track = this.queue.poll();
        this.player.startTrack(track, false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext)
            this.nextTrack();
    }
}

