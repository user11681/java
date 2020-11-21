package user11681.soulboundarmory.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.server.command.CommandSource;
import net.minecraft.util.Identifier;
import user11681.usersmanual.registry.ModRegistry;
import user11681.usersmanual.registry.RegistryEntry;

public class RegistryArgumentType<T extends RegistryEntry> implements ArgumentType<Set<T>> {
    protected final ModRegistry<T> registry;

    protected RegistryArgumentType(final ModRegistry<T> registry) {
        this.registry = registry;
    }

    public static <T extends RegistryEntry> RegistryArgumentType<T> registry(final ModRegistry<T> registry) {
        return new RegistryArgumentType<>(registry);
    }

    @Override
    public Set<T> parse(final StringReader reader) throws CommandSyntaxException {
        final String input = reader.readString();
        final ModRegistry<T> entries = this.registry;

        if (Pattern.compile(Pattern.quote("all"), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(input).find()) {
            return entries.values();
        }

        for (final Identifier name : this.registry.identifiers()) {
            if (Pattern.compile(Pattern.quote(name.getPath()), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(input).find()) {
                return Collections.singleton(entries.get(name));
            }
        }

        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, input);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(this.getSuggestions(context, builder), builder);
    }

    protected <S> Collection<String> getSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return this.registry.identifiers().parallelStream().map(Identifier::getPath).collect(Collectors.toSet());
    }
}
