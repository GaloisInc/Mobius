package bmllib;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 *
 * @author Tomasz Batkiewicz (tb209231@students.mimuw.edu.pl)
 * @version a-01
 */
public class Activator extends Plugin {

  /**
   *  The BMLLib plug-in ID.
   */
  public static final String PLUGIN_ID = "BmlLib";

  /**
   *  The shared plugin instance.
   */
  private static Activator plugin;

  /**
   * The constructor.
   */
  public Activator() {
  }

  /**
   * Returns the shared instance.
   *
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.eclipse.core.runtime.Plugins#start(org.osgi.
   *                                             framework.BundleContext)
   */
  @Override
  public void start(final BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(final BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

}
