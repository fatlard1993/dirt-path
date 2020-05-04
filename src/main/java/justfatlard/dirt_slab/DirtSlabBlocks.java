package justfatlard.dirt_slab;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DirtSlabBlocks {
	public static final String MOD_ID = "dirt-slab-justfatlard";

	public static final Block COARSE_DIRT_SLAB = new SlabBlock(FabricBlockSettings.copy(Blocks.COARSE_DIRT).breakByTool(FabricToolTags.SHOVELS).build());
	public static final Block DIRT_SLAB = new SlabBlock(FabricBlockSettings.copy(Blocks.DIRT).breakByTool(FabricToolTags.SHOVELS).build());
	public static final Block FARMLAND_SLAB = new FarmlandSlab(FabricBlockSettings.copy(Blocks.FARMLAND).breakByTool(FabricToolTags.SHOVELS).build());
	public static final Block GRASS_PATH_SLAB = new SlicedTopSlab(FabricBlockSettings.copy(Blocks.GRASS_PATH).breakByTool(FabricToolTags.SHOVELS).build());
	public static final Block GRASS_SLAB = new GrassSlab(FabricBlockSettings.copy(Blocks.GRASS_BLOCK).breakByTool(FabricToolTags.SHOVELS).ticksRandomly().build());
	public static final Block MYCELIUM_SLAB = new SpreadableSlab(FabricBlockSettings.copy(Blocks.MYCELIUM).breakByTool(FabricToolTags.SHOVELS).build(), Blocks.MYCELIUM);
	public static final Block PODZOL_SLAB = new SlabBlock(FabricBlockSettings.copy(Blocks.PODZOL).breakByTool(FabricToolTags.SHOVELS).build());

	private static void registerSlab(String name, Block block){
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

		Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
	}

	public static void register(){
		registerSlab("coarse_dirt_slab", COARSE_DIRT_SLAB);
		registerSlab("dirt_slab", DIRT_SLAB);
		registerSlab("farmland_slab", FARMLAND_SLAB);
		registerSlab("grass_path_slab", GRASS_PATH_SLAB);
		registerSlab("grass_slab", GRASS_SLAB);
		registerSlab("mycelium_slab", MYCELIUM_SLAB);
		registerSlab("podzol_slab", PODZOL_SLAB);
	}
}
