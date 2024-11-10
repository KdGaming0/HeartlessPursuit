package tech.kd_gaming1.heartless_pursuit;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.kd_gaming1.heartless_pursuit.config.HeartlessPursuitConfig;

public class HeartlessPursuit implements ModInitializer {
    public static final String MOD_ID = "heartlesspursuit";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MidnightConfig.init(MOD_ID, HeartlessPursuitConfig.class);
        PlayerStealXP.init();
    }
}