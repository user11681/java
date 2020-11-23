package user11681.limitless

import com.google.common.math.IntMath
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap
import it.unimi.dsi.fastutil.objects.ReferenceArrayList
import user11681.commonformatting.CommonFormatting

object RomanNumerals {
    private val baseNumerals: Array<String> = arrayOf("I", "V", "X", "L", "C", "D", "M")
    private val cache: Long2ReferenceOpenHashMap<String> = Long2ReferenceOpenHashMap(longArrayOf(0), arrayOf("nulla"))
    private val roman: ReferenceArrayList<String> = ReferenceArrayList.wrap(baseNumerals)
    private val decimal: IntList = IntArrayList.wrap(intArrayOf(1, 5, 10, 50, 100, 500, 1000))

    @JvmStatic
    fun fromDecimal(decimal: Long): String {
        val cachedValue: String? = cache.get(decimal)

        if (cachedValue != null) {
            return cachedValue
        }

        var mutableDecimal: Long = decimal
        val roman = StringBuilder()
        val index: Int = RomanNumerals.decimal.size - 1
        val largest: Int = RomanNumerals.decimal.getInt(index)

        while (mutableDecimal >= largest) {
            roman.append(RomanNumerals.roman[index])
            mutableDecimal -= largest
        }

        var div = 1

        while (mutableDecimal >= div) {
            div *= 10
        }

        div /= 10

        while (mutableDecimal > 0) {
            val lastNum = (mutableDecimal / div).toInt()

            when {
                lastNum <= 3 -> {
                    for (i: Int in 0 until lastNum) {
                        roman.append(getRoman(div))
                    }
                }
                lastNum == 4 -> {
                    roman.append(getRoman(div)).append(getRoman(div * 5))
                }
                lastNum <= 8 -> {
                    roman.append(getRoman(div * 5))

                    for (i: Int in 0..lastNum - 5) {
                        roman.append(getRoman(div))
                    }
                }
                lastNum == 9 -> {
                    roman.append(getRoman(div)).append(getRoman(div * 10))
                }
            }

            mutableDecimal %= div
            div /= 10
        }

        cache[decimal] = roman.toString()

        return roman.toString()
    }

    private fun getRoman(decimal: Int): String {
        return roman[(RomanNumerals.decimal.indexOf(decimal))]
    }

    init {
        val baseCount: Int = baseNumerals.size

        for (level: Int in 0 until 2) {
            for (i: Int in 1 until baseCount) {
                val builder = StringBuilder()

                for (j: Int in 0..level) {
                    builder.append("§").append(CommonFormatting.OVERLINE_CODES[j])
                }

                roman.add(builder.append(baseNumerals[i]).append("§r").toString())
            }
        }

        for (level: Int in 1 until 3) {
            for (i: Int in 1 until baseCount) {
                decimal.add(decimal.getInt(i) * IntMath.pow(1000, level))
            }
        }
    }
}
