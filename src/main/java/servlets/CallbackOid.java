package servlets;

import com.nimbusds.oauth2.sdk.AccessTokenResponse;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.AuthorizationCodeGrant;
import com.nimbusds.oauth2.sdk.ErrorObject;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.SerializeException;
import com.nimbusds.oauth2.sdk.TokenErrorResponse;
import com.nimbusds.oauth2.sdk.TokenRequest;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.nimbusds.oauth2.sdk.auth.ClientSecretBasic;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.nimbusds.oauth2.sdk.id.State;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.openid.connect.sdk.AuthenticationErrorResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponse;
import com.nimbusds.openid.connect.sdk.AuthenticationResponseParser;
import com.nimbusds.openid.connect.sdk.AuthenticationSuccessResponse;
import com.nimbusds.openid.connect.sdk.OIDCTokenResponseParser;
import com.nimbusds.openid.connect.sdk.UserInfoErrorResponse;
import com.nimbusds.openid.connect.sdk.UserInfoRequest;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import com.nimbusds.openid.connect.sdk.UserInfoSuccessResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.minidev.json.JSONObject;
import singletons.GoogleOID;
import singletons.OIDInterface;
@WebServlet(name = "CallbackOid", urlPatterns = {"/callback-oid"},loadOnStartup = 1)
public class CallbackOid extends HttpServlet {

    public static String OID_AuthenticationRequest="OID_AuthenticationRequest";

