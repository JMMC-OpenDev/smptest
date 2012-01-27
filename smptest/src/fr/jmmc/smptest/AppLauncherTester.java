/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package fr.jmmc.smprun;

import fr.jmmc.jmcs.App;
import fr.jmmc.jmcs.gui.SwingSettings;
import fr.jmmc.jmcs.gui.SwingUtils;
import fr.jmmc.jmcs.gui.WindowCenterer;
import fr.jmmc.jmcs.network.interop.SampManager;
import fr.jmmc.smprun.stub.ClientStub;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.ivoa.util.runner.LocalLauncher;

/**
 * AppLauncherTester main class.
 * 
 * @author Sylvain LAFRASSE
 */
public class AppLauncherTester extends App {

    /** Logger */
    private static final Logger _logger = Logger.getLogger(AppLauncherTester.class.getName());

    /**
     * Launch the AppLauncherTester application.
     *
     * @param args command-line options.
     */
    public AppLauncher(final String[] args) {
        super(args);
    }

    /**
     * Initialize application objects
     * @param args ignored arguments
     *
     * @throws RuntimeException if the AppLauncher initialisation failed
     */
    @Override
    protected void init(final String[] args) {

        // Start first the SampManager (connect to an existing hub or start a new one)
        // and check if it is connected to one Hub:
        if (!SampManager.isConnected()) {
            throw new IllegalStateException("Unable to connect to an existing hub or start an internal SAMP hub !");
        }

        // Using invokeAndWait to be in sync with this thread :
        // note: invokeAndWaitEDT throws an IllegalStateException if any exception occurs
        SwingUtils.invokeAndWaitEDT(new Runnable() {

            /**
             * Initializes the swing components with their actions in EDT
             */
            @Override
            public void run() {
                App.setFrame(WelcomeWindow.getInstance());
            }
        });
    }

    /**
     * Execute application body = make the application frame visible
     */
    @Override
    protected void execute() {
        SwingUtils.invokeLaterEDT(new Runnable() {

            /**
             * Show the application frame using EDT
             */
            @Override
            public void run() {
                _logger.fine("AppLauncherTester.ready : handler called.");

                final JFrame frame = getFrame();

                WindowCenterer.centerOnMainScreen(frame);

                frame.setVisible(true);
            }
        });
    }

    /**
     * Main entry point
     *
     * @param args command line arguments (open file ...)
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static void main(final String[] args) {

        // init swing application for science
        SwingSettings.setup();

        final long start = System.nanoTime();
        try {
            // Start application with the command line arguments
            new AppLauncherTester(args);
        } finally {
            final long time = (System.nanoTime() - start);

            if (_logger.isLoggable(Level.INFO)) {
                _logger.info("startup : duration = " + 1e-6d * time + " ms.");
            }
        }
    }
}
/*___oOo___*/
