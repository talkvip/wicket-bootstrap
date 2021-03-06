package de.agilecoders.wicket.markup.html.references;

import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * TODO: document
 *
 * @author miha
 * @version 1.0
 */
public class BootstrapDatepickerReference extends CssResourceReference {
    private static final long serialVersionUID = 1L;

    /**
     * Singleton instance of this reference
     */
    public static final ResourceReference INSTANCE = new BootstrapDatepickerReference();

    /**
     * Private constructor.
     */
    private BootstrapDatepickerReference() {
        super(BootstrapDatepickerReference.class, "css/datepicker.css");
    }
}
