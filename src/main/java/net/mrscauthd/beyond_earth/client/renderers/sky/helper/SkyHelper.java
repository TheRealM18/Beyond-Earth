package net.mrscauthd.beyond_earth.client.renderers.sky.helper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.mrscauthd.beyond_earth.BeyondEarth;
import org.apache.commons.lang3.tuple.Triple;

public class SkyHelper {

    /** PLANETS */
    public static final ResourceLocation WHITE_SUN = new ResourceLocation(BeyondEarth.MODID, "textures/sky/white_sun.png");
    public static final ResourceLocation SUN = new ResourceLocation(BeyondEarth.MODID, "textures/sky/sun.png");
    public static final ResourceLocation MOON_PHASE = new ResourceLocation(BeyondEarth.MODID, "textures/sky/moon_phases.png");
    public static final ResourceLocation EARTH = new ResourceLocation(BeyondEarth.MODID, "textures/sky/earth.png");
    public static final ResourceLocation MARS = new ResourceLocation(BeyondEarth.MODID, "textures/sky/mars.png");
    public static final ResourceLocation PHOBOS = new ResourceLocation(BeyondEarth.MODID, "textures/sky/phobos.png");
    public static final ResourceLocation DEIMOS = new ResourceLocation(BeyondEarth.MODID, "textures/sky/deimos.png");

    /** LIGHTS */
    public static final ResourceLocation PLANET_LIGHT = new ResourceLocation(BeyondEarth.MODID, "textures/sky/planet_light.png");
    public static final ResourceLocation PLANET_PHASE_LIGHT = new ResourceLocation(BeyondEarth.MODID, "textures/sky/planet_phases_light.png");

    /** RAIN */
    public static final ResourceLocation MARS_DUST = new ResourceLocation(BeyondEarth.MODID, "textures/sky/mars_dust.png");
    public static final ResourceLocation SNOW = new ResourceLocation("textures/environment/snow.png");

