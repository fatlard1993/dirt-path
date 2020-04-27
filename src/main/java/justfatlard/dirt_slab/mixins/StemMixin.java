package justfatlard.dirt_slab.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.Main;

@Mixin(StemBlock.class)
public class StemMixin {
	@Inject(at = @At("TAIL"), method = "canPlantOnTop", cancellable = true)
	public void canPlantOnTop(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		Block block = state.getBlock();

		if(block == DirtSlabBlocks.FARMLAND_SLAB && Main.hasTopSlab(state)) info.setReturnValue(true);
	}

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/block/BlockState.getBlock()Lnet/minecraft/block/Block;", ordinal = 0), method = "scheduledTick", locals = LocalCapture.CAPTURE_FAILSOFT)
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos stemPos, Random random, CallbackInfo ci, Direction direction, BlockPos placementPos, Block block){
		BlockState groundState = world.getBlockState(placementPos.down());

		if(Main.isGrassType(groundState.getBlock()) && Main.hasTopSlab(groundState) && world.getBlockState(placementPos).isAir()){
			StemBlock stem = (StemBlock)(Object)this;

			world.setBlockState(placementPos, stem.getGourdBlock().getDefaultState());
			world.setBlockState(stemPos, stem.getGourdBlock().getAttachedStem().getDefaultState().with(HorizontalFacingBlock.FACING, direction));
		}
	}
}
