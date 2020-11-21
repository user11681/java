package user11681.limitless.asm.mixin.enchantment;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import user11681.limitless.config.enchantment.entry.EnchantmentParticleConfiguration;
import user11681.limitless.config.LimitlessConfiguration;
import user11681.limitless.config.enchantment.entry.radius.HorizontalRadius;
import user11681.limitless.config.enchantment.entry.radius.VerticalRadius;
import user11681.limitless.enchantment.EnchantingBlocks;

@Mixin(value = EnchantingTableBlock.class,
       priority = 100)
abstract class EnchantingTableBlockMixin extends BlockWithEntity {
    protected EnchantingTableBlockMixin(final Settings settings) {
        super(settings);
    }

    /**
     * @author user11681
     * @reason replacing the loop to increase glyph particle range.
     */
    @Overwrite
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState enchantingTableState, World world, BlockPos enchantingTablePos, Random random) {
        super.randomDisplayTick(enchantingTableState, world, enchantingTablePos, random);

        final LimitlessConfiguration configuration = LimitlessConfiguration.instance;
        final EnchantmentParticleConfiguration particleConfiguration = configuration.enchantment.particles;

        if (particleConfiguration.enabled) {
            final HorizontalRadius horizontalRadiusRange;
            final VerticalRadius verticalRadiusRange;

            if (particleConfiguration.inherit) {
                horizontalRadiusRange = configuration.enchantment.enchantingBlocks.radius.xz;
                verticalRadiusRange = configuration.enchantment.enchantingBlocks.radius.y;
            } else {
                horizontalRadiusRange = particleConfiguration.radius.xz;
                verticalRadiusRange = particleConfiguration.radius.y;
            }

            final int maxVerticalRadius = verticalRadiusRange.max;
            final int maxHorizontalRadius = horizontalRadiusRange.max;
            int verticalRadius;
            int horizontalRadius;
            int end;
            int displacement;
            BlockState blockState;

            for (int direction = -1; direction <= 1; direction += 2) {
                for (verticalRadius = verticalRadiusRange.min; verticalRadius <= maxVerticalRadius; verticalRadius++) {
                    for (horizontalRadius = horizontalRadiusRange.min; horizontalRadius <= maxHorizontalRadius; horizontalRadius++) {
                        end = direction * horizontalRadius;

                        if (random.nextInt(16) == 0) {
                            for (int distance = -horizontalRadius; distance <= horizontalRadius; distance++) {
                                displacement = direction * distance;
                                blockState = world.getBlockState(enchantingTablePos.add(displacement, verticalRadius, end));

                                if (blockState.isIn(EnchantingBlocks.tag)) {
                                    world.addParticle(ParticleTypes.ENCHANT,
                                        enchantingTablePos.getX() + 0.5,
                                        enchantingTablePos.getY() + 2,
                                        enchantingTablePos.getZ() + 0.5,
                                        displacement + random.nextFloat() - 0.5,
                                        verticalRadius - random.nextFloat() - 1,
                                        end + random.nextFloat() - 0.5
                                    );
                                }

                                if (distance != -horizontalRadius && distance != horizontalRadius) {
                                    blockState = world.getBlockState(enchantingTablePos.add(end, verticalRadius, displacement));

                                    if (blockState.isIn(EnchantingBlocks.tag)) {
                                        world.addParticle(ParticleTypes.ENCHANT,
                                            enchantingTablePos.getX() + 0.5,
                                            enchantingTablePos.getY() + 2,
                                            enchantingTablePos.getZ() + 0.5,
                                            end + random.nextFloat() - 0.5,
                                            verticalRadius - random.nextFloat() - 1,
                                            displacement + random.nextFloat() - 0.5
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
