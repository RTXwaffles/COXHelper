package com.example.olmhelper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("olmhelper")
public interface OlmHelperConfig extends Config
{
    @ConfigItem(
            keyName = "showPrayerOverlay",
            name = "Show Prayer Overlay",
            description = "Enable or disable the prayer overlay during Olm fight"
    )
    default boolean showPrayerOverlay()
    {
        return true;
    }
}
