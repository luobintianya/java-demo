
package com.wqb.security.browser.config;

import com.wqb.security.core.authentication.FormAuthenticationConfig;
import com.wqb.security.core.authentication.configurer.AuthorizationConfigurerManager;
import com.wqb.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.wqb.security.core.properties.SecurityProperties;
import com.wqb.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 浏览器环境下安全配置主类
 *
 * @author Shoven
 * @since 2019-05-08 21:53
 */
@Component
public class BrowserSecurityConfiguration {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private SpringSocialConfigurer springSocialConfigurer;

	@Autowired
	private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

	@Autowired
	private InvalidSessionStrategy invalidSessionStrategy;

	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private AuthorizationConfigurerManager authorizationConfigurerManager;

	@Autowired
	private FormAuthenticationConfig formAuthenticationConfig;

    public void configureWeb(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/**/favicon.ico",
                "/**/*.js",
                "/**/*.css",
                "/**/*.jpg",
                "/**/*.png",
                "/**/*.gif"
        );
    }

	public void configureHttp(HttpSecurity http) throws Exception {

		formAuthenticationConfig.configure(http);

		http.apply(validateCodeSecurityConfig)
				.and()
			.apply(smsCodeAuthenticationSecurityConfig)
				.and()
			.apply(springSocialConfigurer)
				.and()
			//记住我配置，如果想在'记住我'登录时记录日志，可以注册一个InteractiveAuthenticationSuccessEvent事件的监听器
			.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
				.userDetailsService(userDetailsService)
				.and()
			.sessionManagement()
				.invalidSessionStrategy(invalidSessionStrategy)
				.maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
				.maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
				.expiredSessionStrategy(sessionInformationExpiredStrategy)
				.and()
				.and()
			.logout()
				.logoutUrl(securityProperties.getBrowser().getSignOutUrl())
				.logoutSuccessHandler(logoutSuccessHandler)
				.deleteCookies("JSESSIONID")
				.and()
			.csrf().disable();

        authorizationConfigurerManager.config(http.authorizeRequests());
	}

	/**
	 * 记住我功能的token存取器配置
	 * @return
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

}
