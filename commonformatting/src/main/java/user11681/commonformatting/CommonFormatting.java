package user11681.commonformatting;

import java.util.List;
import user11681.phormat.api.FormattingInitializer;
import user11681.phormat.api.FormattingRegistry;
import user11681.phormat.asm.access.FormattingAccess;

public class CommonFormatting implements FormattingInitializer {
    public static final char[] OVERLINE_CODES = {
        4096,
        4096 + 1,
        4096 + 2,
        4096 + 3,
        4096 + 4,
        4096 + 5,
    };

    @Override
    public void register(final FormattingRegistry registry) {
        registry.register("OVERLINEA", OVERLINE_CODES[0], true);
        registry.register("OVERLINEB", OVERLINE_CODES[1], true);
        registry.register("OVERLINEC", OVERLINE_CODES[2], true);
        registry.register("OVERLINED", OVERLINE_CODES[3], true);
        registry.register("OVERLINEE", OVERLINE_CODES[4], true);
        registry.register("OVERLINEF", OVERLINE_CODES[5], true);
    }

    @Override
    public void customize(final List<FormattingAccess> entries) {
        for (final FormattingAccess entry : entries) {
            if (entry.getCode() >= 4096) {
                entry.setFormatter(new OverlineFormatter(entry.getCode() - 4096));
            }
        }
    }
}
