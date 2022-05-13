package net.fabric_hider.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.io.PrintStream;

@Mixin({PrintStream.class})
public class LoggerMixin {

    @Inject(at = @At("HEAD"), method = "println", cancellable = true, remap = false)
    public void println(String msg) {
        System.out.println("w");
    }

}
