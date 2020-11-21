package user11681.commonformatting;

import user11681.phormat.api.FormattingBuilder;

public class CommonFormatting {
    public static final char[] OVERLINE_CODES = {
        4096,
        4096 + 1,
        4096 + 2,
        4096 + 3,
        4096 + 4,
        4096 + 5,
    };

    static {
        FormattingBuilder.create("OVERLINE_0", OVERLINE_CODES[0]).formatter(new OverlineFormatter(0)).build(true);
        FormattingBuilder.create("OVERLINE_1", OVERLINE_CODES[1]).formatter(new OverlineFormatter(1)).build(true);
        FormattingBuilder.create("OVERLINE_2", OVERLINE_CODES[2]).formatter(new OverlineFormatter(2)).build(true);
        FormattingBuilder.create("OVERLINE_3", OVERLINE_CODES[3]).formatter(new OverlineFormatter(3)).build(true);
        FormattingBuilder.create("OVERLINE_4", OVERLINE_CODES[4]).formatter(new OverlineFormatter(4)).build(true);
        FormattingBuilder.create("OVERLINE_5", OVERLINE_CODES[5]).formatter(new OverlineFormatter(5)).build(true);
    }
}
