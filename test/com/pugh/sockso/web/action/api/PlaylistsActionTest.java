
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.tests.SocksoTestCase;
import com.pugh.sockso.tests.TestDatabase;
import com.pugh.sockso.tests.TestResponse;

public class PlaylistsActionTest extends SocksoTestCase {
    
    private PlaylistsAction action;
    
    private TestResponse res;
    
    @Override
    protected void setUp() throws Exception {
        TestDatabase db = new TestDatabase();
        db.fixture( "playlists" );
        res = new TestResponse();
        action = new PlaylistsAction();
        action.setDatabase( db );
        action.setResponse( res );
        action.setRequest( getRequest("/api/playlists") );
    }
    
    public void testActionHandlesPlaylistsUrl() {
        assertTrue( action.canHandle(getRequest("/api/playlists")) );
        assertTrue( action.canHandle(getRequest("/api/playlists?offset=100")) );
    }
    
    public void testActionDoesntHandleNonPlaylistUrls() {
        assertFalse( action.canHandle(getRequest("/api/playlists/123")) );
        assertFalse( action.canHandle(getRequest("/api/albums")) );
    }
    
    public void testPlaylistsAreListedWhenRequested() throws Exception {
        action.handleRequest();
        assertContains( res.getOutput(), "A Playlist" );
        assertContains( res.getOutput(), "Bar Bar" );
        assertContains( res.getOutput(), "Foo Foo" );
    }
    
    public void testPlaylistsForUsersIncludeTheUsersInformation() throws Exception {
        action.handleRequest();
        assertContains( res.getOutput(), "MyUser" );
    }
    
}
