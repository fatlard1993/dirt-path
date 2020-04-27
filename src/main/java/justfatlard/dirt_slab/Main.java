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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemStack;
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
				return view != null && pos != null ? BiomeColors.getGrassColor(view, pos) : GrassColors.getColor(0.5D, 1.0D);
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

	public static void setToDirt(BlockState state, World world, BlockPos pos){
		world.setBlockState(pos, Block.pushEntitiesUpBeforeBlockChange(state, DirtSlabBlocks.DIRT_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)), world, pos));
	}

	public static void spreadableTick(BlockState spreader, ServerWorld world, BlockPos pos, Random random) {
		if(world.getLightLevel(pos.up()) >= 9){
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
