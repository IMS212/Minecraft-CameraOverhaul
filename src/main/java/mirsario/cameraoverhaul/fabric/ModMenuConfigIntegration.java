package mirsario.cameraoverhaul.fabric;

import java.util.function.*;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.gui.entries.*;
import mirsario.cameraoverhaul.common.*;
import mirsario.cameraoverhaul.common.configuration.*;
import mirsario.cameraoverhaul.core.configuration.*;
import net.minecraft.client.*;
import net.minecraft.text.*;

public class ModMenuConfigIntegration implements ModMenuApi
{
	private static final String ConfigEntriesPrefix = "cameraoverhaul.config";

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return screen -> GetConfigBuilder().build();
	}
	
	@SuppressWarnings("resource") //MinecraftClient.getInstance() isn't a resource
	public static ConfigBuilder GetConfigBuilder()
	{
		ConfigData config = CameraOverhaul.instance.config;
		
		ConfigBuilder builder = ConfigBuilder.create()
			.setParentScreen(MinecraftClient.getInstance().currentScreen)
			.setTitle(new TranslatableText("cameraoverhaul.config.title"))
			.transparentBackground()
			.setSavingRunnable(() -> {
				Configuration.SaveConfig(CameraOverhaul.instance.config, CameraOverhaul.Id, ConfigData.ConfigVersion);
			});
		
		ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("cameraoverhaul.config.category.general"));
		ConfigEntryBuilder entryBuilder = builder.entryBuilder();
		
		//Entries
		general.addEntry(CreateBooleanEntry(entryBuilder, "enabled", true, config.enabled, value -> config.enabled = value));
		general.addEntry(CreateFloatFactorEntry(entryBuilder, "strafingRollFactor", 1.0f, config.strafingRollFactor, value -> config.strafingRollFactor = value));
		general.addEntry(CreateFloatFactorEntry(entryBuilder, "yawDeltaRollFactor", 1.0f, config.yawDeltaRollFactor, value -> config.yawDeltaRollFactor = value));
		general.addEntry(CreateFloatFactorEntry(entryBuilder, "verticalVelocityPitchFactor", 1.0f, config.verticalVelocityPitchFactor, value -> config.verticalVelocityPitchFactor = value));
		general.addEntry(CreateFloatFactorEntry(entryBuilder, "forwardVelocityPitchFactor", 1.0f, config.forwardVelocityPitchFactor, value -> config.forwardVelocityPitchFactor = value));
		
		return builder;
	}
	//Entry Helpers
	public static BooleanListEntry CreateBooleanEntry(ConfigEntryBuilder entryBuilder, String entryName, Boolean defaultValue, Boolean value, Function<Boolean, Boolean> setter)
	{
		String lowerCaseName = entryName.toLowerCase();
		String baseTranslationPath = ConfigEntriesPrefix + "." + lowerCaseName;

		return entryBuilder.startBooleanToggle(new TranslatableText(baseTranslationPath + ".name"), value)
			.setDefaultValue(defaultValue)
			.setTooltip(new TranslatableText(baseTranslationPath + ".tooltip"))
			.setSaveConsumer(newValue -> setter.apply(newValue))
			.build();
	}
	public static FloatListEntry CreateFloatFactorEntry(ConfigEntryBuilder entryBuilder, String entryName, float defaultValue, float value, Function<Float, Float> setter)
	{
		String lowerCaseName = entryName.toLowerCase();
		String baseTranslationPath = ConfigEntriesPrefix + "." + lowerCaseName;

		return entryBuilder.startFloatField(new TranslatableText(baseTranslationPath + ".name"), value)
			.setDefaultValue(defaultValue)
			.setTooltip(new TranslatableText(baseTranslationPath + ".tooltip"))
			.setSaveConsumer(newValue -> setter.apply(newValue))
			.build();
	}
}
