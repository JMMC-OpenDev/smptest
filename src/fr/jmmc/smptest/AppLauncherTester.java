/*******************************************************************************
 * JMMC project ( http://www.jmmc.fr ) - Copyright (C) CNRS.
 ******************************************************************************/
package fr.jmmc.smptest;

import fr.jmmc.jmcs.App;
import fr.jmmc.jmcs.gui.MessagePane;
import fr.jmmc.jmcs.gui.util.SwingSettings;
import fr.jmmc.jmcs.gui.util.SwingUtils;
import fr.jmmc.jmcs.gui.util.WindowCenterer;
import fr.jmmc.jmcs.network.interop.SampCapability;
import fr.jmmc.jmcs.network.interop.SampManager;
import fr.jmmc.jmcs.network.interop.SampMessageHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.astrogrid.samp.Message;

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
    public AppLauncherTester(final String[] args) {
        // Start whith no splash screen
        super(args, false, true, false);
    }

    /**
     * Initialize application objects
     * @param args ignored arguments
     *
     * @throws RuntimeException if the AppLauncherTester initialization failed
     */
    @Override
    protected void init(final String[] args) {

        // Start first the SampManager (connect to an existing hub or start a new one)
        // and check if it is connected to one Hub:
        if (!SampManager.isConnected()) {
            throw new IllegalStateException("Unable to connect to an existing hub or start an internal SAMP hub !");
        }
    }


    @Override
    protected void execute() {
    }

    /**
     * Create SAMP Message handlers
     */
    @Override
    protected void declareInteroperability() {

        // Add fake handler to allow AppLauncher JNLP startup test routine
        new SampMessageHandler(SampCapability.APPLAUNCHERTESTER_TRY_LAUNCH) {

            /**
             * Implements message processing
             *
             * @param senderId public ID of sender client
             * @param message message with MType this handler is subscribed to
             * @throws SampException if any error occurred while message processing
             */
            @Override
            protected void processMessage(final String senderId, final Message message) {
                _logger.info("Received '" + this.handledMType() + "' message from '" + senderId + "' : '" + message + "'.");
                // Using invokeAndWait to be in sync with this thread :
                // note: invokeAndWaitEDT throws an IllegalStateException if any exception occurs
                SwingUtils.invokeAndWaitEDT(new Runnable() {

                    /**
                     * Initializes the SWING components with their actions in EDT
                     */
                    @Override
                    public void run() {
                        WindowCenterer.centerOnMainScreen(App.getFrame());
                        App.getFrame().setVisible(false);
                        MessagePane.showMessage("AppLauncher installation and first run went fine !", "Congratulation !");
                        App.getFrame().setVisible(false);
                        App.quitAction().actionPerformed(null);
                    }
                });
            }
        };

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
