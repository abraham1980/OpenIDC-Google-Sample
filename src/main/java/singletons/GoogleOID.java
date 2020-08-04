package singletons;

import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;
import java.net.URI;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author abraham
 */
public class GoogleOID implements OIDInterface {
    private static GoogleOID singleton;
    private String redirectURI= new String("http://localhost:8080/openid-google-sameple/callback-oid");
    private String autoriceEndPoint =new String( "https://accounts.google.com/o/oauth2/v2/auth");
    
    //Paste here the well kwon google oidconnect from:
    // https://accounts.google.com/.well-known/openid-configuration
    private String providerInfo="{\n" +
" \"issuer\": \"https://accounts.google.com\",\n" +
" \"authorization_endpoint\": \"https://accounts.google.com/o/oauth2/v2/auth\",\n" +
" \"device_authorization_endpoint\": \"https://oauth2.googleapis.com/device/code\",\n" +
" \"token_endpoint\": \"https://oauth2.googleapis.com/token\",\n" +
" \"userinfo_endpoint\": \"https://openidconnect.googleapis.com/v1/userinfo\",\n" +
" \"revocation_endpoint\": \"https://oauth2.googleapis.com/revoke\",\n" +
" \"jwks_uri\": \"https://www.googleapis.com/oauth2/v3/certs\",\n" +
" \"response_types_supported\": [\n" +
"  \"code\",\n" +
"  \"token\",\n" +
"  \"id_token\",\n" +
"  \"code token\",\n" +
"  \"code id_token\",\n" +
"  \"token id_token\",\n" +
"  \"code token id_token\",\n" +
"  \"none\"\n" +
" ],\n" +
" \"subject_types_supported\": [\n" +
"  \"public\"\n" +
" ],\n" +
" \"id_token_signing_alg_values_supported\": [\n" +
"  \"RS256\"\n" +
" ],\n" +
" \"scopes_supported\": [\n" +
"  \"openid\",\n" +
"  \"email\",\n" +
"  \"profile\"\n" +
" ],\n" +
" \"token_endpoint_auth_methods_supported\": [\n" +
"  \"client_secret_post\",\n" +
"  \"client_secret_basic\"\n" +
" ],\n" +
" \"claims_supported\": [\n" +
"  \"aud\",\n" +
"  \"email\",\n" +
"  \"email_verified\",\n" +
"  \"exp\",\n" +
"  \"family_name\",\n" +
"  \"given_name\",\n" +
"  \"iat\",\n" +
"  \"iss\",\n" +
"  \"locale\",\n" +
"  \"name\",\n" +
"  \"picture\",\n" +
"  \"sub\"\n" +
" ],\n" +
" \"code_challenge_methods_supported\": [\n" +
"  \"plain\",\n" +
"  \"S256\"\n" +
" ],\n" +
" \"grant_types_supported\": [\n" +
"  \"authorization_code\",\n" +
"  \"refresh_token\",\n" +
"  \"urn:ietf:params:oauth:grant-type:device_code\",\n" +
"  \"urn:ietf:params:oauth:grant-type:jwt-bearer\"\n" +
" ]\n" +
"}";
    
    //// https://console.developers.google.com/apis/credentials
    // Create your proyect and credentiials in google console developer and paste here 
    // your  json configurtion file from google console developer
    // may be you need to supress the tag "web {.....}" to obtain somethin like:
    
    String clientInformation="{\n" +
"   \"client_id\":\"XXXXXXXXXXXXX.apps.googleusercontent.com\",\n" +
"   \"project_id\":\"YYYYYYYYYYYYYYY\",\n" +
"   \"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\n" +
"   \"token_uri\":\"https://oauth2.googleapis.com/token\",\n" +
"   \"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\n" +
"   \"client_secret\":\"ZZZZZZZZZZZZZZZZZZZZZZZZZ\",\n" +
"   \"redirect_uris\":[\n" +
"      \"http://localhost:8080/openid-google-sameple/callback-oid\",\n" +
"   ]\n" +
"}";
    private OIDCClientMetadata oIDCClientMetadata;
    private OIDCClientInformation oIDCClientInformation ;
    private OIDCProviderMetadata oIDCProviderMetadata ;
    
    private GoogleOID() {
    }

    public static GoogleOID getInstance() {
        if (singleton==null) {
            singleton=new GoogleOID();
            try {
                singleton.oIDCProviderMetadata = OIDCProviderMetadata.parse(singleton.providerInfo);
                singleton.oIDCClientMetadata = OIDCClientMetadata.parse(JSONObjectUtils.parse(singleton.clientInformation));
                singleton.oIDCClientInformation = OIDCClientInformation.parse(JSONObjectUtils.parse(singleton.clientInformation));
                
            } catch (Exception ex) {
                //Logger.getLogger(GoogleOID.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return singleton;
    }
    
    

    

    @Override
    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    @Override
    public OIDCClientMetadata getOIDCClientMetadata() {
        return oIDCClientMetadata;
    }
    
    @Override
    public OIDCClientInformation getOIDCClientInformation() {
        return oIDCClientInformation;
    }

    @Override
    public OIDCProviderMetadata getOIDCProviderMetadata() {
        return oIDCProviderMetadata;
    }
}
