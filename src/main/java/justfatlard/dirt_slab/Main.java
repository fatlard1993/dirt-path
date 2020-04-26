package justfatlard.dirt_slab;

import java.util.Random;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.world.BlockRenderView;

public class Main implements ModInitializer, ClientModInitializer {
	public static final String MOD_ID = "dirt-slab-justfatlard";

	public static final Block DIRT_SLAB;
	public static final Block COARSE_DIRT_SLAB;
	public static final Block FARMLAND_SLAB;
	public static final Block GRASS_SLAB;
	public static final Block GRASS_PATH_SLAB;
	public static final Block MYCELIUM_SLAB;
	public static final Block PODZOL_SLAB;

	private static Block registerSlab(String name, Block block){
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		return (Block)Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
	}

	@Override
	public void onInitialize(){
		System.out.println("Loaded dirt-slab");
	}

	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.BLOCK.register(new BlockColorProvider(){
			@Override
			public int getColor(BlockState state, BlockRenderView world, BlockPos pos, int layer){
				return world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D);
			}
		}, GRASS_SLAB);

		ColorProviderRegistry.ITEM.register(new ItemColorProvider(){
			@Override
			public int getColor(ItemStack stack, int layer){
				return GrassColors.getColor(0.5D, 1.0D);
			}
		}, GRASS_SLAB);
	}

	public static void spreadableTick(BlockState spreader, ServerWorld world, BlockPos pos, Random random){
		if(world.getLightLevel(pos.up()) >= 9){
			for(int x = 0; x < 4; ++x){
				BlockPos randBlockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
				BlockState spreadee = world.getBlockState(randBlockPos);

				if(SpreadableSlab.canSpread(spreader, world, randBlockPos)){
					if(spreader.getBlock() == Main.GRASS_SLAB && spreadee.getBlock() == Blocks.DIRT){
						world.setBlockState(randBlockPos, Blocks.GRASS_BLOCK.getDefaultState());
					}

					else if(spreader.getBlock() == Main.MYCELIUM_SLAB && spreadee.getBlock() == Blocks.DIRT){
						world.setBlockState(randBlockPos, Blocks.MYCELIUM.getDefaultState());
					}

					else if((spreader.getBlock() == Blocks.GRASS_BLOCK || spreader.getBlock() == Main.GRASS_SLAB) && spreadee.getBlock() == Main.DIRT_SLAB){
						world.setBlockState(randBlockPos, Main.GRASS_SLAB.getDefaultState().with(SlabBlock.TYPE, spreadee.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, spreadee.get(SlabBlock.WATERLOGGED)));
					}

					else if((spreader.getBlock() == Blocks.MYCELIUM || spreader.getBlock() == Main.MYCELIUM_SLAB) && spreadee.getBlock() == Main.DIRT_SLAB){
						world.setBlockState(randBlockPos, Main.MYCELIUM_SLAB.getDefaultState().with(SlabBlock.TYPE, spreadee.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, spreadee.get(SlabBlock.WATERLOGGED)));
					}
				}
			}
		}
	}

	static {
		COARSE_DIRT_SLAB = registerSlab("coarse_dirt_slab", new SlabBlock(FabricBlockSettings.copy(Blocks.COARSE_DIRT).breakByTool(FabricToolTags.SHOVELS).build()));
		DIRT_SLAB = registerSlab("dirt_slab", new SlabBlock(FabricBlockSettings.copy(Blocks.DIRT).breakByTool(FabricToolTags.SHOVELS).build()));
		FARMLAND_SLAB = registerSlab("farmland_slab", new FarmlandSlab(FabricBlockSettings.copy(Blocks.FARMLAND).breakByTool(FabricToolTags.SHOVELS).build()));
		GRASS_PATH_SLAB = registerSlab("grass_path_slab", new SlicedTopSlab(FabricBlockSettings.copy(Blocks.GRASS_PATH).breakByTool(FabricToolTags.SHOVELS).build()));
		GRASS_SLAB = registerSlab("grass_slab", new SpreadableSlab(FabricBlockSettings.copy(Blocks.DIRT).breakByTool(FabricToolTags.SHOVELS).build(), Blocks.GRASS_BLOCK));
		MYCELIUM_SLAB = registerSlab("mycelium_slab", new SpreadableSlab(FabricBlockSettings.copy(Blocks.MYCELIUM).breakByTool(FabricToolTags.SHOVELS).build(), Blocks.MYCELIUM));
		PODZOL_SLAB = registerSlab("podzol_slab", new SlabBlock(FabricBlockSettings.copy(Blocks.PODZOL).breakByTool(FabricToolTags.SHOVELS).build()));
	}
}
