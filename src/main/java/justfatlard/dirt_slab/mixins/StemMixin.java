package justfatlard.dirt_slab.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import justfatlard.dirt_slab.Main;

@Mixin(StemBlock.class)
public class StemMixin {
	@Inject(at = @At("TAIL"), method = "canPlantOnTop", cancellable = true)
	public void canPlantOnTop(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		Block block = state.getBlock();

		if(block == Main.FARMLAND_SLAB && (state.get(SlabBlock.TYPE) == SlabType.TOP || state.get(SlabBlock.TYPE) == SlabType.DOUBLE)) info.setReturnValue(true);
	}
}
