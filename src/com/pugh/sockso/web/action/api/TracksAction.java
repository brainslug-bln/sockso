
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.music.Track;
import com.pugh.sockso.templates.api.TTracks;
import com.pugh.sockso.web.Request;

import java.io.IOException;

import java.util.Vector;

public class TracksAction extends BaseApiAction {

    public void handleRequest() throws Exception {

        Vector<Track> tracks = Track.findAll(
            getDatabase(),
            getLimit(),
            getOffset()
        );
        
        showTracks( tracks );
        
    }
    
    protected void showTracks( final Vector<Track> tracks ) throws IOException {
        
        TTracks tpl = new TTracks();
        tpl.setTracks( tracks );
        
        getResponse().showJson( tpl.makeRenderer() );
        
    }

    public boolean canHandle( final Request req ) {
        
        return req.getParamCount() == 2
            && req.getUrlParam( 1 ).equals( "tracks" );
        
    }
    
}
