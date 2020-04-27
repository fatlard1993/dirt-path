package justfatlard.dirt_slab;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.block.SlabBlock;

public class SpreadableSlab extends SlabBlock {
	public final Block baseBlock;

	public SpreadableSlab(Settings settings, Block baseBlock){
		super(settings);

		this.baseBlock = baseBlock;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random){
		this.baseBlock.randomDisplayTick(blockState, world, blockPos, random);
	}

	@Override
	public void scheduledTick(BlockState spreader, ServerWorld world, BlockPos pos, Random random){
		Main.spreadableTick(spreader, world, pos, random);
	}

	private static boolean canSurvive(BlockState state, WorldView worldView, BlockPos pos){
		BlockPos blockPos = pos.up();
		BlockState topBlock = worldView.getBlockState(blockPos);

		// if((state.getBlock() == DirtSlabBlocks.GRASS_SLAB || state.getBlock() == DirtSlabBlocks.MYCELIUM_SLAB) && state.get(WATERLOGGED) == true && state.get(TYPE) == SlabType.BOTTOM) return false;

		// else
		if(topBlock.getBlock() == Blocks.SNOW && (Integer)topBlock.get(SnowBlock.LAYERS) == 1) return true;

		else {
			int i = ChunkLightProvider.getRealisticOpacity(worldView, state, pos, topBlock, blockPos, Direction.UP, topBlock.getOpacity(worldView, blockPos));

			return i < worldView.getMaxLightLevel();
		}
	}

	public static boolean canSpread(BlockState state, WorldView worldView, BlockPos pos){
		return canSurvive(state, worldView, pos) && !worldView.getFluidState(pos.up()).matches(FluidTags.WATER);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos){
		if(facing == Direction.UP && !state.canPlaceAt(world, pos)) world.getBlockTickScheduler().schedule(pos, this, 1);

		return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx){
		return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? Block.pushEntitiesUpBeforeBlockChange(this.getDefaultState(), DirtSlabBlocks.DIRT_SLAB.getDefaultState(), ctx.getWorld(), ctx.getBlockPos()) : super.getPlacementState(ctx);
	}
}