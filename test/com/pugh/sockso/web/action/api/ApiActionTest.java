
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.tests.SocksoTestCase;
import com.pugh.sockso.web.Request;

public class ApiActionTest extends SocksoTestCase {

    private ApiAction action;
    
    @Override
    protected void setUp() {
        action = new MyApiAction();
    }
    
    public void testGetoffsetReturnsIntValueWhenSpecified() {
        action.setRequest(getRequest( "/?offset=123" ));
        assertEquals( 123, action.getOffset() );
    }
    
    public void testGetoffsetReturnsDefaultValueWhenNotSpecified() {
        action.setRequest(getRequest( "/" ));
        assertEquals( ApiAction.DEFAULT_OFFSET, action.getOffset() );
    }
    
    public void testGetoffsetReturnsDefaultValueWhenInvalidSpecified() {
        action.setRequest(getRequest( "/?offset=foo" ));
        assertEquals( ApiAction.DEFAULT_OFFSET, action.getOffset() );
    }
    
    public void testGetlimitReturnsIntValueWhenSpecified() {
        action.setRequest(getRequest( "/?limit=123" ));
        assertEquals( 123, action.getLimit() );
    }
    
    public void testGetlimitReturnsDefaultValueWhenNotSpecified() {
        action.setRequest(getRequest( "/" ));
        assertEquals( ApiAction.DEFAULT_LIMIT, action.getLimit() );
    }
    
    public void testGetlimitReturnsDefaultValueWhenInvalidSpecified() {
        action.setRequest(getRequest( "/?limit=foo" ));
        assertEquals( ApiAction.DEFAULT_LIMIT, action.getLimit() );
    }
    
}

class MyApiAction extends ApiAction {
    
    public boolean canHandle( final Request req ) {
        return false;
    }
    
    public void handleRequest() {}
    
}
