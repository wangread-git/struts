package org.apache.struts2.util;

import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.TextProvider;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * Helper methods to access text from TextProviders
 */
public class TextProviderHelper {

    private static final Logger LOG = LoggerFactory.getLogger(TextProviderHelper.class);

    /**
     * <p>Get a message from the first TextProvider encountered in the stack.
     * If the first TextProvider doesn't provide the message the default message is returned.</p>
     * <p>The search for a TextProvider is iterative from the root of the stack.</p>
     * <p>This method was refactored from  {@link org.apache.struts2.components.Text} to use a
     * consistent implementation across UIBean components.</p>
     * @param key             the message key in the resource bundle
     * @param defaultMessage  the message to return if not found (evaluated for OGNL)
     * @param args            an array args to be used in a {@link java.text.MessageFormat} message
     * @param stack           the value stack to use for finding the text
       *
     * @return the message if found, otherwise the defaultMessage
     */
    public static String getText(String key, String defaultMessage, List<String> args, ValueStack stack) {
        String msg = null;
        TextProvider tp = null;

        for (Iterator iterator = stack.getRoot().iterator(); iterator.hasNext();) {
            Object o = iterator.next();

            if (o instanceof TextProvider) {
                tp = (TextProvider) o;
                msg = tp.getText(key, null, args, stack);

                break;
            }
        }

        if (msg == null) {
            // evaluate the defaultMesage as an OGNL expression
            msg = stack.findString(defaultMessage);
            if (msg == null) {
                // use the defaultMessage literal value
                msg = defaultMessage;
            }

            if (LOG.isWarnEnabled()) {
                if (tp != null) {
                    LOG.warn("The first TextProvider in the ValueStack ("+tp.getClass().getName()+") could not locate the message resource with key '"+key+"'");
                } else {
                    LOG.warn("Could not locate the message resource '"+key+"' as there is no TextProvider in the ValueStack.");
                }
                if (msg.equals(defaultMessage)) {
                    LOG.warn("The default value expression '"+defaultMessage+"' was evaluated and did not match a property.  The literal value '"+defaultMessage+"' will be used.");
                } else {
                    LOG.warn("The default value expression '"+defaultMessage+"' evaluated to '"+msg+"'");
                }
            }
        }
        return msg;
    }

    /**
     * <p>Get a message from the first TextProvider encountered in the stack.
     * If the first TextProvider doesn't provide the message the default message is returned.</p>
     * <p>The search for a TextProvider is iterative from the root of the stack.</p>
     * <p>This method was refactored from  {@link org.apache.struts2.components.Text} to use a
     * consistent implementation across UIBean components.</p>
     * @param key             the message key in the resource bundle
     * @param defaultMessage  the message to return if not found
     * @param stack           the value stack to use for finding the text
       *
     * @return the message if found, otherwise the defaultMessage
     */
    public static String getText(String key, String defaultMessage,ValueStack stack) {
        return getText(key, defaultMessage, new LinkedList<String>(), stack);
    }
}