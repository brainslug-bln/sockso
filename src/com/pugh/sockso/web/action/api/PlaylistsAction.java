
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.music.Playlist;
import com.pugh.sockso.templates.api.TApiPlaylists;
import com.pugh.sockso.web.BadRequestException;
import com.pugh.sockso.web.Request;

import java.io.IOException;

import java.sql.SQLException;

import java.util.Vector;

public class PlaylistsAction extends BaseApiAction {

    /**
     *  Indicates if this action can handle the request
     * 
     *  @param req
     * 
     *  @return 
     * 
     */
    
    public boolean canHandle( final Request req ) {
        
        return req.getParamCount() == 2
            && req.getUrlParam( 1 ).equals( "playlists" );
        
    }
    
    /**
     *  Shows a list of playlists
     * 
     *  @throws SQLException
     *  @throws IOException
     *  @throws BadRequestException 
     * 
     */
    
    public void handleRequest() throws SQLException, IOException, BadRequestException {
        
        final Vector<Playlist> playlists = Playlist.findAll(
            getDatabase(),
            getLimit(),
            getOffset()
        );
        
        showPlaylists( playlists );
        
    }
    
    /**
     *  Shows the playlists as JSON
     * 
     *  @param playlists
     * 
     *  @throws IOException 
     * 
     */
    
    protected void showPlaylists( final Vector<Playlist> playlists ) throws IOException {
        
        final TApiPlaylists tpl = new TApiPlaylists();
        tpl.setPlaylists( playlists );

        getResponse().showJson( tpl.makeRenderer() );
        
    }

}
