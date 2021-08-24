package Audio;

import java.util.HashMap;
//import Audio.AudioPlayer;


public class AudioHandler {
    public HashMap<String , AudioPlayer> sfx;
    private AudioPlayer sound;

    public AudioHandler(){
        sfx = new HashMap<String , AudioPlayer>();
        sfx.put("hurt", new AudioPlayer("/Audio/SFX/Minecraft hurt  sound effect.mp3"));
        sfx.put("shoot", new AudioPlayer("/Audio/SFX/shootFart.mp3"));
    }
}
