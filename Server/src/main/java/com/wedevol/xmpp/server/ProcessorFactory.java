package com.wedevol.xmpp.server;

import com.wedevol.xmpp.service.PayloadProcessor;
import com.wedevol.xmpp.service.impl.*;
import com.wedevol.xmpp.util.Util;

/**
 * Manages the creation of different payload processors based on the desired
 * action
 */

public class ProcessorFactory {

    public static PayloadProcessor getProcessor(String action) {
        if (action == null) {
            throw new IllegalStateException("ProcessorFactory: Action must not be null");
        }
        if (action.equals(Util.BACKEND_ACTION_REGISTER)) {
            return new RegisterProcessor();
        } else if (action.equals(Util.BACKEND_ACTION_ECHO)) {
            return new EchoProcessor();
        } else if (action.equals(Util.BACKEND_ACTION_MESSAGE)) {
            return new MessageProcessor();
        }
        else if (action.equals(Util.BACKEND_ACTION_ADDGROUP)) {
            return new AddgroupProcessor();
        }

        else if (action.equals(Util.BACKEND_ACTION_ADDUSER)) {
            return new AddUserProcessor();
        }

        else if (action.equals(Util.BACKEND_ACTION_DELUSER)) {
            return new DelUserProcessor();
        }
        else if (action.equals(Util.BACKEND_ACTION_MIDPOINT))
        {
            return new MidPointProcessor();
        }
        throw new IllegalStateException("ProcessorFactory: Action " + action + " is unknown");
    }
}