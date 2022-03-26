package fr.blockincraft.kingdoms;

import fr.blockincraft.kingdoms.commands.CmdCompleter;
import fr.blockincraft.kingdoms.commands.CmdExecutor;
import fr.blockincraft.kingdoms.configurations.Lang;
import fr.blockincraft.kingdoms.configurations.Settings;
import fr.blockincraft.kingdoms.listeners.ClaimListeners;
import fr.blockincraft.kingdoms.listeners.MenuListeners;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * This is the core of the plugin.
 */
public final class Kingdoms extends JavaPlugin {
    private static Kingdoms instance;

    /**
     * @return the current instance of the plugin
     */
    public static Kingdoms getInstance() {
        return instance;
    }

    private final KingdomsData data = new KingdomsData();

    /**
     * @return the current data of the plugin.
     */
    public KingdomsData getData() {
        return data;
    }

    public Economy serverEconomy;

    /**
     * @return the economy used by the server.
     */
    public Economy getServerEconomy() {
        return serverEconomy;
    }

    /**
     * Initialize and start the plugin.
     */
    @Override
    public void onEnable() {
        //Set the instance.
        instance = this;

        //Initialize all the bukkit/dependencies elements.
        if (!initCommands() || !setupEconomy()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        initListeners();

        //Load all the configuration values.
        FileHelper.saveAllDefaultConfigs();
        Settings.loadValues();
        Lang.loadMessages();
        CmdExecutor.loadActions();

        Lang.sendMessage(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', Lang.pluginOn.toString()));}

    /**
     * Set the executor and the completer of the /kingdoms command.
     * @return if the initialization was complete.
     */
    private boolean initCommands() {
        try {
            PluginCommand cmd = Objects.requireNonNull(Bukkit.getPluginCommand("kingdoms"));
            cmd.setExecutor(new CmdExecutor());
            cmd.setTabCompleter(new CmdCompleter());
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Register all the listeners used by the plugin.
     */
    private void initListeners() {
        Bukkit.getPluginManager().registerEvents(new ClaimListeners(), this);
        Bukkit.getPluginManager().registerEvents(new MenuListeners(), this);
    }

    /**
     * Get the economy used by the server provided by Vault
     * @return If the economy was found.
     */
    private boolean setupEconomy() {
        //Check if the Vault plugin is present.
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        //Check if the class Economy has an instance and get it.
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        //Initialize the server economy variable.
        serverEconomy = rsp.getProvider();

        return true;
    }

    /**
     * Save data and stop the plugin.
     */
    @Override
    public void onDisable() {
        Lang.sendMessage(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', Lang.pluginOff.toString()));
    }
}
