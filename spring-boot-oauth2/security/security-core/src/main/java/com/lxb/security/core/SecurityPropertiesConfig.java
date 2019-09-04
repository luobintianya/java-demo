
package com.wqb.security.core;

import com.wqb.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityPropertiesConfig {

}
