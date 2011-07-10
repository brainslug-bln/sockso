
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.templates.api.TApiServerInfo;
import com.pugh.sockso.web.Request;

import java.io.IOException;

public class RootAction extends BaseApiAction {

    /**
     *  Handles requests to /
     * 
     *  @param req
     * 
     *  @return 
     * 
     */
    
    public boolean canHandle( final Request req ) {
        
        return req.getParamCount() == 1;
        
    }
    
    /**
     *  Outputs server info
     * 
     *  @throws IOException 
     * 
     */
    
    public void handleRequest() throws IOException {

        final TApiServerInfo tpl = new TApiServerInfo();
        tpl.setProperties( getProperties() );

        getResponse().showJson( tpl.makeRenderer() );
        
    }

}
