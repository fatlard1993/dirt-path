package justfatlard.dirt_slab.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.Main;

@Mixin(MushroomPlantBlock.class)
public class MushroomPlantMixin {
	@Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
	public void canPlaceAt(BlockState state, WorldView view, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		BlockState groundState = view.getBlockState(pos.down());
		Block groundBlock = groundState.getBlock();

		if((groundBlock == DirtSlabBlocks.MYCELIUM_SLAB || groundBlock == DirtSlabBlocks.PODZOL_SLAB) && Main.hasTopSlab(groundState)) info.setReturnValue(true);
	}
}
