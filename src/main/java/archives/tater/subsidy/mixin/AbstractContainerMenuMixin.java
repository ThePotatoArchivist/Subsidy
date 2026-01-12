package archives.tater.subsidy.mixin;

import archives.tater.subsidy.Subsidy;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {
    @WrapOperation(
            method = {
                    "doClick",
                    "isValidQuickcraftType"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasInfiniteMaterials()Z")
    )
    private static boolean check(Player instance, Operation<Boolean> original) {
        return original.call(instance) || Subsidy.hasCreativeInventory(instance);
    }
}
