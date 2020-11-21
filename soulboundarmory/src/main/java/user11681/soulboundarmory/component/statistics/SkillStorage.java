package user11681.soulboundarmory.component.statistics;

import javax.annotation.Nonnull;
import nerdhub.cardinal.components.api.util.NbtSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import user11681.soulboundarmory.registry.Registries;
import user11681.soulboundarmory.skill.Skill;
import user11681.soulboundarmory.skill.SkillContainer;
import user11681.usersmanual.collections.OrderedArrayMap;

public class SkillStorage extends OrderedArrayMap<Skill, SkillContainer> implements NbtSerializable {
    public SkillStorage(final Skill... skills) {
        super();

        for (final Skill skill : skills) {
            this.put(skill, new SkillContainer(skill, this));
        }
    }

    public void add(SkillContainer skill) {
        this.put(skill.getSkill(), skill);
    }

    public boolean contains(final Skill skill) {
        final SkillContainer container = this.get(skill);

        return container != null && container.isLearned();
    }

    public boolean contains(final Skill skill, final int level) {
        final SkillContainer container = this.get(skill);

        return container != null && container.isLearned() && container.getLevel() >= level;
    }

    public void reset() {
        for (final SkillContainer container : this.values()) {
            if (container.hasDependencies())
                this.clear();
        }
    }

    @Nonnull
    public CompoundTag toTag(@Nonnull final CompoundTag tag) {
        for (final SkillContainer skill : this.values()) {
            if (skill != null) {
                tag.put(skill.getSkill().getIdentifier().toString(), skill.toTag(new CompoundTag()));
            }
        }

        return tag;
    }

    @Override
    public void fromTag(final CompoundTag tag) {
        for (final String identifier : tag.getKeys()) {
            final SkillContainer skill = this.get(Registries.SKILL.get(new Identifier(identifier)));

            if (skill != null) {
                skill.fromTag(tag.getCompound(identifier));
            }
        }
    }
}
