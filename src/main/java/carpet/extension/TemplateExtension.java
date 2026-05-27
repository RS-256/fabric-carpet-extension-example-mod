package carpet.extension;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.extension.command.TemplateCommand;
import carpet.extension.settings.TemplateSettings;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TemplateExtension implements CarpetExtension
{
    public static final String MOD_ID = "template";
    public static final String MOD_NAME = "TemplateMod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private static final SettingsManager SETTINGS_MANAGER = new SettingsManager("1.0", MOD_ID, MOD_NAME);

    static
    {
        CarpetServer.manageExtension(new TemplateExtension());
    }

    public static void noop() { }

    public static String id(String path)
    {
        return MOD_ID + ":" + path;
    }

    public static String reference(String path)
    {
        return id(path);
    }

    @Override
    public void onGameStarted()
    {
        LOGGER.info("Registering {} Carpet extension", MOD_NAME);
        CarpetServer.settingsManager.parseSettingsClass(TemplateSettings.Carpet.class);
        SETTINGS_MANAGER.parseSettingsClass(TemplateSettings.Own.class);
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
        TemplateCommand.register(dispatcher);
    }

    @Override
    public SettingsManager extensionSettingsManager()
    {
        // this will ensure that our settings are loaded properly when world loads
        return SETTINGS_MANAGER;
    }

    @Override
    public Map<String, String> canHasTranslations(String lang)
    {
        return Map.of(
                "carpet.rule.uselessNumericalSetting.desc", "A simple numeric setting used by the template extension.",
                "carpet.rule.makarena.desc", "Toggles the template makarena rule.",
                "template.rule.intSetting.desc", "A simple integer setting used by the template extension.",
                "template.rule.stringSetting.desc", "A simple string setting used by the template extension.",
                "template.rule.optionSetting.desc", "Selects one of the template option values.",
                "template.rule.boolSetting.desc", "Toggles the template boolean setting."
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
