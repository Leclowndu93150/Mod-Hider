package com.leclowndu93150.modhider;

import okhttp3.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod(modid = "modid", name = "Mod Hider", version = "1.0")
public class ExampleMod {

    private static final String CONFIG_FILENAME = "hiddenmods.txt";
    private static final String WEBHOOK_FILENAME = "webhook.txt";
    private static List<String> hiddenMods = new ArrayList<>();
    private static String webhookUrl = "";
    private static final Logger LOGGER = LogManager.getLogger();

    // OkHttpClient instance for HTTP requests
    private static final OkHttpClient client = new OkHttpClient();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configDir = event.getModConfigurationDirectory();
        File configFile = new File(configDir, CONFIG_FILENAME);
        loadHiddenModsConfig(configFile);
        File webhookFile = new File(configDir, WEBHOOK_FILENAME);
        loadWebhookUrl(webhookFile);
        //MinecraftForge.EVENT_BUS.register(new EventHandler()); // Uncomment if you have EventHandler class
    }

    private void loadHiddenModsConfig(File configFile) {
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                Files.write(configFile.toPath(), "examplemodid1,examplemodid2".getBytes());
            } catch (IOException e) {
                LOGGER.error("Error creating hidden mods config file", e);
            }
        }

        try {
            String content = new String(Files.readAllBytes(configFile.toPath()));
            if (!content.trim().isEmpty()) {
                hiddenMods = Arrays.asList(content.split("\\s*,\\s*"));
            }
        } catch (IOException e) {
            LOGGER.error("Error reading hidden mods config file", e);
        }

        LOGGER.info("Hidden Mods: " + hiddenMods);
    }

    private void loadWebhookUrl(File webhookFile) {
        if (!webhookFile.exists()) {
            try {
                webhookFile.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Error creating webhook URL file", e);
            }
        }

        try {
            String content = new String(Files.readAllBytes(webhookFile.toPath()));
            if (!content.trim().isEmpty()) {
                webhookUrl = content.trim();
            }
        } catch (IOException e) {
            LOGGER.error("Error reading webhook URL file", e);
        }

        LOGGER.info("Webhook URL: " + webhookUrl);
    }

    public static List<String> getHiddenMods() {
        return hiddenMods;
    }

    public static void sendToWebhook(String message) {
        if (webhookUrl.isEmpty()) {
            LOGGER.error("Webhook URL is not set.");
            return;
        }

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create("{\"content\":\"" + message + "\"}", JSON);
        Request request = new Request.Builder()
                .url(webhookUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                LOGGER.error("Error sending to webhook", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    LOGGER.error("Failed to send message. Response code: " + response.code());
                    if (response.body() != null) {
                        LOGGER.error("Error response: " + response.body().string());
                    }
                }
            }
        });
    }
}