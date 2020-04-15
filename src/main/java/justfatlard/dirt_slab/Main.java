package justfatlard.dirt_slab;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	public static final Block DIRT_SLAB;
	public static final Block COARSE_DIRT_SLAB;
	public static final Block FARMLAND_SLAB;
	public static final Block GRASS_SLAB;
	public static final Block GRASS_PATH_SLAB;

	public static final Item DIRT_SLAB_ITEM;
	public static final Item COARSE_DIRT_SLAB_ITEM;
	public static final Item FARMLAND_SLAB_ITEM;
	// public static final Item GRASS_SLAB_ITEM;
	public static final Item GRASS_PATH_SLAB_ITEM;

	private static Block registerBlock(String id, Block block) {
		return (Block)Registry.register(Registry.BLOCK, new Identifier("dirt-slab-justfatlard", (String)id), block);
	}

	private static Item registerItem(String id, Item item) {
		return (Item)Registry.register(Registry.ITEM, new Identifier("dirt-slab-justfatlard", (String)id), item);
	}

	@Override
	public void onInitialize(){
		System.out.println("Loaded dirt-slab");
	}

	static {
		DIRT_SLAB = registerBlock("dirt_slab", new SlabBlock(FabricBlockSettings.of(Material.EARTH).sounds(BlockSoundGroup.GRAVEL).strength(0.5F, 0.5F).build()));
		COARSE_DIRT_SLAB = registerBlock("coarse_dirt_slab", new SlabBlock(FabricBlockSettings.of(Material.EARTH).sounds(BlockSoundGroup.GRAVEL).strength(0.5F, 0.5F).build()));
		FARMLAND_SLAB = registerBlock("farmland_slab", new SlabBlock(FabricBlockSettings.of(Material.EARTH).sounds(BlockSoundGroup.GRAVEL).ticksRandomly().strength(0.5F, 0.5F).build()));
		GRASS_SLAB = registerBlock("grass_slab", new GrassSlab(FabricBlockSettings.of(Material.ORGANIC).sounds(BlockSoundGroup.GRASS).ticksRandomly().strength(0.6F, 0.6F).build()));
		GRASS_PATH_SLAB = registerBlock("grass_path_slab", new GrassSlab(FabricBlockSettings.of(Material.EARTH).sounds(BlockSoundGroup.GRASS).strength(0.65F, 0.65F).build()));

		DIRT_SLAB_ITEM = registerItem("dirt_slab", new BlockItem(DIRT_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		COARSE_DIRT_SLAB_ITEM = registerItem("coarse_dirt_slab", new BlockItem(COARSE_DIRT_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		FARMLAND_SLAB_ITEM = registerItem("farmland_slab", new BlockItem(FARMLAND_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		// GRASS_SLAB_ITEM = registerItem("grass_slab", new BlockItem(GRASS_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
		GRASS_PATH_SLAB_ITEM = registerItem("grass_path_slab", new BlockItem(GRASS_PATH_SLAB, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
	}
}
