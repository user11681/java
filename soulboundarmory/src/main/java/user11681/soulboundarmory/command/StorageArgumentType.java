package user11681.soulboundarmory.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import user11681.soulboundarmory.component.soulbound.item.ItemStorage;
import user11681.soulboundarmory.component.soulbound.item.StorageType;
import user11681.soulboundarmory.registry.Registries;

public class StorageArgumentType extends RegistryArgumentType<StorageType<? extends ItemStorage<?>>> {
    protected StorageArgumentType() {
        super(Registries.STORAGE);
    }

    public static StorageArgumentType storages() {
        return new StorageArgumentType();
    }

    @SuppressWarnings("unchecked")
    public static Set<StorageType<? extends ItemStorage<?>>> getStorages(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Set.class);
    }

    @Override
    public Set<StorageType<? extends ItemStorage<?>>> parse(final StringReader reader) throws CommandSyntaxException {
        final int cursor = reader.getCursor();

        if (Pattern.compile(Pattern.quote("current"), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(reader.readString()).find()) {
            return new HashSet<>();
        }

        reader.setCursor(cursor);

        return super.parse(reader);
    }

    @Override
    protected <S> Collection<String> getSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        final Collection<String> suggestions = super.getSuggestions(context, builder);
        suggestions.add("current");

        return suggestions;
    }
}