    public static void drawStars(VertexBuffer vertexBuffer, Matrix4f matrix4f, Matrix4f projectionMatrix, ShaderInstance shaderInstance, Runnable setupFog, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        FogRenderer.setupNoFog();
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
        setupFog.run();

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void drawPlanetWithMoonPhaseAndWithLight(ResourceLocation texture, Vec3 lightColor, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float y, Minecraft mc, boolean blend) {
        drawPlanetWithMoonPhase(texture, new Vec3(255, 255, 255), bufferBuilder, matrix4f, size, y, mc, blend);
        drawPlanetWithMoonPhase(PLANET_PHASE_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, mc, true);
    }

    public static void drawPlanetWithMoonPhase(ResourceLocation texture, Vec3 color, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float y, Minecraft mc, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        int r = (int) color.x();
        int g = (int) color.y();
        int b = (int) color.z();

        RenderSystem.enableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, texture);
        int k = mc.level.getMoonPhase();
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float)(l + 0) / 4.0F;
        float f14 = (float)(i1 + 0) / 2.0F;
        float f15 = (float)(l + 1) / 4.0F;
        float f16 = (float)(i1 + 1) / 2.0F;
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix4f, -size, -y, size).color(r, g, b, 255).uv(f15, f16).endVertex();
        bufferBuilder.vertex(matrix4f, size, -y, size).color(r, g, b, 255).uv(f13, f16).endVertex();
        bufferBuilder.vertex(matrix4f, size, -y, -size).color(r, g, b, 255).uv(f13, f14).endVertex();
        bufferBuilder.vertex(matrix4f, -size, -y, -size).color(r, g, b, 255).uv(f15, f14).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
        RenderSystem.disableTexture();

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void drawSunWithLight(ResourceLocation texture, Vec3 sunColor, Vec3 lightColor, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float y, boolean blend) {
        drawPlanet(texture, sunColor, bufferBuilder, matrix4f, size, y, blend);
        drawPlanet(SkyHelper.PLANET_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, true);
    }

    public static void drawPlanetWithLight(ResourceLocation texture, Vec3 lightColor, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float y, boolean blend) {
        drawPlanet(texture, new Vec3(255, 255, 255), bufferBuilder, matrix4f, size, y, blend);
        drawPlanet(SkyHelper.PLANET_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, true);
    }

    public static void drawPlanet(ResourceLocation texture, Vec3 color, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float y, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        int r = (int) color.x();
        int g = (int) color.y();
        int b = (int) color.z();

        RenderSystem.enableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderTexture(0, texture);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferBuilder.vertex(matrix4f, -size, y, -size).color(r, g, b, 255).uv(1.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, size, y, -size).color(r, g, b, 255).uv(0.0F, 0.0F).endVertex();
        bufferBuilder.vertex(matrix4f, size, y, size).color(r, g, b, 255).uv(0.0F, 1.0F).endVertex();
        bufferBuilder.vertex(matrix4f, -size, y, size).color(r, g, b, 255).uv(1.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
        RenderSystem.disableTexture();

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void drawSunWithLightAndSatellites(Minecraft mc, ResourceLocation texture, Vec3 color, Vec3 lightColor, Vec3 satelliteColor, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float satelliteSize, int satellites, float satelliteSpeed, float xAngle, float yAngle, float zAngle, float y, boolean blend, boolean satelliteBlend) {
        drawPlanetWithSatellites(mc, texture, color, satelliteColor, bufferBuilder, matrix4f, size, satelliteSize, satellites, satelliteSpeed, xAngle, yAngle, zAngle, y, blend, satelliteBlend);
        drawPlanet(SkyHelper.PLANET_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, true);
    }

    public static void drawPlanetWithLightAndSatellites(Minecraft mc, ResourceLocation texture, Vec3 lightColor, Vec3 satelliteColor, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float lightSize, float satelliteSize, int satellites, float satelliteSpeed, float xAngle, float yAngle, float zAngle, float y, boolean blend, boolean satelliteBlend) {
        drawPlanetWithSatellites(mc, texture, new Vec3(255, 255, 255), satelliteColor, bufferBuilder, matrix4f, size, satelliteSize, satellites, satelliteSpeed, xAngle, yAngle, zAngle, y, blend, satelliteBlend);
        drawPlanet(SkyHelper.PLANET_LIGHT, lightColor, bufferBuilder, matrix4f, lightSize, y, true);
    }

    public static void drawPlanetWithSatellites(Minecraft mc, ResourceLocation texture, Vec3 color, Vec3 satelliteColor, BufferBuilder bufferBuilder, Matrix4f matrix4f, float size, float satelliteSize, int satellites, float satelliteSpeed, float xAngle, float yAngle, float zAngle, float y, boolean blend, boolean satelliteBlend) {
        float gameTick = (mc.level.getGameTime() + mc.getPartialTick()) / satelliteSpeed;
        float distance = 6.3F / satellites;

        for (int i = 0; i < satellites; i++) {
            float sinTick = (float) Math.sin(gameTick + (distance * i));
            float cosTick = (float) Math.cos(gameTick + (distance * i));
            float yPos = y + cosTick * yAngle;

            if (y < yPos) {
                SkyHelper.drawSatellites(satelliteColor, bufferBuilder, matrix4f, sinTick, cosTick, xAngle, yAngle, zAngle, satelliteSize, y, satelliteBlend);
            }
        }

        SkyHelper.drawPlanet(texture, color, bufferBuilder, matrix4f, size, y, blend);

        for (int i = 0; i < satellites; i++) {
            float sinTick = (float) Math.sin(gameTick + (distance * i));
            float cosTick = (float) Math.cos(gameTick + (distance * i));

            float yPos = y + cosTick * yAngle;

            if (y > yPos) {
                SkyHelper.drawSatellites(satelliteColor, bufferBuilder, matrix4f, sinTick, cosTick, xAngle, yAngle, zAngle, satelliteSize, y, satelliteBlend);
            }
        }
    }

    public static void drawSatellites(Vec3 color, BufferBuilder bufferBuilder, Matrix4f matrix4f, float sinTick, float cosTick, float xAngle, float yAngle, float zAngle, float size, float y, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }

        int r = (int) color.x();
        int g = (int) color.y();
        int b = (int) color.z();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(matrix4f, sinTick * xAngle - size, y + cosTick * yAngle, cosTick * zAngle - size).color(r, g, b, 255).endVertex();
        bufferBuilder.vertex(matrix4f, sinTick * xAngle + size, y + cosTick * yAngle, cosTick * zAngle - size).color(r, g, b, 255).endVertex();
        bufferBuilder.vertex(matrix4f, sinTick * xAngle + size, y + cosTick * yAngle, cosTick * zAngle + size).color(r, g, b, 255).endVertex();
        bufferBuilder.vertex(matrix4f, sinTick * xAngle - size, y + cosTick * yAngle, cosTick * zAngle + size).color(r, g, b, 255).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void setupSunRiseColor(PoseStack poseStack, BufferBuilder bufferBuilder, float partialTick, Minecraft mc, boolean blend) {
        if (blend) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
        }

        float[] afloat = mc.level.effects().getSunriseColor(mc.level.getTimeOfDay(partialTick), partialTick);
        if (afloat != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableTexture();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.pushPose();
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            float f3 = Mth.sin(mc.level.getSunAngle(partialTick)) < 0.0F ? 180.0F : 0.0F;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(f3));
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
            float f4 = afloat[0];
            float f5 = afloat[1];
            float f6 = afloat[2];
            Matrix4f matrix4f = poseStack.last().pose();
            bufferBuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            bufferBuilder.vertex(matrix4f, 0.0F, 100.0F, 0.0F).color(f4, f5, f6, afloat[3]).endVertex();
            int i = 16;

            for(int j = 0; j <= i; ++j) {
                float f7 = (float)j * ((float)Math.PI * 2F) / 16.0F;
                float f8 = Mth.sin(f7);
                float f9 = Mth.cos(f7);
                bufferBuilder.vertex(matrix4f, f8 * 120.0F, f9 * 120.0F, -f9 * 40.0F * afloat[3]).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
            }

            BufferUploader.drawWithShader(bufferBuilder.end());
            poseStack.popPose();
        }

        if (blend) {
            RenderSystem.disableBlend();
        }
    }

    public static void setupShaderColor(Minecraft mc, float r, float g, float b) {
        if (mc.level.effects().hasGround()) {
            RenderSystem.setShaderColor(r * 0.2F + 0.04F, g * 0.2F + 0.04F, b * 0.6F + 0.1F, 1.0F);
        } else {
            RenderSystem.setShaderColor(r, g, b, 1.0F);
        }
    }

    public static void drawSky(Minecraft mc, Matrix4f matrix4f, Matrix4f projectionMatrix, ShaderInstance shaderInstance) {
        mc.levelRenderer.skyBuffer.bind();
        mc.levelRenderer.skyBuffer.drawWithShader(matrix4f, projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
    }

    public static void drawDarkSky(Minecraft mc, PoseStack poseStack, Matrix4f projectionMatrix, ShaderInstance shaderInstance, float partialTick) {
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
        double d0 = mc.player.getEyePosition(partialTick).y - mc.level.getLevelData().getHorizonHeight(mc.level);
        if (d0 < 0.0D) {
            poseStack.pushPose();
            poseStack.translate(0.0D, 12.0D, 0.0D);
            mc.levelRenderer.darkBuffer.bind();
            mc.levelRenderer.darkBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderInstance);
            VertexBuffer.unbind();
            poseStack.popPose();
        }
    }

    public static void setupRainSize(float[] rainSizeX, float[] rainSizeZ) {
        for(int i = 0; i < 32; ++i) {
            for(int j = 0; j < 32; ++j) {
                float f9 = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = Mth.sqrt(f9 * f9 + f1 * f1);
                rainSizeX[i << 5 | j] = -f1 / f2;
                rainSizeZ[i << 5 | j] = f9 / f2;
            }
        }
    }

    public static Matrix4f setMatrixRot(PoseStack poseStack, Triple<Quaternion, Quaternion, Quaternion> quaternionTriple) {
        poseStack.pushPose();

        Quaternion left = quaternionTriple.getLeft();
        Quaternion middle = quaternionTriple.getMiddle();
        Quaternion right = quaternionTriple.getRight();

        if (left != null) {
            poseStack.mulPose(quaternionTriple.getLeft());
        }

        if (middle != null) {
            poseStack.mulPose(quaternionTriple.getMiddle());
        }

        if (right != null) {
            poseStack.mulPose(quaternionTriple.getRight());
        }

        Matrix4f matrix4f = poseStack.last().pose();
        poseStack.popPose();

        return matrix4f;
    }
}
