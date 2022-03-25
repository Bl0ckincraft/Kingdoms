package fr.kellyan.kingdoms.configurations;

import fr.kellyan.kingdoms.Kingdoms;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * This class contain all the settings of the plugin.
 */
public class Settings {
    public static IntSetting minNameLength = new IntSetting(3, "min_name_length");
    public static IntSetting maxNameLength = new IntSetting(30, "max_name_length");

    /**
     * This method load all the settings values from the config.yml.
     */
    public static void loadValues() {
        FileConfiguration configYml = Kingdoms.getInstance().getConfig();

        minNameLength.loadFrom(configYml);
        maxNameLength.loadFrom(configYml);
    }

    /**
     * Replace the %name% of all settings by their values. Example : "Hello %min_name_length%" -> "Hello 3".
     * @param text the text to edit.
     * @return the edited text.
     */
    public static String replaceValues(String text) {
        String editedText = text;
        editedText = replaceValue(editedText, minNameLength);
        editedText = replaceValue(editedText, maxNameLength);
        return editedText;
    }

    /**
     * Replace the %name% of setting by her value.
     * @param text the text to edit.
     * @param setting the setting.
     * @return the edited text.
     */
    public static String replaceValue(String text, Setting setting) {
        return text.replace("%" + setting.getName() + "%", setting.getValue().toString());
    }

    /**
     * This is the core of a setting.
     * @param <T> the type of value.
     */
    private static abstract class Setting<T> {
        protected T value;
        protected T defaultValue;
        protected final String name;

        /**
         * This is the constructor of the class.
         * @param defaultValue the default value of the setting.
         * @param name the name/path of the setting.
         */
        protected Setting(T defaultValue, String name) {
            this.value = defaultValue;
            this.defaultValue = defaultValue;
            this.name = name;
        }

        /**
         * @return the default value.
         */
        public T getDefaultValue() {
            return defaultValue;
        }

        /**
         * @return the current value.
         */
        public T getValue() {
            return value;
        }

        /**
         * @return the name of the setting.
         */
        public String getName() {
            return name;
        }

        /**
         * Load the value from the configuration part.
         * @param section the configuration part.
         */
        public abstract void loadFrom(ConfigurationSection section);
    }

    /**
     * This is a setting which contain an integer.
     */
    private static class IntSetting extends Setting<Integer> {
        protected IntSetting(int defaultValue, String name) {
            super(defaultValue, name);
        }

        @Override
        public void loadFrom(ConfigurationSection section) {
            value = section.getInt(name, defaultValue);
        }
    }
}
