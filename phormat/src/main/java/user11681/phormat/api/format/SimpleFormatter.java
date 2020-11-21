package user11681.phormat.api.format;

import java.util.function.Consumer;

/**
 * A formatter that delegates formatting to the {@linkplain Consumer} stored in {@linkplain #formatter}.
 */
public class SimpleFormatter extends AbstractFormatter {
    /**
     * This formatter's delegate.
     */
    protected final Consumer<FormattingContext> formatter;

    /**
     * @param formatter the {@linkplain Consumer} to which to delegate formatting.
     */
    public SimpleFormatter(final Consumer<FormattingContext> formatter) {
        this.formatter = formatter;
    }

    /**
     * Delegate formatting to {@linkplain #formatter}.
     */
    @Override
    public void format() {
        this.formatter.accept(this.context);
    }
}
