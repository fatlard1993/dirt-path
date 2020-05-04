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
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.block.SlabBlock;

public class SpreadableSlab extends SlabBlock {
	public final Block baseBlock;
	public static final BooleanProperty SNOWY;

	public SpreadableSlab(Settings settings, Block baseBlock){
		super(settings);

		this.baseBlock = baseBlock;

		this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM)).with(SlabBlock.WATERLOGGED, false)).with(SNOWY, false));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random){
		this.baseBlock.randomDisplayTick(blockState, world, blockPos, random);
	}

	@Override
	public void scheduledTick(BlockState spreader, ServerWorld world, BlockPos pos, Random random){
		// if(!canSurvive(spreader, world, pos)) Main.setToDirt(world, pos);

		// else
		Main.spreadableTick(spreader, world, pos, random);
	}

	public static boolean canSurvive(BlockState state, WorldView view, BlockPos pos){
		BlockPos posUp = pos.up();
		BlockState topBlock = view.getBlockState(posUp);

		if((state.getBlock() == DirtSlabBlocks.GRASS_SLAB || state.getBlock() == DirtSlabBlocks.MYCELIUM_SLAB) && (state.get(TYPE) == SlabType.BOTTOM) && state.get(WATERLOGGED)) return false;

		else if(topBlock.getBlock() == Blocks.SNOW && (Integer)topBlock.get(SnowBlock.LAYERS) == 1) return true;

		else {
			int i = ChunkLightProvider.getRealisticOpacity(view, state, pos, topBlock, posUp, Direction.UP, topBlock.getOpacity(view, posUp));

			return i < view.getMaxLightLevel();
		}
	}

	public static boolean canSpread(BlockState state, WorldView view, BlockPos pos){
		return canSurvive(state, view, pos) && !view.getFluidState(pos.up()).matches(FluidTags.WATER);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos){
		if(facing == Direction.UP && !state.canPlaceAt(world, pos)) world.getBlockTickScheduler().schedule(pos, this, 1);

		if(facing != Direction.UP) return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);

		else {
			Block neighborBlock = neighborState.getBlock();

			return (BlockState)state.with(SNOWY, neighborBlock == Blocks.SNOW_BLOCK || neighborBlock == Blocks.SNOW);
		}
	}

	public BlockState getPlacementState(ItemPlacementContext ctx){
		Block topBlock = ctx.getWorld().getBlockState(ctx.getBlockPos().up()).getBlock();

		return (BlockState)(!this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? pushEntitiesUpBeforeBlockChange(this.getDefaultState(), DirtSlabBlocks.DIRT_SLAB.getDefaultState(), ctx.getWorld(), ctx.getBlockPos()) : super.getPlacementState(ctx)).with(SNOWY, topBlock == Blocks.SNOW_BLOCK || topBlock == Blocks.SNOW);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder){ builder.add(SNOWY); }

	static {
		SNOWY = Properties.SNOWY;
	}
}