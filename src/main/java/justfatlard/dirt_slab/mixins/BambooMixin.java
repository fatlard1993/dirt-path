package justfatlard.dirt_slab.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import justfatlard.dirt_slab.Main;

@Mixin(BambooBlock.class)
public class BambooMixin {
	@Inject(at = @At("HEAD"), method = "canPlaceAt", cancellable = true)
	public void canPlaceAt(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> info){
		if(Main.isGrassType(state.getBlock()) && Main.hasTopSlab(state) && world.getBlockState(pos.up()).isAir()) info.setReturnValue(true);
	}

	@Inject(at = @At("HEAD"), method = "getPlacementState", cancellable = true)
	public void getPlacementState(ItemPlacementContext context, CallbackInfoReturnable<BlockState> info){
		BlockPos pos = context.getBlockPos();
		BlockState state = context.getWorld().getBlockState(pos);

		if(Main.isGrassType(state.getBlock()) && Main.hasTopSlab(state) && context.getWorld().getBlockState(pos.up()).isAir()) info.setReturnValue(Blocks.BAMBOO_SAPLING.getDefaultState());
	}
}