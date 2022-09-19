package fr.blockincraft.kingdoms;

import com.google.common.base.Charsets;
import fr.blockincraft.kingdoms.command.completer.AreasCompleter;
import fr.blockincraft.kingdoms.command.completer.ClaimsCompleter;
import fr.blockincraft.kingdoms.command.completer.KingdomsCompleter;
import fr.blockincraft.kingdoms.command.executor.AreasExecutor;
import fr.blockincraft.kingdoms.command.executor.ClaimsExecutor;
import fr.blockincraft.kingdoms.command.executor.KingdomsExecutor;
import fr.blockincraft.kingdoms.compat.DynmapManager;
import fr.blockincraft.kingdoms.compat.WorldGuardWrapper;
import fr.blockincraft.kingdoms.core.entity.*;
import fr.blockincraft.kingdoms.listeners.*;
import fr.blockincraft.kingdoms.task.ReloadDynmapTask;
import fr.blockincraft.kingdoms.api.RequestHandler;
import fr.blockincraft.kingdoms.task.VisualizeTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Kingdoms extends JavaPlugin {
    private static Kingdoms instance;

    private File langFile;
    private FileConfiguration langConfig;

    private SessionFactory sessionFactory;
    private Registry registry;
    private Economy economy;
    private WorldGuardWrapper worldGuardWrapper;
    private DynmapManager dynmapManager;
    private Server webServer;

    private boolean slimefun;
    private boolean itemsAdder;

    public static Kingdoms getInstance() {
        return instance;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Registry getRegistry() {
        return registry;
    }

    public Economy getEconomy() {
        return economy;
    }

    public WorldGuardWrapper getWorldGuardWrapper() {
        return worldGuardWrapper;
    }

    public DynmapManager getDynmapManager() {
        return dynmapManager;
    }

    public boolean isSlimefun() {
        return slimefun;
    }

    public boolean isItemsAdder() {
        return itemsAdder;
    }

    @Override
    public void onLoad() {
        try {
            worldGuardWrapper = new WorldGuardWrapper();
            worldGuardWrapper.initWorldGuardFlag();
        } catch (Exception e) {
            worldGuardWrapper = null;
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            saveResource("lang.yml", false);
        }
        reloadLangConfig();

        if (!verifyDriver()) {
            loadingError("Cannot find mysql Driver class");
            return;
        }
        if (!initHibernateSession()) {
            loadingError("Cannot create an hibernate session factory");
            return;
        }

        if (!initEconomy()) {
            loadingError("Economy cannot be loaded");
            return;
        }

        instance = this;

        registry = new Registry();
        registry.init();

        loadServer();

        if (!startServer()) {
            loadingError("Web server cannot be started");
            return;
        }


        if (Bukkit.getPluginManager().isPluginEnabled("dynmap")) {
            this.dynmapManager = new DynmapManager();
            ReloadDynmapTask.startTask();
        }

        slimefun = Bukkit.getPluginManager().isPluginEnabled("Slimefun");
        itemsAdder = Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");

        initListeners();
        initCommands();

        VisualizeTask.startTask();
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.closeInventory();
        }

        if (sessionFactory != null) sessionFactory.close();

        sessionFactory = null;
        economy = null;
        worldGuardWrapper = null;
        dynmapManager = null;

        try {
            webServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        instance = null;
    }

    private void loadingError(String cause) {
        Logger logger = this.getLogger();

        logger.log(Level.SEVERE, cause);
        logger.log(Level.SEVERE, "Stopping");

        Bukkit.getPluginManager().disablePlugin(this);
    }

    private boolean verifyDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private boolean initHibernateSession() {
        ConfigurationSection dbConfig = this.getConfig().getConfigurationSection("database");

        if (dbConfig == null) return false;

        String url = "jdbc:mysql://" + dbConfig.get("ip", "") + ":" + dbConfig.get("port", 3306) + "/" + dbConfig.get("name", "kingdoms");
        String user = (String) dbConfig.get("user", "");
        String pass = (String) dbConfig.get("pass", "");

        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            configuration.setProperty("hibernate.connection.url", url);
            configuration.setProperty("hibernate.connection.username", user);
            configuration.setProperty("hibernate.connection.password", pass);

            configuration.addAnnotatedClass(Area.class);
            configuration.addAnnotatedClass(CurrentCommission.class);
            configuration.addAnnotatedClass(Kingdom.class);
            configuration.addAnnotatedClass(KingdomClaim.class);
            configuration.addAnnotatedClass(PersonalClaim.class);

            StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean initEconomy() {
        try {
            Class.forName("net.milkbowl.vault.economy.Economy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();

        return true;
    }

    public void loadServer() {
        String hostname = "0.0.0.0";
        int port = 23947;

        int maxConnections = 30;
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(30);
        ExecutorThreadPool pool = new ExecutorThreadPool(maxConnections, 2, queue);

        webServer = new Server(pool);
        webServer.setSessionIdManager(new DefaultSessionIdManager(webServer));

        NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(webServer);
        connector.setIdleTimeout(5000);
        connector.setAcceptQueueSize(50);
        connector.setPort(port);
        webServer.setConnectors(new Connector[]{connector});

        webServer.setStopAtShutdown(true);

        ContextHandler contextHandler = new ContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.setHandler(new RequestHandler());
        HandlerList hList = new HandlerList();
        hList.setHandlers(new org.eclipse.jetty.server.Handler[] { new SessionHandler(), contextHandler });
        webServer.setHandler(hList);
    }

    public boolean startServer() {
        try {
            if (webServer != null) {
                webServer.start();
                getLogger().info("Web server started on address 0.0.0.0:23947");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void initListeners() {
        Bukkit.getPluginManager().registerEvents(new ClaimProtectionListener(), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), instance);
        Bukkit.getPluginManager().registerEvents(new VisualizationListener(), instance);
        Bukkit.getPluginManager().registerEvents(new MenuListener(), instance);
        Bukkit.getPluginManager().registerEvents(new CommissionsListener(), instance);
    }

    public void initCommands() {
        Objects.requireNonNull(getCommand("kingdoms")).setExecutor(new KingdomsExecutor());
        Objects.requireNonNull(getCommand("kingdoms")).setTabCompleter(new KingdomsCompleter());
        Objects.requireNonNull(getCommand("claims")).setExecutor(new ClaimsExecutor());
        Objects.requireNonNull(getCommand("claims")).setTabCompleter(new ClaimsCompleter());
        Objects.requireNonNull(getCommand("areas")).setExecutor(new AreasExecutor());
        Objects.requireNonNull(getCommand("areas")).setTabCompleter(new AreasCompleter());
    }

    public void reloadLangConfig() {
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        final InputStream defConfigStream = getResource("lang.yml");
        if (defConfigStream == null) {
            return;
        }

        langConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public FileConfiguration getLangConfig() {
        return langConfig;
    }
}
