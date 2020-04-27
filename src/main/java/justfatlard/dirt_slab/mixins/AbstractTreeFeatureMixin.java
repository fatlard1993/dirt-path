package justfatlard.dirt_slab.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

import justfatlard.dirt_slab.Main;

@Mixin(AbstractTreeFeature.class)
public class AbstractTreeFeatureMixin {
	@Inject(at = @At("HEAD"), method = "isNaturalDirt", cancellable = true)
	private static void isNaturalDirt(TestableWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		if(world.testBlockState(pos, (state) -> { return Main.isDirtType(state.getBlock()) && Main.hasTopSlab(state); })) info.setReturnValue(true);
	}

	@Inject(at = @At("HEAD"), method = {"isNaturalDirtOrGrass", "isDirtOrGrass"}, cancellable = true)
	private static void isNaturalDirtOrGrass(TestableWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		if(world.testBlockState(pos, (state) -> { return Main.isGrassType(state.getBlock()) && Main.hasTopSlab(state); })) info.setReturnValue(true);
	}
}
