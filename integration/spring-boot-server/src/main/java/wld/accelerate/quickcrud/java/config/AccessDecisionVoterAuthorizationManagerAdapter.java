package wld.accelerate.quickcrud.java.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;


@Component
public class AccessDecisionVoterAuthorizationManagerAdapter implements AuthorizationManager {


    @Override
    public AuthorizationDecision check(Supplier authentication, Object object) {
        //Collection<ConfigAttribute> attributes = this.securityMetadataSource.getAttributes(object);

        Authentication authentication1 = (Authentication) authentication.get();
        if(authentication1.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANON"))) {
            return new AuthorizationDecision(true);
        } else {
            return new AuthorizationDecision(false);
        }
    }

    @Override
    public void verify(Supplier authentication, Object object) {
        AuthorizationDecision decision = check(authentication, object);
        if (decision != null && !decision.isGranted()) {
            throw new AccessDeniedException("Access Denied");
        }
    }
}
