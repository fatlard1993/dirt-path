package justfatlard.dirt_slab;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.block.SlabBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WorldView;

public class SlicedTopSlab extends SlabBlock {
	protected static final VoxelShape TOP_SHAPE;
	protected static final VoxelShape BOTTOM_SHAPE;
	protected static final VoxelShape DOUBLE_SHAPE;

	protected SlicedTopSlab(Block.Settings settings){
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	public boolean hasInWallOverlay(BlockState state, BlockView view, BlockPos pos){
		return true;
	}

	@Override
	public boolean hasSidedTransparency(BlockState state){
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context){
		SlabType slabType = (SlabType)state.get(TYPE);

		switch(slabType){
			case DOUBLE:
				return DOUBLE_SHAPE;
			case TOP:
				return TOP_SHAPE;
			default:
				return BOTTOM_SHAPE;
		}
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos){
		return canExistAt(state, world, pos);
	}

	public static boolean canExistAt(BlockState state, WorldView world, BlockPos pos){
		BlockState upState = world.getBlockState(pos.up());

		return ((state.getBlock() instanceof SlabBlock && state.get(TYPE) == SlabType.BOTTOM) || (upState.getBlock() instanceof SlabBlock && upState.get(TYPE) == SlabType.TOP) || !upState.getMaterial().isSolid() || upState.getBlock() instanceof FenceGateBlock || upState.getBlock() instanceof PistonExtensionBlock);
	}

	public boolean canPlaceAtSide(BlockState world, BlockView view, BlockPos pos, BlockPlacementEnvironment env){ return false; }

	public BlockState getPlacementState(ItemPlacementContext ctx){
		return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? pushEntitiesUpBeforeBlockChange(this.getDefaultState(), DirtSlabBlocks.DIRT_SLAB.getDefaultState(), ctx.getWorld(), ctx.getBlockPos()) : super.getPlacementState(ctx);
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos){
		if(facing == Direction.UP && !state.canPlaceAt(world, pos)) world.getBlockTickScheduler().schedule(pos, this, 1);

		return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random){ Main.setToDirt(world, pos); }

	static {
		TOP_SHAPE = Block.createCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);
		BOTTOM_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
		DOUBLE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	}
}