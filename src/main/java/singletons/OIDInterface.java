/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package singletons;

import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;
import java.io.Serializable;

/**
 *
 * @author abraham
 */
public interface OIDInterface extends Serializable{

    
    String getRedirectURI();
    OIDCClientMetadata getOIDCClientMetadata();    
    OIDCClientInformation getOIDCClientInformation();
    OIDCProviderMetadata getOIDCProviderMetadata();
    
}
