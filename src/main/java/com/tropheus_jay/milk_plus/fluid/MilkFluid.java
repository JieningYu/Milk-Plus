package com.tropheus_jay.milk_plus.fluid;

import io.github.tropheusj.dripstone_fluid_lib.DripstoneInteractingFluid;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static com.tropheus_jay.milk_plus.MilkPlus.*;
import static net.minecraft.item.Items.MILK_BUCKET;

public abstract class MilkFluid extends AbstractFluid implements DripstoneInteractingFluid {
	@Override
	public Fluid getStill() {
		return STILL_MILK;
	}
	
	@Override
	public Fluid getFlowing() {
		return FLOWING_MILK;
	}
	
	@Override
	public Item getBucketItem() {
		return Registry.ITEM.get(Registry.ITEM.getRawId(MILK_BUCKET));
	}

	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 2;
	}
	
	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return MILK.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}
	
	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}
	
	@Override
	public int getParticleColor(World world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
		return 0xFFFFFF;
	}
	
	@Override
	public boolean growsDripstone(BlockState state) {
		return true;
	}
	
	@Override
	public int getFluidDripWorldEvent(BlockState state, World world, BlockPos cauldronPos) {
		return WorldEvents.POINTED_DRIPSTONE_DRIPS_WATER_INTO_CAULDRON;
	}
	
	@Override
	public @Nullable BlockState getCauldronBlockState(BlockState state, World world, BlockPos cauldronPos) {
		return MILK_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 1);
	}
	
	@Override
	public float getFluidDripChance(BlockState state, World world, BlockPos pos) {
		return WATER_DRIP_CHANCE;
	}
	
	public static class Flowing extends MilkFluid {
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}
	}
	
	public static class Still extends MilkFluid {
		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}
	}
}
