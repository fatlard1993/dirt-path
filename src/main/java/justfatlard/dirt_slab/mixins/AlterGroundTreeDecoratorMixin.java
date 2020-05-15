package justfatlard.dirt_slab.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.Main;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.decorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

@Mixin(AlterGroundTreeDecorator.class)
public class AlterGroundTreeDecoratorMixin {
	@Inject(at = @At("HEAD"), method = "method_23463")
	public void method_23463(ModifiableTestableWorld world, Random rand, BlockPos pos, CallbackInfo info){
		for(int x = 2; x >= -3; --x){
			BlockPos posUp = pos.up(x);

			if(world.testBlockState(posUp, (state) -> { return Main.isDirtType(state.getBlock()); })){
				world.testBlockState(posUp, (state) -> {
					world.setBlockState(posUp, DirtSlabBlocks.PODZOL_SLAB.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)), 19);

					return true;
				});
			}

			if(!AbstractTreeFeature.isAir(world, posUp) && x < 0) break;
		}
	}
}
