/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdi;

import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.openid.connect.sdk.AuthenticationRequest;
import com.nimbusds.openid.connect.sdk.Nonce;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import servlets.CallbackOid;
import singletons.GoogleOID;
import singletons.OIDInterface;


/**
 *
 * @author abraham
 */
@Named(value = "newJSFManagedBean")
@ViewScoped
public class NewJSFManagedBean implements Serializable{
    OIDInterface googleOID;

    public OIDInterface getGoogleOID() {
        return googleOID;
    }

    public void setGoogleOID(OIDInterface googleOID) {
        this.googleOID = googleOID;
    }
    
    
    
//    URI autoriceEndPoint;
//    String clientId;
//    URI redirectURI;
    public NewJSFManagedBean() throws URISyntaxException {
        googleOID = GoogleOID.getInstance();        
    }
    
    public String authUrl(OIDInterface oid) throws URISyntaxException {

        
        
        ClientID cid ;
        cid=oid.getOIDCClientInformation().getID();
        URI autoriceEndPoint = oid.getOIDCProviderMetadata().getAuthorizationEndpointURI();
        State state = new State(oid.getClass().getName()+"(("+new State()+"))");
// Generate nonce
        Nonce nonce = new Nonce();
// Specify scope
        Scope scope = Scope.parse("openid");

// Compose the request
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                autoriceEndPoint,
                new ResponseType(ResponseType.Value.CODE),
                scope, cid, new URI(oid.getRedirectURI()), state, nonce);

        URI authReqURI = authenticationRequest.toURI();
        
        ExternalContext external =FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = external.getSessionMap();
        sessionMap.put(CallbackOid.OID_AuthenticationRequest                
                , authReqURI);
        return authReqURI.toString();
        
}
    
    
//    public String authUrl(OIDInterface oid) throws URISyntaxException {
//        URI autoriceEndPoint = new URI (oid.getAutoriceEndPoint());
//        String clientId=oid.getClientId();
//        URI redirectURI=new URI(oid.getRedirectURI());
//        OIDCClientInformation c;
//        
//        ClientID cid = new ClientID(clientId);//c.getID();
//        State state = new State();
//// Generate nonce
//        Nonce nonce = new Nonce();
//// Specify scope
//        Scope scope = Scope.parse("openid");
//
//// Compose the request
//        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
//                autoriceEndPoint,
//                new ResponseType(ResponseType.Value.CODE),
//                scope, cid, redirectURI, state, nonce);
//
//        URI authReqURI = authenticationRequest.toURI();
//        
//        ExternalContext external =FacesContext.getCurrentInstance().getExternalContext();
//        Map<String, Object> sessionMap = external.getSessionMap();
//        sessionMap.put(CallbackOid.OID_AuthenticationRequest
//                
//                , authenticationRequest);
//        return authReqURI.toString();
//        
//    }
    
//    public String authUrl() {
//        OIDCClientInformation c;
//        
//        ClientID cid = new ClientID(clientId);//c.getID();
//        State state = new State();
//// Generate nonce
//        Nonce nonce = new Nonce();
//// Specify scope
//        Scope scope = Scope.parse("openid");
//
//// Compose the request
//        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
//                autoriceEndPoint,
//                new ResponseType(ResponseType.Value.CODE),
//                scope, cid, redirectURI, state, nonce);
//
//        URI authReqURI = authenticationRequest.toURI();
//        
//        ExternalContext external =FacesContext.getCurrentInstance().getExternalContext();
//        Map<String, Object> sessionMap = external.getSessionMap();
//        sessionMap.put(CallbackOid.OID_AuthenticationRequest
//                
//                , authenticationRequest);
//        return authReqURI.toString();
//    }
//    
}
