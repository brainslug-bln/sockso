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
     *  Returns a list of playlists for the user with given limit and offset for the results.
     *  
     *  @param db database object to use
     *  @param limit max number of elements in the result
     *  @param offset offset for pagination
     * 
     *  @throws SQLException
     *  
     */
    
    public static Vector<Playlist> findAll( final Database db, int limit, int offset ) throws SQLException {
    	
        PreparedStatement st = null;
        ResultSet rs = null;

    	try {

            final Vector<Playlist> lists = new Vector<Playlist>();
            String sql = " select p.id, p.name, u.id as userId, u.name as userName " +
                         " from playlists p " +
                            " left outer join users u " +
                            " on u.id = p.user_id " +
                         " order by p.id desc ";
            
            if ( limit != -1 ) {
                sql += " limit " +limit+
                       " offset " +offset;
            }

            st = db.prepare( sql );
            rs = st.executeQuery();
            
            while ( rs.next() ) {
                final User user = rs.getString( "userId" ) != null
                    ? new User( rs.getInt("userId"), rs.getString("userName") )
                    : null;
            	lists.add(new Playlist(
                    rs.getInt( "id" ),
                    rs.getString( "name" ),
                    -1,
                    user
                ));
            }
            
            return lists;
    	
    	}
        
        finally {
            Utils.close( rs );
            Utils.close( st );
    	}
    	
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
