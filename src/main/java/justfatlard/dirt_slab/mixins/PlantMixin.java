package justfatlard.dirt_slab.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import justfatlard.dirt_slab.Main;
import justfatlard.dirt_slab.DirtSlabBlocks;

@Mixin(PlantBlock.class)
public class PlantMixin {
	@Inject(at = @At("TAIL"), method = "canPlantOnTop", cancellable = true)
	public void canPlantOnTop(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		if((Main.isDirtType(state.getBlock()) || state.getBlock() == DirtSlabBlocks.GRASS_SLAB) && Main.hasTopSlab(state)) info.setReturnValue(true);
	}
}
