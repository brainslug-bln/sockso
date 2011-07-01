/*
 * Playlist.java
 * 
 * Created on May 17, 2007, 9:50:09 PM
 * 
 * Represents a playlist
 * 
 */

package com.pugh.sockso.music;

import com.pugh.sockso.Utils;
import com.pugh.sockso.db.Database;
import com.pugh.sockso.web.BadRequestException;
import com.pugh.sockso.web.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

public class Playlist extends MusicItem {
    
    private final int trackCount;
    private final User user;
    
    public Playlist( final int id, final String name ) {
        this( id, name, -1 );
    }
    
    public Playlist( final int id, final String name, final int trackCount ) {
        this( id, name, trackCount, null );
    }

    public Playlist( final int id, final String name, final int trackCount, User user ) {
        super( MusicItem.PLAYLIST, id, name );
        this.trackCount = trackCount;
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
    public int getTrackCount() {
        return trackCount;
    }
    
    /**
     *  returns the sql to select the tracks from a playlist
     * 
     *  @param playlistId the id of the playlist
     *  @return the sql
     * 
     */

    public static String getSelectTracksSql( final int playlistId, final String orderBySql ) {
                
        return Track.getSelectSql() + 
                    " from playlists p " +
                    
                        " left outer join playlist_tracks pt " +
                        " on pt.playlist_id = p.id " +
                        
                        " inner join tracks t " +
                        " on t.id = pt.track_id " +
                        
                        " inner join artists ar " +
                        " on ar.id = t.artist_id " +
                        
                        " inner join albums al " +
                        " on al.id = t.album_id " +
                        
                   " where p.id = '" + playlistId + "' " +
                   orderBySql;
        
    }
    
    /**
     *  returns a list of playlists for the user with given
     *  limit and offset for the results.
     *  
     *  @param db database object to use
     *  @param user user to fetch playlists
     *  @param limit max number of elements in the result
     *  @param offset offset for pagination
     *  @param includeSiteLists whether to include site lists in the results.
     *  @throws BadRequestException 
     *  
     */
    
    public static Vector<Playlist> getPlaylists( final Database db, final User user, int limit, int offset, boolean includeSiteLists ) throws SQLException, BadRequestException {
    	
    	final Vector<Playlist> lists = new Vector<Playlist>();
        PreparedStatement st = null;
        ResultSet rs = null;

        if ( user == null && !includeSiteLists)
            throw new BadRequestException("User is not logged in and site playlists are not selected...");

        String sql = " select p.id as playlistId, p.name as playlistName, " +
                     "        p.user_id as playlistUser, count(t.track_id) as trackCount " +
                     " from playlists p, playlist_tracks t " +
                     " where t.playlist_id = p.id and ( p.user_id = ? or p.user_id is null ) " +
                     " group by playlistId, playlistName, playlistUser " +
                     " limit ? offset ? ";
    	
    	try {
    		
            st = db.prepare( sql );
            st.setInt( 1, (user == null) ? -1 : user.getId() );
            st.setInt( 2, limit );
            st.setInt( 3, offset );

            rs = st.executeQuery();
            
            while ( rs.next() ) {
            	lists.add(createFromResultSet(rs));
            }
            
    	} catch (SQLException e) {
    		System.out.println( e.getMessage() );
    		e.printStackTrace();
    	} finally {
            Utils.close( rs );
            Utils.close( st );
    	}
    	
    	return lists;
    	
    }
    
    /**
     *  creates a new playlist from a result set row
     * 
     *  @param rs the result set
     *  @return Playlist
     *  @throws SQLException 
     * 
     */
    public static Playlist createFromResultSet(final ResultSet rs) throws SQLException {
    	return new Playlist(rs.getInt( "playlistId" ), rs.getString( "playlistName" ), rs.getInt( "trackCount" ) );
    }

    /**
     *  Finds a playlist by id, or returns null if it doesn't exist
     * 
     *  @param db
     *  @param id
     * 
     *  @return 
     * 
     */
    
    public static Playlist find( final Database db, final int id ) throws SQLException {
        
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            
            final String sql = " select id, name " +
                               " from playlists " +
                               " where id = ? ";
            
            st = db.prepare( sql );
            st.setInt( 1, id );
            rs = st.executeQuery();
            
            if ( rs.next() ) {
                return new Playlist(
                    rs.getInt( "id" ),
                    rs.getString( "name" )
                );
            }
            
        }
        
        finally {
            Utils.close( st );
            Utils.close( rs );
        }
        
        return null;
        
    }
    
}
