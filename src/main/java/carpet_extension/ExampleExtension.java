package carpet_extension;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class ExampleExtension implements CarpetExtension
{
    public static void noop() { }
    private static final SettingsManager mySettingManager;
    static
    {
        mySettingManager = new SettingsManager("1.0","examplemod","Example Mod");
        CarpetServer.manageExtension(new ExampleExtension());
    }

    @Override
    public void onGameStarted()
    {
        // let's /carpet handle our few simple settings
        CarpetServer.settingsManager.parseSettingsClass(ExampleSimpleSettings.class);
        // Lets have our own settings class independent from carpet.conf
        mySettingManager.parseSettingsClass(ExampleOwnSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server)
    {
        // reloading of /carpet settings is handled by carpet
        // reloading of own settings is handled as an extension, since we claim own settings manager
    }

    @Override
    public void onTick(MinecraftServer server)
    {
        // no need to add this.
    }

    @Override
    public void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext)
    {
        ExampleCommand.register(dispatcher);
    }

    @Override
    public SettingsManager extensionSettingsManager()
    {
        // this will ensure that our settings are loaded properly when world loads
        return mySettingManager;
    }

    @Override
    public Map<String, String> canHasTranslations(String lang)
    {
        return Map.of(
                "carpet.rule.uselessNumericalSetting.desc", "A simple numeric setting used by the example extension.",
                "carpet.rule.makarena.desc", "Toggles the example makarena rule.",
                "examplemod.rule.intSetting.desc", "A simple integer setting used by the example extension.",
                "examplemod.rule.stringSetting.desc", "A simple string setting used by the example extension.",
                "examplemod.rule.optionSetting.desc", "Selects one of the example option values.",
                "examplemod.rule.boolSetting.desc", "Toggles the example boolean setting."
        );
    }

    @Override
    public void onPlayerLoggedIn(ServerPlayer player)
    {
        //
    }

    @Override
    public void onPlayerLoggedOut(ServerPlayer player)
    {
        //
    }
}
