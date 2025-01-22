package com.example.olmhelper;

import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Config;
import javax.inject.Inject;
import com.google.common.eventbus.Subscribe;
import net.runelite.client.eventbus.EventBus;


@PluginDescriptor(
        name = "Olm Protection Prayer Helper",
        description = "Displays the appropriate protection prayer to use during the Olm fight in Chambers of Xeric",
        tags = {"olm", "prayer", "chambers", "xeric"}
)
public class OlmHelperPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private ClientThread clientThread;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private OlmPrayerOverlay olmPrayerOverlay;

    @Inject
    private OlmHelperConfig config;

    private boolean showPrayerOverlay;

    @Override
    protected void startUp() throws Exception
    {
        showPrayerOverlay = config.showPrayerOverlay();
        overlayManager.add(olmPrayerOverlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(olmPrayerOverlay);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event)
    {
        if (event.getGroup().equals("olmhelper"))
        {
            if (event.getKey().equals("showPrayerOverlay"))
            {
                showPrayerOverlay = Boolean.parseBoolean(event.getNewValue());
            }
        }
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (!showPrayerOverlay)
        {
            return;
        }

        // Check if we are fighting Olm (you can use different methods to check if the player is fighting Olm)
        NPC olm = getOlmNpc();
        if (olm != null)
        {
            // Check Olm's attack type and set the appropriate prayer
            int attackType = getOlmAttackType(olm);

            switch (attackType)
            {
                case 0: // Melee
                    setProtectionPrayer(Prayer.PROTECT_FROM_MELEE);
                    break;
                case 1: // Ranged
                    setProtectionPrayer(Prayer.PROTECT_FROM_MISSILES);
                    break;
                case 2: // Magic
                    setProtectionPrayer(Prayer.PROTECT_FROM_MAGIC);
                    break;
                default:
                    break;
            }
        }
    }

    private NPC getOlmNpc()
    {
        // You can identify Olm by its unique NPC ID in the Chambers of Xeric.
        for (NPC npc : client.getNpcs())
        {
            if (npc.getId() == 6807) // Replace with Olm's actual NPC ID
            {
                return npc;
            }
        }
        return null;
    }

    private int getOlmAttackType(NPC olm)
    {
        // Check Olm's current attack style. This logic should be based on his current phase or attack animation.
        // For this example, we'll make some assumptions, but you can expand this with more checks.

        // Just an example; in a real plugin, this should be based on specific animations or combat states.
        if (olm.getAnimation() == 7550)  // Example: Olm's melee attack animation
        {
            return 0; // Melee
        }
        else if (olm.getAnimation() == 7551) // Example: Olm's ranged attack animation
        {
            return 1; // Ranged
        }
        else if (olm.getAnimation() == 7552) // Example: Olm's magic attack animation
        {
            return 2; // Magic
        }

        return -1; // Default case (no attack detected)
    }

    private void setProtectionPrayer(Prayer prayer)
    {
        if (!client.getPrayerManager().isPrayerEnabled(prayer))
        {
            client.getPrayerManager().setActivePrayer(prayer, true);
        }
    }
}
