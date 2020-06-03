package justfatlard.dirt_slab.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.Main;

@Mixin(CropBlock.class)
public class CropBlockMixin {
	@Inject(at = @At("TAIL"), method = "canPlantOnTop", cancellable = true)
	public void canPlantOnTop(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		Block block = state.getBlock();

		if(block == DirtSlabBlocks.FARMLAND_SLAB && Main.hasTopSlab(state)) info.setReturnValue(true);
	}

	@Inject(method = "scheduledTick", at = @At(value = "INVOKE", target = "net/minecraft/server/world/ServerWorld.setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
	private void onCropGrow(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo callbackInfo){
		Main.happyParticles(world, pos, state.get(CropBlock.AGE));
	}
}
