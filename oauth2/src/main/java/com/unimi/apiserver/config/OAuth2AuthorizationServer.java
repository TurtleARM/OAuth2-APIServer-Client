package com.unimi.apiserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter
{
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private final TokenStore tokenStore = new InMemoryTokenStore();

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManagerBean;

	@Autowired
	public void setAuthenticationManagerBean(AuthenticationManager authenticationManagerBean) {
		this.authenticationManagerBean = authenticationManagerBean;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("isAuthenticated()")
			.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints
				.tokenStore(this.tokenStore)
				.userDetailsService(userDetailsService)
				.authenticationManager(authenticationManagerBean)
				.tokenServices(tokenServices());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients
				.inMemory()
					.withClient("clientapp")
					.secret(passwordEncoder.encode("strongpassword"))
					.authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials", "implicit")
					.accessTokenValiditySeconds(20)
				    .refreshTokenValiditySeconds(60)
					.scopes("read_profile_info", "edit_profile_info")
					.resourceIds("user-details")
					.redirectUris("https://localhost:8444/callbackPage");
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices() {
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setTokenStore(this.tokenStore);
		return tokenServices;
	}
}
