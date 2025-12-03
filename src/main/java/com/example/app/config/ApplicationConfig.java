package com.example.app.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.app.login.AdminAuthFilter;
import com.example.app.login.EmployeeAuthFilter;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {

	// バリデーションメッセージのカスタマイズ
	@Override
	public Validator getValidator() {
		var validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

    @Bean
    MessageSource messageSource() {
		var messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("validation");
		return messageSource;
	}

    // 認証用フィルタの有効化
    @Bean
    FilterRegistrationBean<AdminAuthFilter> adminAuthFilter() {
 		var bean = new FilterRegistrationBean<AdminAuthFilter>(new AdminAuthFilter());
 		bean.addUrlPatterns("/admin/tool/*");
 		bean.addUrlPatterns("/admin/employee/*");
 		return bean;
 	}

    @Bean
    FilterRegistrationBean<EmployeeAuthFilter> employeeAuthFilter() {
 		var bean = new FilterRegistrationBean<EmployeeAuthFilter>(new EmployeeAuthFilter());
 		bean.addUrlPatterns("/");
 		bean.addUrlPatterns("/rental/*");
 		bean.addUrlPatterns("/shipping/*");
 		return bean;
 	}

}
