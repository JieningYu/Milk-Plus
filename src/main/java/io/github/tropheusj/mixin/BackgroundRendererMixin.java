package io.github.tropheusj.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.tropheusj.MilkPlus;
import io.github.tropheusj.milk.Milk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.BackgroundRenderer.FogType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
	@Shadow
	private static float red;
	
	@Shadow
	private static float green;
	
	@Shadow
	private static float blue;
	
	@ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;clearColor(FFFF)V", remap = false))
	private static void milk_plus$modifyFogColors(Args args, Camera camera, float partialTicks, ClientWorld level, int renderDistanceChunks, float bossColorModifier) {
		FluidState state = level.getFluidState(camera.getBlockPos());
		if (state.isOf(Milk.STILL_MILK) || state.isOf(Milk.FLOWING_MILK)) {
			red = 1;
			green = 1;
			blue = 1;
		}
	}
	
	@Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
	private static void milk_plus$setupFog(Camera camera, FogType fogType, float f, boolean bl, CallbackInfo ci) {
		FluidState state = MinecraftClient.getInstance().world.getFluidState(camera.getBlockPos());
		if (state.isOf(Milk.STILL_MILK) || state.isOf(Milk.FLOWING_MILK)) {
			RenderSystem.setShaderFogStart(-8);
			RenderSystem.setShaderFogEnd(5);
			ci.cancel();
		}
	}
}