    public CallbackOid() {
        super();
        System.out.println("CallbackOid creado");
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CallbackOid</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CallbackOid at " + request.getContextPath() + "</h1>");
            
            for (String k :request.getParameterMap().keySet()
                    ) {
                String v = request.getParameter(k);
                out.println("<p>");
                out.println(k+": " +v);
                out.println("</p>");
            }
                    
            out.println("<p>");
            AuthorizationCode authcode= authCode(request,out);
            out.println("AUTH: " +authcode);
            out.println("</p>");
            OIDInterface oidi = requestedProvider(requestedState(request));
            AccessTokenResponse accesTokenResponse = getToken(oidi, authcode);
            out.println("<p>getAccessToken(): "+accesTokenResponse.getTokens().getAccessToken()+"</p>");
            out.println("<p>toOIDCTokens().getAccessToken(): "+accesTokenResponse.getTokens().toOIDCTokens().getAccessToken()+"</p>");
            out.println("<p>toOIDCTokens().getIDToken() 1: "+accesTokenResponse.getTokens().toOIDCTokens().getIDToken()+"</p>");
            out.println("<p>toOIDCTokens().getIDToken() 2: "+accesTokenResponse.getTokens().toOIDCTokens().getIDToken().getParsedString()+"</p>");
            out.println("<p>toOIDCTokens().getIDToken() 3: "+accesTokenResponse.getTokens().toOIDCTokens().getIDToken().serialize()+"</p>");
            out.println("<p>toOIDCTokens().getIDToken() 4: "+accesTokenResponse.getTokens().toOIDCTokens().getIDToken().getHeader()+"</p>");
            try {
                out.println("<p>toOIDCTokens().getIDToken() 5: "+accesTokenResponse.getTokens().toOIDCTokens().getIDToken().getJWTClaimsSet()+"</p>");
                out.println("<p>toOIDCTokens().getIDToken() 6: "+accesTokenResponse.getTokens().toOIDCTokens().getIDToken().getParsedParts()+"</p>");
            } catch (java.text.ParseException ex) {
                Logger.getLogger(CallbackOid.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            out.println("<p>toOIDCTokens().getRefreshToken(): "+accesTokenResponse.getTokens().toOIDCTokens().getRefreshToken()+"</p>");
            
            
            out.println("<p> getUserInfo(oidi, accesTokenResponse): "+getUserInfo(oidi, accesTokenResponse)+"</p>");
            
            out.println("</body>");
            out.println("</html>");
        }
        
    }
    public AuthorizationCode authCode(HttpServletRequest req, PrintWriter out) throws ServletException {
        AuthenticationResponse authResp = null;
        try {
            
            String url = ((HttpServletRequest)req).getRequestURL().toString();
            String queryString = ((HttpServletRequest)req).getQueryString();
            String uri =url+"?"+queryString;
            URI uri1 = new URI(uri);
            
            out.append("<p>URL 1: "+uri+"</p>");
            
            authResp = AuthenticationResponseParser.parse(uri1);
            if(authResp == null) {
                throw new ServletException("No se ha obtennido ningun valor al parsear la URI");
            }
            
            if (authResp instanceof AuthenticationErrorResponse) {
                ErrorObject error = ((AuthenticationErrorResponse) authResp)
                        .getErrorObject();
                // TODO error handling
                String msgerr=error.getCode()+" "+error.getDescription()+" "+error.getHTTPStatusCode()+" "+error.getURI();
                System.out.println(msgerr);
                throw new ServletException(msgerr);
            }
            State reqState = requestedState (req);
            out.append("<p>State solicitado: "+reqState+"</p>");
            out.append("<p>State recibido: "+authResp.getState()+"</p>");
            if (!reqState.equals(authResp.getState())) {
                throw new ServletException("Fallo de seguridad el state  requerido ("+reqState+") no coincide con el state recibido ("+authResp.getState()+")");
            }            
            AuthenticationSuccessResponse successResponse = (AuthenticationSuccessResponse) authResp;
            successResponse.getSessionState();
            AuthorizationCode authCode = successResponse.getAuthorizationCode();
            return authCode;
        }catch(ServletException e){
            throw e;
        } catch (ParseException  | URISyntaxException ex) {
            throw new ServletException(ex);
        }         
    }

    public AccessTokenResponse getToken(OIDInterface oidprovider, AuthorizationCode authCode) throws ServletException{
        
        try {
            
            TokenRequest tokenReq = new TokenRequest(
                    oidprovider.getOIDCProviderMetadata().getTokenEndpointURI(),
                    new ClientSecretBasic(oidprovider.getOIDCClientInformation().getID(),
                            oidprovider.getOIDCClientInformation().getSecret()),
                    new AuthorizationCodeGrant(authCode, new URI(oidprovider.getRedirectURI())));
            
            HTTPResponse tokenHTTPResp = null;
            try {
                tokenHTTPResp = tokenReq.toHTTPRequest().send();
            } catch (SerializeException | IOException e) {
                throw new ServletException(e);
            }
            
            // Parse and check response
            TokenResponse tokenResponse = null;
            try {
                tokenResponse = OIDCTokenResponseParser.parse(tokenHTTPResp);
            } catch (ParseException e) {
                throw new ServletException(e);
            }

            if (tokenResponse instanceof TokenErrorResponse) {
                ErrorObject error = ((TokenErrorResponse) tokenResponse).getErrorObject();
                String msgerr=error.getCode()+" "+error.getDescription()+" "+error.getHTTPStatusCode()+" "+error.getURI();
                System.out.println(msgerr);
                throw new ServletException(msgerr);
            }
            
            
            

            AccessTokenResponse accessTokenResponse = (AccessTokenResponse) tokenResponse;
            
            accessTokenResponse.getTokens().getAccessToken();
            accessTokenResponse.getTokens().toOIDCTokens().getAccessToken();
            accessTokenResponse.getTokens().toOIDCTokens().getIDToken();
            
            
            
            
            return accessTokenResponse;


        } catch (URISyntaxException ex) {
            throw new ServletException(ex);
        }
    }
    
    public JSONObject getUserInfo(OIDInterface oidprovider,AccessTokenResponse accessTokenResponse) throws ServletException {        
        UserInfoRequest userInfoReq = new UserInfoRequest(
                oidprovider.getOIDCProviderMetadata().getUserInfoEndpointURI(),
                (BearerAccessToken) accessTokenResponse.getTokens().getBearerAccessToken());
        HTTPResponse userInfoHTTPResp = null;
        try {
            userInfoHTTPResp = userInfoReq.toHTTPRequest().send();
        } catch (SerializeException | IOException e) {
            throw new ServletException(e);
        }
        UserInfoResponse userInfoResponse = null;
        try {
            userInfoResponse = UserInfoResponse.parse(userInfoHTTPResp);
        } catch (ParseException e) {
            throw new ServletException(e);
        }
        if (userInfoResponse instanceof UserInfoErrorResponse) {
            ErrorObject error = ((UserInfoErrorResponse) userInfoResponse).getErrorObject();
            String msgerr=error.getCode()+" "+error.getDescription()+" "+error.getHTTPStatusCode()+" "+error.getURI();
                System.out.println(msgerr);
                throw new ServletException(msgerr);
        }
        UserInfoSuccessResponse successResponse = (UserInfoSuccessResponse) userInfoResponse;
        JSONObject claims = successResponse.getUserInfo().toJSONObject();
        return claims;
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private State requestedState(HttpServletRequest req)  throws ServletException {
        try {
            return AuthenticationResponseParser.parse((URI) req.getSession().getAttribute(OID_AuthenticationRequest)).getState();
        } catch (ParseException ex) {
            throw new ServletException(ex);
        }
    }

    private OIDInterface requestedProvider(State requestedState) throws ServletException {
        //"smiles".substring(1, 5) returns "mile"
        String s = requestedState.toString();
        if (s.contains(GoogleOID.class.getSimpleName()))
            return GoogleOID.getInstance();
        throw new ServletException("No se ha reconocido un provedor de OID para el State: " + requestedState);
        
    }
}
