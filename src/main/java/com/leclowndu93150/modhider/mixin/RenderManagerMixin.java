package com.leclowndu93150.modhider.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.ReportedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(RenderManager.class)
public abstract class RenderManagerMixin {
    @Shadow @Nullable public abstract <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn);

    @Shadow public TextureManager renderEngine;

    @Shadow private boolean renderOutlines;

    @Shadow private boolean debugBoundingBox;

    @Shadow protected abstract void renderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks);

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    public void renderEntity(Entity entityIn, double x, double y, double z, float yaw, float partialTicks, boolean p_188391_10_, CallbackInfo ci) {
        Render<Entity> render = null;
        try {
            render = this.<Entity>getEntityRenderObject(entityIn);

            if (render != null && this.renderEngine != null) {
                try {
                    render.setRenderOutlines(this.renderOutlines);
                    render.doRender(entityIn, x, y, z, yaw, partialTicks);
                } catch (Throwable throwable1) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
                }

                try {
                    if (!this.renderOutlines) {
                        render.doRenderShadowAndFire(entityIn, x, y, z, yaw, partialTicks);
                    }
                } catch (Throwable throwable2) {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Post-rendering entity in world"));
                }

                if (this.debugBoundingBox && !p_188391_10_ && !Minecraft.getMinecraft().isReducedDebug()) {
                    try {
                        this.renderDebugBoundingBox(entityIn, x, y, z, yaw, partialTicks);
                    } catch (Throwable throwable) {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }
            }
        } catch (Throwable throwable) {
            throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity in world"));
        }
        ci.cancel();
    }
}
