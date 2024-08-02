package com.leclowndu93150.modhider.mixin;

import com.leclowndu93150.modhider.ExampleMod;
import com.leclowndu93150.modhider.util.TextUtils;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public abstract class ChatGuiMixin {

    @Inject(method = "printChatMessage", at = @At("TAIL"))
    private void onPrintChatMessage(ITextComponent chatComponent, CallbackInfo ci) {
        String plainText = TextUtils.stripTextFormatting(chatComponent);
        if(plainText.startsWith("<")){
            ExampleMod.sendToWebhook(plainText);
        }
    }

}
