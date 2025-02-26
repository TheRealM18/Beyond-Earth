package net.mrscauthd.beyond_earth.client.registries;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.mrscauthd.beyond_earth.BeyondEarth;
import net.mrscauthd.beyond_earth.client.renderers.sky.EarthOrbitSky;
import net.mrscauthd.beyond_earth.client.renderers.sky.EarthSky;
import net.mrscauthd.beyond_earth.client.renderers.sky.MarsSky;
import net.mrscauthd.beyond_earth.client.renderers.sky.MoonSky;

@Mod.EventBusSubscriber(modid = BeyondEarth.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SkyRendererRegistry {

    @SubscribeEvent
    public static void register(RegisterDimensionSpecialEffectsEvent event) {
        event.register(new ResourceLocation("overworld"), new EarthSky(192.0F, true, DimensionSpecialEffects.SkyType.NORMAL, false, false));
        event.register(new ResourceLocation(BeyondEarth.MODID, "earth_orbit"), new EarthOrbitSky(0, true, DimensionSpecialEffects.SkyType.NONE, false, false));
        event.register(new ResourceLocation(BeyondEarth.MODID, "moon"), new MoonSky(0, true, DimensionSpecialEffects.SkyType.NONE, false, false));
        event.register(new ResourceLocation(BeyondEarth.MODID, "mars"), new MarsSky(0, true, DimensionSpecialEffects.SkyType.NONE, false, false));
    }
}
