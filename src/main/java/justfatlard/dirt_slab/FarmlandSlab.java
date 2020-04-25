package justfatlard.dirt_slab;

import java.util.Iterator;
import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.enums.SlabType;

public class FarmlandSlab extends SlabBlock {
	protected static final VoxelShape TOP_SHAPE;
	protected static final VoxelShape BOTTOM_SHAPE;
	protected static final VoxelShape DOUBLE_SHAPE;
	public static final IntProperty MOISTURE;

	protected FarmlandSlab(Block.Settings settings) {
		super(settings);

		this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(MOISTURE, 0));
	}

	@Override
	public boolean hasSidedTransparency(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
		SlabType slabType = (SlabType)state.get(TYPE);

		switch(slabType) {
			case DOUBLE:
				return DOUBLE_SHAPE;
			case TOP:
				return TOP_SHAPE;
			default:
				return BOTTOM_SHAPE;
		}
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (facing == Direction.UP && !state.canPlaceAt(world, pos)) {
			world.getBlockTickScheduler().schedule(pos, this, 1);
		}

		return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.up());

		return !blockState.getMaterial().isSolid() || blockState.getBlock() instanceof FenceGateBlock || blockState.getBlock() instanceof PistonExtensionBlock;
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return !this.getDefaultState().canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? Main.DIRT_SLAB.getDefaultState() : super.getPlacementState(ctx);
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			setToDirt(state, world, pos);
		}

		else {
			int i = (Integer)state.get(MOISTURE);
			if (!isWaterNearby(world, pos) && !world.hasRain(pos.up())) {
				if (i > 0) {
					world.setBlockState(pos, (BlockState)state.with(MOISTURE, i - 1), 2);
				} else if (!hasCrop(world, pos)) {
					setToDirt(state, world, pos);
				}
			} else if (i < 7) {
				world.setBlockState(pos, (BlockState)state.with(MOISTURE, 7), 2);
			}
		}
	}

	public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance) {
		if (!world.isClient && world.random.nextFloat() < distance - 0.5F && entity instanceof LivingEntity && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F) {
			setToDirt(world.getBlockState(pos), world, pos);
		}

		super.onLandedUpon(world, pos, entity, distance);
	}

	public static void setToDirt(BlockState state, World world, BlockPos pos) {
		world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, Main.DIRT_SLAB.getDefaultState(), world, pos));
	}

	private static boolean hasCrop(BlockView world, BlockPos pos) {
		Block block = world.getBlockState(pos.up()).getBlock();

		return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
	}

	private static boolean isWaterNearby(WorldView worldView, BlockPos pos) {
		Iterator var2 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();

		BlockPos blockPos;
		do {
			if (!var2.hasNext()) {
				return false;
			}

			blockPos = (BlockPos)var2.next();
		} while(!worldView.getFluidState(blockPos).matches(FluidTags.WATER));

		return true;
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(MOISTURE);
	}

	public boolean canPlaceAtSide(BlockState world, BlockView view, BlockPos pos, BlockPlacementEnvironment env) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public boolean hasInWallOverlay(BlockState state, BlockView view, BlockPos pos) {
		return true;
	}

	static {
		MOISTURE = Properties.MOISTURE;
		TOP_SHAPE = Block.createCuboidShape(0.0D, 7.0D, 0.0D, 16.0D, 15.0D, 16.0D);
		BOTTOM_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D);
		DOUBLE_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	}
}