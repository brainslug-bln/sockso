
package com.pugh.sockso.web.action.api;

import com.pugh.sockso.tests.SocksoTestCase;
import com.pugh.sockso.tests.TestRequest;
import com.pugh.sockso.web.Request;

public class ApiActionTest extends SocksoTestCase {

    private ApiAction action;
    
    @Override
    protected void setUp() {
        action = new MyApiAction();
    }
    
    public void testGetoffsetReturnsIntValueWhenSpecified() {
        TestRequest req = new TestRequest( "GET /?offset=123 HTTP/1.1" );
        action.setRequest( req );
        assertEquals( 123, action.getOffset() );
    }
    
    public void testGetoffsetReturnsDefaultValueWhenNotSpecified() {
        TestRequest req = new TestRequest( "GET / HTTP/1.1" );
        action.setRequest( req );
        assertEquals( ApiAction.DEFAULT_OFFSET, action.getOffset() );
    }
    
    public void testGetoffsetReturnsDefaultValueWhenInvalidSpecified() {
        TestRequest req = new TestRequest( "GET /?offset=foo HTTP/1.1" );
        action.setRequest( req );
        assertEquals( ApiAction.DEFAULT_OFFSET, action.getOffset() );
    }
    
    public void testGetlimitReturnsIntValueWhenSpecified() {
        TestRequest req = new TestRequest( "GET /?limit=123 HTTP/1.1" );
        action.setRequest( req );
        assertEquals( 123, action.getLimit() );
    }
    
    public void testGetlimitReturnsDefaultValueWhenNotSpecified() {
        TestRequest req = new TestRequest( "GET / HTTP/1.1" );
        action.setRequest( req );
        assertEquals( ApiAction.DEFAULT_LIMIT, action.getLimit() );
    }
    
    public void testGetlimitReturnsDefaultValueWhenInvalidSpecified() {
        TestRequest req = new TestRequest( "GET /?limit=foo HTTP/1.1" );
        action.setRequest( req );
        assertEquals( ApiAction.DEFAULT_LIMIT, action.getLimit() );
    }
    
}

class MyApiAction extends ApiAction {
    
    public boolean canHandle( final Request req ) {
        return false;
    }
    
    public void handleRequest() {}
    
}