package justfatlard.dirt_slab.mixins;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.Main;

@Mixin(SugarCaneBlock.class)
public class SugarCaneMixin {
	@Inject(at = @At("TAIL"), method = "canPlaceAt", cancellable = true)
	public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		BlockPos groundPos = pos.down();
		BlockState groundState = world.getBlockState(groundPos);
		Block groundBlock = groundState.getBlock();

		if((groundBlock == DirtSlabBlocks.GRASS_SLAB || groundBlock == DirtSlabBlocks.DIRT_SLAB || groundBlock == DirtSlabBlocks.COARSE_DIRT_SLAB || groundBlock == DirtSlabBlocks.PODZOL_SLAB) && Main.hasTopSlab(groundState)){
			Iterator<Direction> horizontalIterator = Direction.Type.HORIZONTAL.iterator();

			while(horizontalIterator.hasNext()){
				Direction direction = (Direction)horizontalIterator.next();
				BlockState blockState = world.getBlockState(groundPos.offset(direction));
				FluidState fluidState = world.getFluidState(groundPos.offset(direction));

				if(fluidState.matches(FluidTags.WATER) || blockState.getBlock() == Blocks.FROSTED_ICE) info.setReturnValue(true);
			}
		}
	}
}
