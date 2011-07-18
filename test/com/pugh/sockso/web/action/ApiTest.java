
package com.pugh.sockso.web.action;

import com.pugh.sockso.Constants;
import com.pugh.sockso.StringProperties;
import com.pugh.sockso.tests.SocksoTestCase;
import com.pugh.sockso.web.BadRequestException;
import com.pugh.sockso.web.Request;
import com.pugh.sockso.web.action.api.ApiAction;

import static org.easymock.EasyMock.*;

public class ApiTest extends SocksoTestCase {

    private Api api;
    
    private StringProperties p;
    
    @Override
    protected void setUp() {
        p = new StringProperties();
        p.set( Constants.WWW_USERS_REQUIRE_LOGIN, p.YES );
        api = new Api();
        api.setProperties( p );
    }
    
    public void testApiActionDoesNotRequireLoginAsItHandlesThisForItsSubActions() {
        assertFalse( api.requiresLogin() );
    }
    
    public void testRequestedActionIsRun() {
    }
    
    public void testJsonErrorReturnedWhenActionThrowsException() {
    }
    
    public void testErrorReturnedWhenActionRequiresLoginAndUserIsNotLoggedIn() throws Exception {
        ApiAction action = createNiceMock( ApiAction.class );
        expect( action.canHandle((Request)anyObject()) ).andReturn( Boolean.TRUE );
        expect( action.requiresLogin() ).andReturn( Boolean.TRUE );
        replay( action );
        //
        api.setUser( null );
        boolean gotException = false;
        try {
            api.processActions( new ApiAction[] { action } );
        }
        catch ( BadRequestException e ) { gotException = true; }
        if ( !gotException ) { fail( "Expecting exception to be thrown as user was not logged in" ); }
        verify( action );
    }
    
    public void testActionIsRunWhenUserNotLoggedInAndActionDoesNotRequireLogin() throws Exception {
        ApiAction action = createNiceMock( ApiAction.class );
        expect( action.canHandle((Request)anyObject()) ).andReturn( Boolean.TRUE );
        expect( action.requiresLogin() ).andReturn( Boolean.FALSE );
        action.handleRequest();
        replay( action );
        //
        api.setUser( null );
        api.processActions( new ApiAction[] { action } );
        verify( action );
    }
    
    public void testActionIsRunWhenActionRequiresLoginButServerDoesNot() throws Exception {
        ApiAction action = createNiceMock( ApiAction.class );
        expect( action.canHandle((Request)anyObject()) ).andReturn( Boolean.TRUE );
        action.handleRequest();
        replay( action );
        //
        p.set( Constants.WWW_USERS_REQUIRE_LOGIN, p.NO );
        api.setUser( null );
        api.processActions( new ApiAction[] { action } );
        verify( action );
    }
    
}
