package user11681.limitless

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer
import net.fabricmc.api.*
import net.minecraft.item.ItemStack
import user11681.limitless.config.LimitlessConfiguration
import user11681.limitless.config.enchantment.provider.EnchantmentListProvider
import java.lang.reflect.Field

@EnvironmentInterface(value = EnvType.CLIENT, itf = ClientModInitializer::class)
object Limitless : ModInitializer, ClientModInitializer {
    const val ID: String = "limitless"

    @JvmField
    val forConflictRemoval: ReferenceOpenHashSet<ItemStack> = ReferenceOpenHashSet()

    override fun onInitialize() {
        LimitlessConfiguration.instance = AutoConfig.register(LimitlessConfiguration::class.java, ::JanksonConfigSerializer).config
    }

    @Environment(EnvType.CLIENT)
    override fun onInitializeClient() {
        AutoConfig.getGuiRegistry(LimitlessConfiguration::class.java).registerPredicateProvider(EnchantmentListProvider(), {field: Field -> field.name == "maxLevels"})
    }
}
