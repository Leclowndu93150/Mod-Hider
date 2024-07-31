package com.example.modid;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class ExampleMod {

    private static final String CONFIG_FILENAME = "hiddenmods.txt";
    private static List<String> hiddenMods = new ArrayList<>();

    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configDir = event.getModConfigurationDirectory();
        File configFile = new File(configDir, CONFIG_FILENAME);
        loadHiddenModsConfig(configFile);
    }

    private void loadHiddenModsConfig(File configFile) {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                // Add a sample entry to the file
                Files.write(configFile.toPath(), "examplemodid1,examplemodid2".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String content = new String(Files.readAllBytes(configFile.toPath()));
            if (!content.trim().isEmpty()) {
                hiddenMods = Arrays.asList(content.split("\\s*,\\s*"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print the list of hidden mods for debugging
        LOGGER.info("Hidden Mods: " + hiddenMods);
    }

    public static List<String> getHiddenMods() {
        return hiddenMods;
    }

}
