package carpet_extension;

import carpet.api.settings.Rule;

public class ExampleOwnSettings
{
    public enum Option
    {
        OPTION_A, OPTION_B, OPTION_C
    }

    @Rule(categories = "misc")
    public static int intSetting = 10;

    @Rule(
            options = {"foo", "bar", "baz"},
            categories = "misc",
            strict = false
    )
    public static String stringSetting = "foo";

    @Rule(
            categories = "misc")
    public static Option optionSetting = Option.OPTION_A;

    @Rule(categories = "misc")
    public static boolean boolSetting;
}
