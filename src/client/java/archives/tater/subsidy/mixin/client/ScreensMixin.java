package archives.tater.subsidy.mixin.client;

import archives.tater.subsidy.Subsidy;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;

@Mixin({
        InventoryScreen.class,
        CreativeModeInventoryScreen.class,
        AbstractContainerScreen.class
})
public class ScreensMixin {
    @WrapOperation(
            method = {
                    "init",
                    "containerTick",
                    "mouseClicked"
            },
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasInfiniteMaterials()Z")
    )
    private boolean check(LocalPlayer instance, Operation<Boolean> original) {
        return original.call(instance) || Subsidy.hasCreativeInventory(instance);
    }
}
