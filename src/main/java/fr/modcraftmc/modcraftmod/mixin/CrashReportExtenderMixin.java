package fr.modcraftmc.modcraftmod.mixin;

import net.minecraft.CrashReport;
import net.minecraftforge.logging.CrashReportExtender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CrashReportExtender.class, remap = false)
public class CrashReportExtenderMixin {

    @Inject(method = "addCrashReportHeader", at = @At("HEAD"))
    private static void addVgtomFault(StringBuilder stringbuilder, CrashReport crashReport, CallbackInfo ci) {
        stringbuilder.append("Encore de la faute a vgtom !!");
    }
}
