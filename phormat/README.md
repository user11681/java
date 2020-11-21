Phormat is a library that allows Fabric mod developers to easily add custom formatting to Minecraft.

### include
`build.gradle`:
```groovy
repositories {
    . . .
    maven {
        url "https://jitpack.io"
    }
}
. . .
dependencies {
    . . .
    modApi include("com.github.user11681:phormat:1.16-SNAPSHOT")
}
```

### use
`fabric.mod.json`:
```json
"entrypoints": {
    "phormat": [
        "com.example.Example"
    ]
}
```

`com/example/Example.java`:
```java
public class Example {
    private static final long start = System.currentTimeMillis();
    public static final Phormatting COLORS = new Phormatting("COLORS", 'u', 16, null).color(previous -> (int) (start - System.currentTimeMillis()) * 100 % 0xFFFFFF);
    public static final Phormatting EXAMPLE = new Phormatting("PHORMAT", 'x', true).formatter(new MyFormatter());
    public static final Phormatting PURPLE = new Phormatting("PURPLE", 'v', 17, 0xD083FF);
```

`com/example/MyFormatter.java`:
```java
public class MyFormatter extends Phormatter {
    @Override
    public void format(TextRendererDrawerAccess drawer,
                       Style style,
                       int i,
                       int j,
                       FontStorage storage,
                       Glyph glyph,
                       GlyphRenderer glyphRenderer,
                       boolean isBold,
                       float red,
                       float green,
                       float blue,
                       float alpha,
                       float advance) {
        int x = drawer.x();
        int y = drawer.y() + 3;

        drawer.invokeAddRectangle(new GlyphRenderer.Rectangle(x, y, x + advance, y + 2, 0.01F, red, green, blue, alpha);
}
```
