
package com.pugh.sockso.music;

import com.pugh.sockso.Utils;
import com.pugh.sockso.db.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Date;
import java.util.Vector;

public class Album extends MusicItem {

    private final Artist artist;
    private final int trackCount;
    private final int playCount;
    private final Date dateAdded;
    private final String year;

    public Album( final int artistId, final String artistName, final int id, final String name, final String year ) {
        this( new Artist(artistId,artistName), id, name, year );
    }
    
    public Album( final Artist artist, final int id, final String name, final String year ) {
        this( artist, id, name, year, -1 );
    }

    public Album( final Artist artist, final int id, final String name, final String year, final int trackCount ) {
        this( artist, id, name, year, null, trackCount, -1 );
    }
    
    public Album( final Artist artist, final int id, final String name, final String year, final Date dateAdded, final int trackCount, int playCount ) {
        super( MusicItem.ALBUM, id, name );
        this.artist = artist;
        this.trackCount = trackCount;
        this.playCount = playCount;
        this.dateAdded = ( dateAdded != null ) ? new Date(dateAdded.getTime()) : null;
        this.year = ( year != null ) ? year : "";
    }

    /**
     * Returns the year for this album
     *
     * @return
     */
    public String getYear() {

        if ( year == null ) {
            return "";
        }

        return ( year.length() > 4 )
            ? year.substring( 0, 4 )
            : year;

    }

    public Artist getArtist() { return artist; }
    public int getTrackCount() { return trackCount; }
    public Date getDateAdded() { return new Date(dateAdded.getTime()); }
    public int getPlayCount() { return playCount; }

    /**
     *  Find albums for the specified artist
     * 
     *  @param db
     *  @param artistId
     * 
     *  @throws SQLException
     * 
     *  @return 
     * 
     */
    
    public static Vector<Album> findByArtistId( final Database db, final int artistId ) throws SQLException {
        
        final Vector<Album> albums = new Vector<Album>();
        
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            
            final String sql = " select al.id, al.name, al.year " +
                               " from albums al " +
                               " where al.artist_id = ? ";
            
            st = db.prepare( sql );
            st.setInt( 1, artistId );
            rs = st.executeQuery();
            
            while ( rs.next() ) {
                albums.add(new Album(
                    null,
                    rs.getInt( "id" ),
                    rs.getString( "name" ),
                    rs.getString( "year" )
                ));
            }
            
            return albums;
            
        }
        
        finally {
            Utils.close( st );
            Utils.close( rs );
        }
        
    }
    
    /**
     *  Finds an album by id, returns null if not found
     * 
     *  @param db
     *  @param id
     * 
     *  @throws SQLException
     * 
     *  @return 
     * 
     */
    
    public static Album find( final Database db, final int id ) throws SQLException {
        
        PreparedStatement st = null;
        ResultSet rs = null;
        
        try {
            
            final String sql = " select al.id, al.name, al.year " +
                               " from albums al " +
                               " where al.id = ? ";
            
            st = db.prepare( sql );
            st.setInt( 1, id );
            rs = st.executeQuery();
            
            if ( rs.next() ) {
                return new Album(
                    null,
                    rs.getInt( "id" ),
                    rs.getString( "name" ),
                    rs.getString( "year" )
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
