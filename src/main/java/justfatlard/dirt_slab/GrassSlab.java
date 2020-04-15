package justfatlard.dirt_slab;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.block.SlabBlock;

public class GrassSlab extends SlabBlock {
	public GrassSlab(Block.Settings settings){
		super(settings);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
		if(!canSurvive(state, world, pos)){
			world.setBlockState(pos, Main.DIRT_SLAB.getDefaultState());
		}

		else {
			if(world.getLightLevel(pos.up()) >= 9){
				BlockState blockState = this.getDefaultState();

				for(int i = 0; i < 4; ++i){
					BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					if(world.getBlockState(blockPos).getBlock() == Blocks.DIRT && canSpread(blockState, world, blockPos)){
						world.setBlockState(blockPos, Blocks.GRASS_BLOCK.getDefaultState());
					}

					else if(world.getBlockState(blockPos).getBlock() == Main.DIRT_SLAB && canSpread(blockState, world, blockPos)){
						world.setBlockState(blockPos, Main.GRASS_SLAB.getDefaultState());
					}
				}
			}
		}
	}

	private static boolean canSurvive(BlockState state, WorldView worldView, BlockPos pos) {
		BlockPos blockPos = pos.up();
		BlockState blockState = worldView.getBlockState(blockPos);
		if (blockState.getBlock() == Blocks.SNOW && (Integer)blockState.get(SnowBlock.LAYERS) == 1) {
			return true;
		} else {
			int i = ChunkLightProvider.getRealisticOpacity(worldView, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(worldView, blockPos));
			return i < worldView.getMaxLightLevel();
		}
	}

	private static boolean canSpread(BlockState state, WorldView worldView, BlockPos pos) {
		BlockPos blockPos = pos.up();
		return canSurvive(state, worldView, pos) && !worldView.getFluidState(blockPos).matches(FluidTags.WATER);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (facing == Direction.UP && !state.canPlaceAt(world, pos)) {
			world.getBlockTickScheduler().schedule(pos, this, 1);
		}

		return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? Block.pushEntitiesUpBeforeBlockChange(this.getDefaultState(), Main.DIRT_SLAB.getDefaultState(), ctx.getWorld(), ctx.getBlockPos()) : super.getPlacementState(ctx);
	}
}