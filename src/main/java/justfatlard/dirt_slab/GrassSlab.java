package justfatlard.dirt_slab;

import java.util.Random;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowerFeature;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;

public class GrassSlab extends SpreadableSlab implements Fertilizable {
	public GrassSlab(Settings settings){
		super(settings, Blocks.GRASS_BLOCK);
	}

	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient){
		return world.getBlockState(pos.up()).isAir();
	}

	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state){
		return true;
	}

	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state){
		growAll(world, random, pos, state);
	}

	public static void growAll(ServerWorld world, Random random, BlockPos pos, BlockState state){
		BlockPos blockPos = pos.up();
		BlockState blockState = Blocks.GRASS.getDefaultState();

		label48:
		for(int i = 0; i < 128; ++i){
			BlockPos blockPos2 = blockPos;

			for(int j = 0; j < i / 16; ++j){
				blockPos2 = blockPos2.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				Block groundBlock = world.getBlockState(blockPos2.down()).getBlock();

				if((groundBlock != DirtSlabBlocks.GRASS_SLAB && groundBlock != Blocks.GRASS_BLOCK) || world.getBlockState(blockPos2).isFullCube(world, blockPos2)) continue label48;
			}

			BlockState blockState2 = world.getBlockState(blockPos2);

			if(blockState2.getBlock() == blockState.getBlock() && random.nextInt(10) == 0) ((Fertilizable)blockState.getBlock()).grow(world, random, blockPos2, blockState2);

			if(blockState2.isAir()){
				BlockState blockState4;
				if(random.nextInt(8) == 0){
					List<ConfiguredFeature<?, ?>> list = world.getBiome(blockPos2).getFlowerFeatures();

					if(list.isEmpty()) continue;

					ConfiguredFeature<?, ?> configuredFeature = ((DecoratedFeatureConfig)((ConfiguredFeature)list.get(0)).config).feature;
					blockState4 = ((FlowerFeature)configuredFeature.feature).getFlowerToPlace(random, blockPos2, configuredFeature.config);
				}

				else blockState4 = blockState;

				if(blockState4.canPlaceAt(world, blockPos2)) world.setBlockState(blockPos2, blockState4, 3);
			}
		}
	}
}