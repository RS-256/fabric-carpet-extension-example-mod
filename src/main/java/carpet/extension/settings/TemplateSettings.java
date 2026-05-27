package carpet.extension.settings;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.api.settings.Validators;
import carpet.utils.Messenger;
import net.minecraft.commands.CommandSourceStack;

import static carpet.api.settings.RuleCategory.CREATIVE;

public class TemplateSettings
{
    public static class Carpet
    {
        private static class CheckValue extends Validator<Integer>
        {
            @Override
            public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String typedString)
            {
                Messenger.m(source, "rb Congrats, you just changed a setting to "+newValue);
                return newValue < 20000000 ? newValue : null;
            }
        }

        @Rule(
                options = {"32768", "250000", "1000000"},
                validators = {Validators.NonNegativeNumber.class, CheckValue.class},
                categories = {CREATIVE, "template"}
        )
        public static int uselessNumericalSetting = 32768;

        @Rule(categories = {"fun", "template"})
        public static boolean makarena = false;
    }

    public static class Own
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

        @Rule(categories = "misc")
        public static Option optionSetting = Option.OPTION_A;

        @Rule(categories = "misc")
        public static boolean boolSetting;
    }
}
