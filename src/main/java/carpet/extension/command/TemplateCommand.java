package carpet.extension.command;

import carpet.extension.settings.TemplateSettings;
import carpet.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

public class TemplateCommand
{
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        dispatcher.register(literal("testcommand").
                then(literal("first").
                        executes( (c)-> {
                            Messenger.m(c.getSource(), "gi Shhhh.....");
                            return 1;
                        })).
                then(literal("second").
                        executes( (c)-> listSettings(c.getSource()))));

    }

    private static int listSettings(CommandSourceStack source)
    {
        Messenger.m(source, "w Here is all the settings we manage:");
        Messenger.m(source, "w Own stuff:");
        Messenger.m(source, "w  - boolean: "+ TemplateSettings.Own.boolSetting);
        Messenger.m(source, "w  - string: "+ TemplateSettings.Own.stringSetting);
        Messenger.m(source, "w  - int: "+ TemplateSettings.Own.intSetting);
        Messenger.m(source, "w  - enum: "+ TemplateSettings.Own.optionSetting);
        Messenger.m(source, "w Carpet Managed:");
        Messenger.m(source, "w  - makarena: "+ TemplateSettings.Carpet.makarena);
        Messenger.m(source, "w  - useless numerical setting: "+ TemplateSettings.Carpet.uselessNumericalSetting);
        return 1;
    }
}
