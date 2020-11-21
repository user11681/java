package user11681.invisiblelivingentities.mixin.mixin;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.DonkeyEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity> extends EntityRenderer<T> {
    @Unique
    private static final ReferenceArrayList<Class<?>> classes = ReferenceArrayList.wrap(new Class<?>[]{
        BeeEntity.class,
        CatEntity.class,
        ChickenEntity.class,
        CowEntity.class,
        DolphinEntity.class,
        DonkeyEntity.class,
        FishEntity.class,
        FoxEntity.class,
        HorseEntity.class,
        LlamaEntity.class,
        OcelotEntity.class,
        PandaEntity.class,
        ParrotEntity.class,
        PigEntity.class,
        PlayerEntity.class,
        PolarBearEntity.class,
        SheepEntity.class,
        SquidEntity.class,
        TurtleEntity.class,
        WolfEntity.class,
    });

    protected LivingEntityRendererMixin(final EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void cancelRender(final T entity, final float yaw, final float tickDelta, final MatrixStack matrices, final VertexConsumerProvider vertexConsumerProvider, final int light, final CallbackInfo info) {
        for (final Class<?> klass : classes) {
            if (klass.isAssignableFrom(entity.getClass())) {
                this.renderLabelIfPresent(entity, entity.getDisplayName(), matrices, vertexConsumerProvider, light);

                info.cancel();

                return;
            }
        }
    }
}
