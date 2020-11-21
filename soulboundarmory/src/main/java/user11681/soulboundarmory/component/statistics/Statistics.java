package user11681.soulboundarmory.component.statistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import nerdhub.cardinal.components.api.util.NbtSerializable;
import net.minecraft.nbt.CompoundTag;
import user11681.soulboundarmory.registry.Registries;

public class Statistics extends HashMap<Category, Map<StatisticType, Statistic>> implements NbtSerializable, Iterable<Statistic> {
    protected Statistics() {
    }

    public static Builder create() {
        return new Builder(new Statistics());
    }

    public Statistic get(final Category category, final StatisticType statistic) {
        return this.get(category).get(statistic);
    }

    public Statistic get(final StatisticType type) {
        for (final Map<StatisticType, Statistic> category : this.values()) {
            final Statistic statistic = category.get(type);

            if (statistic != null) {
                return statistic;
            }
        }

        return null;
    }

    public void put(final StatisticType type, final Number value) {
        this.get(type).setValue(value);
    }

    public void add(final StatisticType type, final Number value) {
        final Statistic statistic = this.get(type);

        if (statistic != null) {
            statistic.add(value);
        }
    }

    public int size(final Category category) {
        return this.get(category).size();
    }

    public void reset() {
        for (final Category category : this.keySet()) {
            this.reset(category);
        }
    }

    public void reset(final Category category) {
        for (final Statistic statistic : this.get(category).values()) {
            statistic.reset();
        }
    }

    @Nonnull
    @Override
    public Iterator<Statistic> iterator() {
        final Set<Statistic> statistics = new HashSet<>();

        for (final Map<StatisticType, Statistic> category : this.values()) {
            statistics.addAll(category.values());
        }

        return statistics.iterator();
    }

    @Nonnull
    @Override
    public CompoundTag toTag(@Nonnull final CompoundTag tag) {
        for (final Category category : this.keySet()) {
            tag.put(category.asString(), this.toTag(category));
        }

        return tag;
    }

    public CompoundTag toTag(final Category category) {
        final CompoundTag tag = new CompoundTag();

        for (final Statistic statistic : this.get(category).values()) {
            tag.put(statistic.getType().asString(), statistic.toTag(new CompoundTag()));
        }

        return tag;
    }

    @Override
    public void fromTag(final CompoundTag tag) {
        for (final String key : tag.getKeys()) {
            this.fromTag(tag.getCompound(key), Registries.CATEGORY.get(key));
        }
    }

    public void fromTag(final CompoundTag tag, final Category category) {
        if (category != null) {
            for (final String identifier : tag.getKeys()) {
                final Statistic statistic = this.get(Registries.STATISTIC.get(identifier));

                if (statistic != null) {
                    statistic.fromTag(tag.getCompound(identifier));
                }
            }
        }
    }

    public static class Builder {
        protected final Statistics statistics;

        protected Builder(final Statistics statistics) {
            this.statistics = statistics;
        }

        public Builder category(final Category categoryType, final StatisticType... statisticTypes) {
            final Map<StatisticType, Statistic> category = new HashMap<>();
            final Statistics statistics = this.statistics;

            for (final StatisticType statisticType : statisticTypes) {
                category.put(statisticType, new Statistic(categoryType, statisticType));
                statistics.put(categoryType, category);
            }

            return this;
        }

        public Builder min(final double min, final Category category) {
            for (final StatisticType statistic : this.statistics.get(category).keySet()) {
                this.min(min, statistic);
            }

            return this;
        }

        public Builder min(final double min, final StatisticType... types) {
            final Statistics statistics = this.statistics;

            for (final StatisticType type : types) {
                statistics.get(type).setMin(min);
            }

            return this;
        }

        public Builder max(final double max, final Category category) {
            for (final StatisticType type : this.statistics.get(category).keySet()) {
                this.max(max, type);
            }

            return this;
        }

        public Builder max(final double max, final StatisticType... types) {
            final Statistics statistics = this.statistics;

            for (final StatisticType type : types) {
                statistics.get(type).setMax(max);
            }

            return this;
        }

        public Statistics build() {
            return this.statistics;
        }
    }
}
