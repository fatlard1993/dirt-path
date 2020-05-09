package justfatlard.dirt_slab.mixins;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.SlicedTopSlab;

@Mixin(ShovelItem.class)
public class ShovelMixin {
	@Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
	private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info){
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);

		if(context.getSide() != Direction.DOWN && SlicedTopSlab.canExistAt(state, world, pos)){
			PlayerEntity player = context.getPlayer();
			Block block = state.getBlock();
			Boolean success = false;
			BlockState newState = Blocks.GREEN_WOOL.getDefaultState();
			SlabType slabType = block instanceof SlabBlock ? (SlabType)state.get(SlabBlock.TYPE) : SlabType.DOUBLE;

			if(block == Blocks.DIRT){
				newState = Blocks.GRASS_PATH.getDefaultState();

				success = true;
			}

			else if(block == DirtSlabBlocks.GRASS_SLAB || block == DirtSlabBlocks.DIRT_SLAB){ // grass/dirt slab to path slab
				newState = DirtSlabBlocks.GRASS_PATH_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

				success = true;
			}

			// doubles to singles (no grass/dirt due to above logic)
			else if(block == Blocks.COARSE_DIRT || (block == DirtSlabBlocks.COARSE_DIRT_SLAB && slabType == SlabType.DOUBLE)){
				newState = DirtSlabBlocks.COARSE_DIRT_SLAB.getDefaultState();

				success = true;
			}

			else if(block == Blocks.FARMLAND || (block == DirtSlabBlocks.FARMLAND_SLAB && slabType == SlabType.DOUBLE)){
				newState = DirtSlabBlocks.FARMLAND_SLAB.getDefaultState();

				success = true;
			}

			else if(block == Blocks.GRASS_PATH || (block == DirtSlabBlocks.GRASS_PATH_SLAB && slabType == SlabType.DOUBLE)){
				newState = DirtSlabBlocks.GRASS_PATH_SLAB.getDefaultState();

				success = true;
			}

			else if(block == Blocks.MYCELIUM || (block == DirtSlabBlocks.MYCELIUM_SLAB && slabType == SlabType.DOUBLE)){
				newState = DirtSlabBlocks.MYCELIUM_SLAB.getDefaultState();

				success = true;
			}

			else if(block == Blocks.PODZOL || (block == DirtSlabBlocks.PODZOL_SLAB && slabType == SlabType.DOUBLE)){
				newState = DirtSlabBlocks.PODZOL_SLAB.getDefaultState();

				success = true;
			}

			// single swaps (no grass/dirt due to above logic)
			// else if(block == DirtSlabBlocks.COARSE_DIRT_SLAB && slabType == SlabType.BOTTOM || slabType == SlabType.TOP){
			// 	newState = DirtSlabBlocks.COARSE_DIRT_SLAB.getDefaultState().with(SlabBlock.TYPE, slabType == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

			// 	success = true;
			// }

			// else if(block == DirtSlabBlocks.FARMLAND_SLAB && slabType == SlabType.BOTTOM || slabType == SlabType.TOP){
			// 	newState = DirtSlabBlocks.FARMLAND_SLAB.getDefaultState().with(SlabBlock.TYPE, slabType == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

			// 	success = true;
			// }

			// else if(block == DirtSlabBlocks.GRASS_PATH_SLAB && slabType == SlabType.BOTTOM || slabType == SlabType.TOP){
			// 	newState = DirtSlabBlocks.GRASS_PATH_SLAB.getDefaultState().with(SlabBlock.TYPE, slabType == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

			// 	success = true;
			// }

			// else if(block == DirtSlabBlocks.MYCELIUM_SLAB && slabType == SlabType.BOTTOM || slabType == SlabType.TOP){
			// 	newState = DirtSlabBlocks.MYCELIUM_SLAB.getDefaultState().with(SlabBlock.TYPE, slabType == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

			// 	success = true;
			// }

			// else if(block == DirtSlabBlocks.PODZOL_SLAB && slabType == SlabType.BOTTOM || slabType == SlabType.TOP){
			// 	newState = DirtSlabBlocks.PODZOL_SLAB.getDefaultState().with(SlabBlock.TYPE, slabType == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

			// 	success = true;
			// }

			if(success){
				if(!world.isClient){
					world.setBlockState(pos, newState);

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}
}
