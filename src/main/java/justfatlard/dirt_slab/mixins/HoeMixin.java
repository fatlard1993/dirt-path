package justfatlard.dirt_slab.mixins;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.SlicedTopSlab;

@Mixin(HoeItem.class)
public class HoeMixin {
	@Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
	private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info){
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);

		if(context.getSide() != Direction.DOWN && SlicedTopSlab.canExistAt(state, world, pos)){
			if(state.getBlock() == DirtSlabBlocks.COARSE_DIRT_SLAB){ // Coarse dirt slab to dirt slab
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, DirtSlabBlocks.DIRT_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)));

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else 	world.playSound(player, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}

			else if(state.getBlock() == DirtSlabBlocks.DIRT_SLAB || state.getBlock() == DirtSlabBlocks.GRASS_SLAB || state.getBlock() == DirtSlabBlocks.GRASS_PATH_SLAB){
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, DirtSlabBlocks.FARMLAND_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)));

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}
}
