package user11681.phormat.api;

import java.util.List;
import net.minecraft.util.Formatting;
import user11681.phormat.asm.access.FormattingAccess;

/**
 * This entrypoint is called in order to register custom {@link Formatting} entries.
 */
public interface FormattingInitializer {
    /**
     * Register {@link Formatting}s in this method by using {@link FormattingRegistry#register}.<br>
     * {@link #customize} is called later with a list of entries registered by this entrypoint.
     *
     * @param registry the {@linkplain FormattingRegistry registry} to which to register custom {@link Formatting}s.
     */
    void register(FormattingRegistry registry);

    /**
     * Make any necessary remaining modifications to custom {@link Formatting}s, exposed as instances of {@link FormattingAccess}.<br>
     *
     * @param entries the list of registered formattings in the order in which they were registered.
     */
    default void customize(List<FormattingAccess> entries) {}
}
