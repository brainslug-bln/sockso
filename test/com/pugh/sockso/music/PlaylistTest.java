/*
 * PlaylistTest.java
 * 
 * Created on Aug 9, 2007, 8:44:41 PM
 * 
 * Tests the Playlist class
 * 
 */

package com.pugh.sockso.music;

import com.pugh.sockso.Utils;
import com.pugh.sockso.web.User;
import com.pugh.sockso.tests.SocksoTestCase;
import com.pugh.sockso.tests.TestDatabase;

import java.util.Vector;

public class PlaylistTest extends SocksoTestCase {

    private TestDatabase db;
    
    @Override
    public void setUp() {
        db = new TestDatabase();
    }
    
    public void testConstructor() {
        assertNotNull( new Playlist(1,"foo") );
        assertNotNull( new Playlist(1,"foo",1) );
        assertNotNull( new Playlist(1,"foo",1,null) );
    }

    public void testGetUser() {
        final User user = new User( 1, "foo" );
        final Playlist p = new Playlist( 1, "foo", 1, user );
        assertSame( user, p.getUser() );
    }
    
    public void testGetTrackCount() {
        final Playlist p = new Playlist( 1, "foo", 99 );
        assertEquals( 99, p.getTrackCount() );
    }

    public void testGetSelectTracksSql() {
        
        final int playlistId = 123;
        final String orderBySql = " where myfield = 1 ";
        final String sql = Playlist.getSelectTracksSql( playlistId, orderBySql );
        
        assertTrue( sql.matches(".*"+playlistId+".*") );
        assertTrue( sql.matches(".*"+orderBySql+".*") );
        
    }
    
    public void testFindReturnsPlaylistThatMatchesTheIdSpecified() throws Exception {
        db.fixture( "singlePlaylist" );
        Playlist playlist = Playlist.find( db, 1 );
        assertEquals( playlist.getName(), "Foo Bar" );
    }
    
    public void testFindReturnsNullWhenTheSpecifiedPlaylistDoesNotExist() throws Exception {
        assertNull( Playlist.find(db,123) );
    }
    
    public void testFindallReturnsAllPlaylists() throws Exception {
        db.fixture( "playlists" );
        Vector<Playlist> playlists = Playlist.findAll( db, 100, 0 );
        assertEquals( 3, playlists.size() );
    }
    
    public void testFindallCanBeOffset() throws Exception {
        db.fixture( "playlists" );
        Vector<Playlist> playlists = Playlist.findAll( db, 100, 1 );
        assertEquals( 2, playlists.size() );
        assertEquals( "A Playlist", playlists.get(0).getName() );
        assertEquals( "Foo Foo", playlists.get(1).getName() );
    }
    
    public void testFindallCanBeLimited() throws Exception {
        db.fixture( "playlists" );
        Vector<Playlist> playlists = Playlist.findAll( db, 2, 0 );
        assertEquals( 2, playlists.size() );
        assertEquals( "Bar Bar", playlists.get(0).getName() );
        assertEquals( "A Playlist", playlists.get(1).getName() );
    }
    
    public void testFindallReturnsNewestPlaylistsFirst() throws Exception {
        db.fixture( "playlists" );
        Vector<Playlist> playlists = Playlist.findAll( db, 100, 0 );
        assertEquals( "Bar Bar", playlists.get(0).getName() );
        assertEquals( "Foo Foo", playlists.get(2).getName() );
    }
    
    public void testFindallWithLimitOfMinusOneMeansNoLimit() throws Exception {
        db.fixture( "playlists" );
        for ( int i=0; i<200; i++ ) {
            db.update( " insert into playlists ( name, date_created, date_modified ) " +
                       " values ( '" +Utils.getRandomString(20)+ "', now(), now() )" );
        }
        Vector<Playlist> playlists = Playlist.findAll( db, -1, 0 );
        assertEquals( 203, playlists.size() );
    }
    
    public void testFindallReturnsUsersWithPlaylistsTheyHaveCreated() throws Exception {
        db.fixture( "playlists" );
        Vector<Playlist> playlists = Playlist.findAll( db, 100, 0 );
        assertEquals( "MyUser", playlists.get(1).getUser().getName() );
    }
    
    public void testFindReturnsUserWhoCreatedPlaylistWhenThereIsOne() throws Exception {
        db.fixture( "playlists" );
        Playlist playlist = Playlist.find( db, 2 );
        assertEquals( "MyUser", playlist.getUser().getName() );
    }
    
}
