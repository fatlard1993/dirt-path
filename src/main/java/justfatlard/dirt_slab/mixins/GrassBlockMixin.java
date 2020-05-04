package justfatlard.dirt_slab.mixins;

import java.util.Random;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;
import net.minecraft.block.Fertilizable;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.GrassSlab;

@Mixin(GrassBlock.class)
public class GrassBlockMixin {
	@Inject(at = @At("TAIL"), method = "grow", cancellable = true)
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo info){
		GrassSlab.growAll(world, random, pos, state);
	}
}
