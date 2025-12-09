package limitless.enchantments.mixin;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import limitless.enchantments.LimitlessEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    public void init(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> worldKey, LevelStem dimensionOptions, ChunkProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<CustomSpawner> spawners, boolean shouldTickTime, @Nullable RandomSequences randomSequencesState, CallbackInfo info) {
        ServerLevel world = (ServerLevel) (Object) this;
        LimitlessEnchantments.loadSettings(world);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        @SuppressWarnings("resource")
        ServerLevel world = (ServerLevel) (Object) this;
        TickRateManager tickManager = world.tickRateManager();
        boolean bl = tickManager.runsNormally();

        if (bl) {
            try {
                world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).forEach((e) -> {
                    Holder<Enchantment> entry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).wrapAsHolder(e);
                    if (entry != null && LimitlessEnchantments.blackListedEnchantments.contains(entry.getRegisteredName())) {
                        LimitlessEnchantments.blackListedEnchantmentsMap.put(entry.getRegisteredName(), entry.value());
                    }
                });
            }
            catch (Exception e) {
            }
        }
    }
}
