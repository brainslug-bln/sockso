
package com.pugh.sockso.web.action;

import com.pugh.sockso.web.BadRequestException;
import com.pugh.sockso.web.Request;
import com.pugh.sockso.web.action.api.ApiAction;
import com.pugh.sockso.web.action.api.ArtistTracks;
import com.pugh.sockso.web.action.api.PlaylistsAction;
import com.pugh.sockso.web.action.api.RootAction;
import com.pugh.sockso.web.action.api.TrackAction;

import org.apache.log4j.Logger;

import java.io.IOException;

import java.sql.SQLException;

/**
 *  End point for Sockso Web API methods.
 * 
 */
public class Api extends WebAction {

    private static final Logger log = Logger.getLogger( Api.class );

    /**
     *  Run through API actions looking for one to handle the request
     *
     *  @throws BadRequestException
     *  @throws IOException
     *  @throws SQLException
     *
     */

    @Override
    public void handleRequest() throws BadRequestException {

        final Request req = getRequest();
        
        for ( final ApiAction action : getApiActions() ) {

            if ( action.canHandle(req) ) {
                
                log.debug( "Run API action: " +action.getClass().getName() );
            
                action.init( getRequest(), getResponse(), getUser(), getLocale() );
                action.setDatabase( getDatabase() );
                action.setProperties( getProperties() );
                
                try {
                    action.handleRequest();
                }
                
                catch ( final Exception e ) {
                    log.debug( "API Exception: " +e.getMessage() );
                    throw new BadRequestException( e.getMessage() );
                }
                
                return;
                
            }

        }

        throw new BadRequestException( "Unknown API Action" );

    }

    /**
     *  Returns an array of all the api actions
     * 
     *  @return
     *
     */
    protected ApiAction[] getApiActions() {

        return new ApiAction[] {
            new RootAction(),
            new PlaylistsAction(),
            new TrackAction(),
            new ArtistTracks()
        };

    }

}
