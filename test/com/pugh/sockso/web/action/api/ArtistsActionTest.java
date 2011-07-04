
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.tests.SocksoTestCase;
import com.pugh.sockso.tests.TestDatabase;
import com.pugh.sockso.tests.TestResponse;

public class ArtistsActionTest extends SocksoTestCase {

    private ArtistsAction action;
    
    private TestResponse res;

    @Override
    protected void setUp() throws Exception {
        res = new TestResponse();
        TestDatabase db = new TestDatabase();
        db.fixture( "artistsAlbumsAndTracks" );
        action = new ArtistsAction();
        action.setDatabase( db );
        action.setResponse( res );
    }
    
    public void testActionCanHandleArtistsUrl() {
        assertTrue( action.canHandle(getRequest( "/api/artists")) );
        assertTrue( action.canHandle(getRequest( "/api/artists?foo=bar")) );
    }
    
    public void testActionDoesntHandleNonArtistsUrls() {
        assertFalse( action.canHandle(getRequest( "/api/artists/123")) );
        assertFalse( action.canHandle(getRequest( "/api/artists/123/tracks")) );
        assertFalse( action.canHandle(getRequest( "/api/albums")) );
    }
    
    public void testArtistsListed() {
        action.setRequest(getRequest( "/api/artists" ));
        action.handleRequest();
        assertContains( res.getOutput(), "A Artist" );
        assertContains( res.getOutput(), "Empty Artist" );
    }
    
    public void testArtistListCanBeOffset() {
        action.setRequest(getRequest( "/api/artists?offset=1" ));
        action.handleRequest();
        assertNotContains( res.getOutput(), "A Artist" );
        assertContains( res.getOutput(), "Empty Artist" );
    }
    
    public void testArtistListCanBeLimited() {
        action.setRequest(getRequest( "/api/artists?limit=1" ));
        action.handleRequest();
        assertContains( res.getOutput(), "A Artist" );
        assertNotContains( res.getOutput(), "Empty Artist" );
    }
    
}
