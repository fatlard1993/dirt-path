package justfatlard.dirt_slab.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import justfatlard.dirt_slab.DirtSlabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@Mixin(PlantBlock.class)
public class PlantMixin {
	@Inject(at = @At("TAIL"), method = "canPlantOnTop", cancellable = true)
	public void canPlantOnTop(BlockState state, BlockView view, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		Block block = state.getBlock();

		if((block == DirtSlabBlocks.DIRT_SLAB || block == DirtSlabBlocks.COARSE_DIRT_SLAB || block == DirtSlabBlocks.GRASS_SLAB || block == DirtSlabBlocks.PODZOL_SLAB) && (state.get(SlabBlock.TYPE) == SlabType.TOP || state.get(SlabBlock.TYPE) == SlabType.DOUBLE)){
			info.setReturnValue(true);
		}
	}
}
