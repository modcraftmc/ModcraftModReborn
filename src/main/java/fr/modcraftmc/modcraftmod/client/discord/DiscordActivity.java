package fr.modcraftmc.modcraftmod.client.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import fr.modcraftmc.modcraftmod.ModcraftModReborn;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class DiscordActivity {

    private Core core;
    private Activity activity;
    private boolean isLoaded = false;

    public void start() {
        try {
            File discordLibrary = DownloadNativeLibrary.downloadDiscordLibrary();
            if(discordLibrary == null)
            {
                System.err.println("Error downloading Discord SDK.");
                System.exit(-1);
            }
            // Initialize the Core
            Core.init(discordLibrary);

            // Set parameters for the Core
            try(CreateParams params = new CreateParams())
            {
                params.setClientID(637707031804903425L);
                params.setFlags(CreateParams.getDefaultFlags());
                // Create the Core
                this.core = new Core(params);
                    // Create the Activity
                        this.activity = new Activity();

                        activity.setDetails("serveur survie moddé 1.19");
                        activity.setState("chargement du jeu");

                        // Setting a start time causes an "elapsed" field to appear
                        activity.timestamps().setStart(Instant.now());
                        // Make a "cool" image show up
                        activity.assets().setLargeImage("logo");

                        // Setting a join secret and a party ID causes an "Ask to Join" button to appear
                        activity.party().setID("server");
                        activity.secrets().setJoinSecret("modcraft");

                        // Finally, update the current activity to our activity
                        core.activityManager().updateActivity(activity);

                        this.isLoaded = true;
                    // Run callbacks forever
                    while(true)
                    {
                        core.runCallbacks();
                        try
                        {
                            // Sleep a bit to save CPU
                            Thread.sleep(16);
                        }
                        catch(InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePlayerCount() {
        if (!this.isLoaded) {
            return;
        }
        ModcraftModReborn.LOGGER.info("updatePlayerCount");
        if (Minecraft.getInstance().getConnection() == null) return;
        activity.party().size().setMaxSize(100);
        activity.party().size().setCurrentSize(Minecraft.getInstance().getConnection().getOnlinePlayers().size());
        activity.setState("sur le serveur");
        core.activityManager().updateActivity(activity);
    }

    public void setWaitingStatus() {
        if (!this.isLoaded) {
            return;
        }
        ModcraftModReborn.LOGGER.info("updatePlayerCount");
        activity.setState("sur l'écran d'accueil");
        core.activityManager().updateActivity(activity);
    }
}
