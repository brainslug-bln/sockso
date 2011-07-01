
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.web.Request;
import com.pugh.sockso.web.action.WebAction;

abstract public class ApiAction extends WebAction {

    public static final int DEFAULT_OFFSET = 0;
    
    public static final int DEFAULT_LIMIT = 100;
    
    /**
     *  Indicates if the action can handle the specified request
     * 
     *  @param req
     * 
     *  @return 
     * 
     */
    
    public abstract boolean canHandle( final Request req );
    
    /**
     *  Return the number of results to limit by
     *
     *  @return
     *
     */

    public int getLimit() {

        return getUrlArgument( "limit", DEFAULT_LIMIT );

    }

    /**
     *  Returns the number to offset results by
     *
     *  @return
     *
     */

    public int getOffset() {

        return getUrlArgument( "offset", DEFAULT_OFFSET );
        
    }
    
    /**
     *  Returns the value of the named url argument as an integer if specified,
     *  or returns the default if it's not or invalid
     * 
     *  @param name
     *  @param defaultValue
     * 
     *  @return 
     * 
     */
    
    protected int getUrlArgument( final String name, final int defaultValue ) {
        
        try {

            if ( getRequest().hasArgument(name) ) {
                return Integer.parseInt(
                    getRequest().getArgument( name )
                );
            }

        }
        
        catch ( final NumberFormatException ignored ) {}

        return defaultValue;

    }

    /**
     *  Indicates if the given string is an integer
     * 
     *  @param integer
     * 
     *  @return 
     * 
     */
   
    protected boolean isInteger( final String integer ) {
       
        try {
            Integer.parseInt( integer );
            return true;
        }
       
        catch ( final NumberFormatException e ) {}
       
        return false;
       
    }

}
