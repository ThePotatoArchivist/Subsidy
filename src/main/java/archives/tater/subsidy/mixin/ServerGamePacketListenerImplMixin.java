package archives.tater.subsidy.mixin;

import archives.tater.subsidy.Subsidy;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @WrapOperation(
            method = {
                    "handlePickItemFromBlock",
                    "handlePickItemFromEntity",
                    "tryPickItem",
                    "handleSetCreativeModeSlot",
                    "hasInfiniteMaterials"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;hasInfiniteMaterials()Z")
    )
    private boolean check(ServerPlayer instance, Operation<Boolean> original) {
        return original.call(instance) || Subsidy.hasCreativeInventory(instance);
    }
}