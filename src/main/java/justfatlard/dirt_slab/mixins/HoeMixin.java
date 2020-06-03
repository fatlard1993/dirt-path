package justfatlard.dirt_slab.mixins;

import java.util.Map;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import justfatlard.dirt_slab.Main;
import justfatlard.dirt_slab.SlicedTopSlab;

@Mixin(HoeItem.class)
public class HoeMixin {
	@Shadow
	@Final
	@Mutable
	private static Map<Block, BlockState> TILLED_BLOCKS;

	static {
		TILLED_BLOCKS.put(Blocks.FARMLAND, Blocks.DIRT.getDefaultState());
	}

	@Inject(method = "useOnBlock", locals = LocalCapture.CAPTURE_FAILHARD, at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/world/World.setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void onTillBlock(ItemUsageContext context, CallbackInfoReturnable info, World world, BlockPos pos, BlockState state, PlayerEntity player){
		if(state.getBlock() == Blocks.FARMLAND) world.getBlockTickScheduler().schedule(pos, state.getBlock(), 1);
	}

	@Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
	private void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info){
		BlockPos pos = context.getBlockPos();
		World world = context.getWorld();
		BlockState state = world.getBlockState(pos);

		if(context.getSide() != Direction.DOWN && SlicedTopSlab.canExistAt(state, world, pos)){
			PlayerEntity player = context.getPlayer();
			Block block = state.getBlock();
			Boolean success = false;
			BlockState newState = Blocks.GREEN_WOOL.getDefaultState();

			if(block == DirtSlabBlocks.COARSE_DIRT_SLAB || block == DirtSlabBlocks.FARMLAND_SLAB){
				newState = DirtSlabBlocks.DIRT_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

				success = true;

				if(world.isClient) world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

			else if(block == DirtSlabBlocks.DIRT_SLAB || block == DirtSlabBlocks.GRASS_SLAB || block == DirtSlabBlocks.GRASS_PATH_SLAB){
				newState = DirtSlabBlocks.FARMLAND_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));

				success = true;

				if(world.isClient) world.playSound(player, pos, SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

			if(success){
				if(!world.isClient){
					world.setBlockState(pos, newState);

					if(player != null) context.getStack().damage(1, (LivingEntity)player, (Consumer<LivingEntity>)((playerEntity_1x) -> { (playerEntity_1x).sendToolBreakStatus(context.getHand()); }));
				}

				Main.dirtParticles(world, pos, 1);

				info.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}
}