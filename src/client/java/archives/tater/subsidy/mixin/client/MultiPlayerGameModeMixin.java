package archives.tater.subsidy.mixin.client;

import archives.tater.subsidy.Subsidy;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @WrapOperation(
            method = {
                    "handleCreativeModeItemAdd",
                    "handleCreativeModeItemDrop"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasInfiniteMaterials()Z")
    )
    private boolean check(LocalPlayer instance, Operation<Boolean> original) {
        return original.call(instance) || Subsidy.hasCreativeInventory(instance);
    }
}
