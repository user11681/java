@file:Suppress("UNCHECKED_CAST")

package user11681.limitless.enchanting

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentLevelEntry
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import user11681.limitless.asm.access.EnchantmentAccess
import kotlin.math.max
import kotlin.math.min

object EnchantmentUtil {
    const val INTERNAL_NAME = "user11681.limitless.enchanting"
    const val DESCRIPTOR = "Luser11681.limitless.enchanting;"

    const val SUCCESS = 0
    const val ADD = 1
    const val CONFLICT = 2

    @JvmStatic
    fun getHighestSuitableLevel(power: Int, enchantment: Enchantment, entries: MutableList<EnchantmentLevelEntry>) {
        var found = false
        var lastCandidate = 0

        for (i: Int in enchantment.minLevel..enchantment.maxLevel) {
            if (power >= enchantment.getMinPower(i) && power <= enchantment.getMaxPower(i)) {
                lastCandidate = i
                found = true

                if ((enchantment as EnchantmentAccess).limitless_getOriginalMaxLevel() == 1) {
                    break
                }
            } else {
                break
            }
        }

        if (found) {
            entries.add(EnchantmentLevelEntry(enchantment, lastCandidate))
        }
    }

    @JvmStatic
    fun mergeEnchantment(itemStack: ItemStack, enchantment: EnchantmentLevelEntry, mergeConflicts: Boolean) {
        val book: Boolean = itemStack.item == Items.ENCHANTED_BOOK

        if (book || itemStack.hasEnchantments()) {
            if (tryMerge(itemStack, enchantment, mergeConflicts) == ADD) {
                if (book) {
                    EnchantedBookItem.addEnchantment(itemStack, enchantment)
                } else {
                    itemStack.addEnchantment(enchantment.enchantment, enchantment.level)
                }
            }
        } else {
            itemStack.addEnchantment(enchantment.enchantment, enchantment.level)
        }
    }

    @JvmStatic
    fun mergeEnchantment(itemStack: ItemStack, enchantment: Enchantment, level: Int, mergeConflicts: Boolean) {
        val book: Boolean = itemStack.item == Items.ENCHANTED_BOOK

        if (book || itemStack.hasEnchantments()) {
            if (tryMerge(itemStack, enchantment, level, mergeConflicts) == ADD) {
                if (book) {
                    EnchantedBookItem.addEnchantment(itemStack, EnchantmentLevelEntry(enchantment, level))
                } else {
                    itemStack.addEnchantment(enchantment, level)
                }
            }
        } else {
            itemStack.addEnchantment(enchantment, level)
        }
    }

    @JvmStatic
    fun tryMerge(itemStack: ItemStack, enchantment: EnchantmentLevelEntry, mergeConflicts: Boolean): Int {
        return tryMerge(itemStack, enchantment.enchantment, enchantment.level, mergeConflicts)
    }

    @SuppressWarnings("unchecked")
    @JvmStatic
    fun tryMerge(itemStack: ItemStack, enchantment: Enchantment, level: Int, mergeConflicts: Boolean): Int {
        var status: Int = ADD
        val book: Boolean = itemStack.item == Items.ENCHANTED_BOOK

        val enchantments: ListTag = if (book) {
            EnchantedBookItem.getEnchantmentTag(itemStack)
        } else {
            itemStack.enchantments
        }

        for (enchantmentTag: CompoundTag in enchantments as Iterable<CompoundTag>) {
            if (Identifier(enchantmentTag.getString("id")) == Registry.ENCHANTMENT.getId(enchantment)) {
                val tagLevel: Int = enchantmentTag.getInt("lvl")

                if (tagLevel == level) {
                    enchantmentTag.putInt("lvl", min(tagLevel + 1, enchantment.maxLevel))
                } else {
                    enchantmentTag.putInt("lvl", max(tagLevel, level))
                }

                return SUCCESS
            } else if (!mergeConflicts && status != CONFLICT && !enchantment.canCombine(Registry.ENCHANTMENT.get(Identifier(enchantmentTag.getString("id"))))) {
                status = CONFLICT
            }
        }

        return status
    }
}
