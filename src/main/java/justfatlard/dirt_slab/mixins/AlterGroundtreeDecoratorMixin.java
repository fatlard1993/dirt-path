package justfatlard.dirt_slab.mixins;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import justfatlard.dirt_slab.DirtSlabBlocks;
import justfatlard.dirt_slab.Main;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.decorator.AlterGroundTreeDecorator;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

@Mixin(AlterGroundTreeDecorator.class)
public class AlterGroundtreeDecoratorMixin {
	@Inject(at = @At("HEAD"), method = "method_23463")
	public void method_23463(ModifiableTestableWorld modifiableTestableWorld, Random random, BlockPos blockPos, CallbackInfo info){
		for(int x = 2; x >= -3; --x){
			BlockPos posUp = blockPos.up(x);

			setPodzol(modifiableTestableWorld, posUp);

			if(!AbstractTreeFeature.isAir(modifiableTestableWorld, posUp) && x < 0) break;
		}
	}

	private void setPodzol(ModifiableTestableWorld world, BlockPos pos){
		if(world.testBlockState(pos, (state) -> { return Main.isDirtType(state.getBlock()); })){
			BlockState podzolState = DirtSlabBlocks.PODZOL_SLAB.getDefaultState();

			world.testBlockState(pos, (state) -> {
				world.setBlockState(pos, podzolState.with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)), 19);

				return true;
			});
		}
	}
}
