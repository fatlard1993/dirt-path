package justfatlard.dirt_slab.mixins;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

import justfatlard.dirt_slab.Main;

@Mixin(ShovelItem.class)
public class ShovelMixin {
	@Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
	private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info){
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();

		if(context.getSide() != Direction.DOWN && world.getBlockState(pos.up()).isAir()){
			BlockState state = world.getBlockState(pos);

			if(state.getBlock() == Blocks.DIRT){ // dirt to path
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, Blocks.GRASS_PATH.getDefaultState());

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}

			else if(state.getBlock() == Main.GRASS_SLAB || state.getBlock() == Main.DIRT_SLAB){ // grass/dirt slab to path slab
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, Main.GRASS_PATH_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)));

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}

			// doubles to singles

			else if(state.getBlock() == Blocks.GRASS_PATH || (state.getBlock() == Main.GRASS_PATH_SLAB && (SlabType)state.get(SlabBlock.TYPE) == SlabType.DOUBLE)){
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, Main.GRASS_PATH_SLAB.getDefaultState());

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}

			else if(state.getBlock() == Blocks.FARMLAND || (state.getBlock() == Main.FARMLAND_SLAB && (SlabType)state.get(SlabBlock.TYPE) == SlabType.DOUBLE)){
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, Main.FARMLAND_SLAB.getDefaultState());

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}

			else if(state.getBlock() == Blocks.COARSE_DIRT || (state.getBlock() == Main.COARSE_DIRT_SLAB && (SlabType)state.get(SlabBlock.TYPE) == SlabType.DOUBLE)){
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, Main.COARSE_DIRT_SLAB.getDefaultState());

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}

			else if(state.getBlock() == Blocks.PODZOL || (state.getBlock() == Main.PODZOL_SLAB && (SlabType)state.get(SlabBlock.TYPE) == SlabType.DOUBLE)){
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, Main.PODZOL_SLAB.getDefaultState());

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}

			else if(state.getBlock() == Blocks.MYCELIUM || (state.getBlock() == Main.MYCELIUM_SLAB && (SlabType)state.get(SlabBlock.TYPE) == SlabType.DOUBLE)){
				PlayerEntity player = context.getPlayer();

				if(!world.isClient){
					world.setBlockState(pos, Main.MYCELIUM_SLAB.getDefaultState());

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				else world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

				info.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}
}
