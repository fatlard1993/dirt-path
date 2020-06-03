package justfatlard.dirt_slab;

import java.util.Random;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class Main implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "dirt-slab-justfatlard";

	@Override
	public void onInitialize(){
		DirtSlabBlocks.register();

		System.out.println("Loaded dirt-slab");
	}

	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.BLOCK.register(new BlockColorProvider(){
			@Override
			public int getColor(BlockState state, BlockRenderView view, BlockPos pos, int tintIndex){
				if(tintIndex == 1) return view != null && pos != null ? BiomeColors.getGrassColor(view, pos) : GrassColors.getColor(0.5D, 1.0D);

				return -1;
			}
		}, DirtSlabBlocks.GRASS_SLAB);

		ColorProviderRegistry.ITEM.register(new ItemColorProvider(){
			@Override
			public int getColor(ItemStack stack, int tintIndex){
				return GrassColors.getColor(0.5D, 1.0D);
			}
		}, DirtSlabBlocks.GRASS_SLAB);

		BlockRenderLayerMap.INSTANCE.putBlock(DirtSlabBlocks.GRASS_SLAB, RenderLayer.getCutoutMipped());
	}

	public static boolean hasTopSlab(BlockState state){
		Block block = state.getBlock();

		return (block instanceof SlabBlock) && (state.get(SlabBlock.TYPE) == SlabType.TOP || state.get(SlabBlock.TYPE) == SlabType.DOUBLE);
	}

	public static boolean isDirtType(Block block){
		return block == DirtSlabBlocks.COARSE_DIRT_SLAB || block == DirtSlabBlocks.DIRT_SLAB || block == DirtSlabBlocks.FARMLAND_SLAB || block == DirtSlabBlocks.PODZOL_SLAB;
	}

	public static boolean isGrassType(Block block){
		return block == DirtSlabBlocks.GRASS_SLAB || block == DirtSlabBlocks.MYCELIUM_SLAB || isDirtType(block);
	}

	public static void dirtParticles(World world, BlockPos pos, int count){
		if(!world.isClient) ((ServerWorld) world).spawnParticles(ParticleTypes.MYCELIUM, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.nextInt(world.random, 1, count), 0.25, 0.02, 0.25, 0.1);
	}

	public static void waterParticles(World world, BlockPos pos, int count){
		if(!world.isClient) ((ServerWorld) world).spawnParticles(ParticleTypes.SPLASH, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, MathHelper.nextInt(world.random, 1, count), 0.25, 0.02, 0.25, 0.1);
	}

	public static void happyParticles(World world, BlockPos pos, int count){
		if(!world.isClient) ((ServerWorld) world).spawnParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, MathHelper.nextInt(world.random, 1, count), 0.25, 0.02, 0.25, 0.1);
	}

	public static void sadParticles(World world, BlockPos pos, int count){
		if(!world.isClient) ((ServerWorld) world).spawnParticles(ParticleTypes.ANGRY_VILLAGER, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, MathHelper.nextInt(world.random, 1, count), 0.25, 0.02, 0.25, 0.1);
	}

	public static void setToDirt(World world, BlockPos pos){
		BlockState state = world.getBlockState(pos);

		if(state.getBlock() instanceof SlabBlock)	world.setBlockState(pos, DirtSlabBlocks.DIRT_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)));

		else world.setBlockState(pos, Blocks.DIRT.getDefaultState());

		dirtParticles(world, pos, 3);
	}

	public static void spreadableTick(BlockState spreader, ServerWorld world, BlockPos pos, Random random){
		if(SpreadableSlab.canSurvive(spreader, world, pos) && world.getLightLevel(pos.up()) >= 9){
			for(int x = 0; x < 4; ++x){
				BlockPos randBlockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
				BlockState spreadee = world.getBlockState(randBlockPos);

				if(SpreadableSlab.canSpread(spreader, world, randBlockPos)){
					if(spreader.getBlock() == DirtSlabBlocks.GRASS_SLAB && spreadee.getBlock() == Blocks.DIRT){
						world.setBlockState(randBlockPos, Blocks.GRASS_BLOCK.getDefaultState());
					}

					else if(spreader.getBlock() == DirtSlabBlocks.MYCELIUM_SLAB && spreadee.getBlock() == Blocks.DIRT){
						world.setBlockState(randBlockPos, Blocks.MYCELIUM.getDefaultState());
					}

					else if((spreader.getBlock() == Blocks.GRASS_BLOCK || spreader.getBlock() == DirtSlabBlocks.GRASS_SLAB) && spreadee.getBlock() == DirtSlabBlocks.DIRT_SLAB){
						world.setBlockState(randBlockPos, DirtSlabBlocks.GRASS_SLAB.getDefaultState().with(SlabBlock.TYPE, spreadee.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, spreadee.get(SlabBlock.WATERLOGGED)));
					}

					else if((spreader.getBlock() == Blocks.MYCELIUM || spreader.getBlock() == DirtSlabBlocks.MYCELIUM_SLAB) && spreadee.getBlock() == DirtSlabBlocks.DIRT_SLAB){
						world.setBlockState(randBlockPos, DirtSlabBlocks.MYCELIUM_SLAB.getDefaultState().with(SlabBlock.TYPE, spreadee.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, spreadee.get(SlabBlock.WATERLOGGED)));
					}
				}
			}
		}
	}
}
