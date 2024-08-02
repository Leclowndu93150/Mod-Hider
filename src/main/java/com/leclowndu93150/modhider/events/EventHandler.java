package com.leclowndu93150.modhider.events;


import com.leclowndu93150.modhider.ExampleMod;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        String message = event.getMessage();
        if (message.startsWith("/")) {
            return;
        }

        String returnedMessage ="["+ event.getUsername() + "] " + message;

        ExampleMod.sendToWebhook(returnedMessage);
    }
}
