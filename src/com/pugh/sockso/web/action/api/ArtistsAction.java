
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.music.Artist;
import com.pugh.sockso.web.Request;

import java.util.Vector;

public class ArtistsAction extends ApiAction {

    /**
     *  Indicates if the action can handle the request
     * 
     *  @param req
     * 
     *  @return 
     * 
     */
    
    public boolean canHandle( final Request req ) {
        
        return req.getParamCount() == 2
            && req.getUrlParam( 1 ).equals( "artists" );
        
    }

    /**
     *  Shows the requested list of artists
     * 
     */
    
    public void handleRequest() {
        
//        final Vector<Artist> artists = Artist.findAll(
//            getDatabase(), getLimit(), getOffset()
//        );
        
    }
    
}
