package de.agilecoders.wicket.samples;

import de.agilecoders.wicket.Bootstrap;
import de.agilecoders.wicket.markup.html.themes.metro.MetroTheme;
import de.agilecoders.wicket.samples.pages.HomePage;
import de.agilecoders.wicket.settings.BootstrapSettings;
import de.agilecoders.wicket.settings.BootswatchThemeProvider;
import de.agilecoders.wicket.settings.ThemeProvider;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;
import org.apache.wicket.util.time.Duration;
import org.wicketstuff.annotation.scan.AnnotatedMountScanner;

import java.io.IOException;
import java.util.Properties;

/**
 * Demo Application instance.
 */
public class WicketApplication extends WebApplication {
    private Properties properties;

    /**
     * Get Application for current thread.
     *
     * @return The current thread's Application
     */
    public static WicketApplication get() {
        return (WicketApplication) Application.get();
    }

    /**
     * Constructor.
     */
    public WicketApplication() {
        super();

        properties = loadProperties();
        setConfigurationType(RuntimeConfigurationType.valueOf(properties.getProperty("configuration.type")));
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.Application#init()
     */
    @Override
    public void init() {
        super.init();

        // wicket markup leads to strange ui problems because css selectors
        // won't match anymore.
        getMarkupSettings().setStripWicketTags(true);

        // deactivate ajax debug mode
        getDebugSettings().setAjaxDebugModeEnabled(false);

        // Allow fonts.
        IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
        if (packageResourceGuard instanceof SecurePackageResourceGuard) {
            SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
            guard.addPattern("+*.woff");
            guard.addPattern("+*.ttf");
            guard.addPattern("+*.svg");
        }

        getResourceSettings().setDefaultCacheDuration(Duration.milliseconds(0));
        getResourceSettings().setResourcePollFrequency(Duration.seconds(2));
        getResourceSettings().setCachingStrategy(NoOpResourceCachingStrategy.INSTANCE);

        configureBootstrap();

        new AnnotatedMountScanner().scanPackage("de.agilecoders.wicket.samples.pages").mount(this);
    }

    private void configureBootstrap() {
        BootstrapSettings settings = new BootstrapSettings();
        settings.minify(true) // use minimized version of all bootstrap references
                .useJqueryPP(true)
                .useModernizr(true)
                .useResponsiveCss(true)
                .getBootstrapLessCompilerSettings().setUseLessCompiler(true);

        ThemeProvider themeProvider = new BootswatchThemeProvider() {{
            add(new MetroTheme());
            defaultTheme("wicket");
        }};
        settings.setThemeProvider(themeProvider);

        Bootstrap.install(this, settings);
    }

    public Properties getProperties() {
        return properties;
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
