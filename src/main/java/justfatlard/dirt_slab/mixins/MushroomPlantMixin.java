package justfatlard.dirt_slab.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import justfatlard.dirt_slab.DirtSlabBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

@Mixin(MushroomPlantBlock.class)
public class MushroomPlantMixin {
	@Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
	public void canPlaceAt(BlockState blockState, WorldView view, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		BlockState state = view.getBlockState(pos.down());
		Block block = state.getBlock();

		if((block == DirtSlabBlocks.MYCELIUM_SLAB || block == DirtSlabBlocks.PODZOL_SLAB) && (state.get(SlabBlock.TYPE) == SlabType.TOP || state.get(SlabBlock.TYPE) == SlabType.DOUBLE)) info.setReturnValue(true);
	}
}
