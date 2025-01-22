package com.example.olmhelper;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TextComponent;

import javax.inject.Inject;
import java.awt.*;

public class OlmPrayerOverlay extends Overlay
{
    private final Client client;
    private final OlmHelperPlugin plugin;

    @Inject
    public OlmPrayerOverlay(Client client, OlmHelperPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.TOP_CENTER); // Position on the screen
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.showPrayerOverlay)
        {
            return null;  // Don't render if the overlay is disabled
        }

        String prayerMessage = "Current Protection Prayer: " + getCurrentPrayer();
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));

        // Draw the prayer message on the screen
        graphics.drawString(prayerMessage, 10, 50);

        return new Dimension(0, 0);
    }

    private String getCurrentPrayer()
    {
        // Retrieve the current active prayer from the client and return it as a string.
        if (client.getPrayerManager().isPrayerEnabled(Prayer.PROTECT_FROM_MELEE))
        {
            return "Melee";
        }
        if (client.getPrayerManager().isPrayerEnabled(Prayer.PROTECT_FROM_MISSILES))
        {
            return "Ranged";
        }
        if (client.getPrayerManager().isPrayerEnabled(Prayer.PROTECT_FROM_MAGIC))
        {
            return "Magic";
        }
        return "None";  // If no prayer is enabled
    }
}
