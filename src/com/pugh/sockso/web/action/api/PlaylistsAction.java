
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.music.Playlist;
import com.pugh.sockso.templates.api.TApiPlaylists;
import com.pugh.sockso.web.BadRequestException;
import com.pugh.sockso.web.Request;

import java.io.IOException;

import java.sql.SQLException;

public class PlaylistsAction extends ApiAction {

    public boolean canHandle( final Request req ) {
        
        return false;
        
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
        
        final TApiPlaylists tpl = new TApiPlaylists();
        tpl.setPlaylists( Playlist.getPlaylists(getDatabase(), getUser(), getLimit(), getOffset(), true));

        getResponse().showJson( tpl.makeRenderer() );
        
    }

}
