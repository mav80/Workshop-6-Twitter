package pl.coderslab.app;

import java.util.Locale;

import javax.persistence.EntityManagerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.FormatterRegistry;
//import org.springframework.format.FormatterRegistry;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import javax.validation.Validator;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "pl.coderslab")
@EnableJpaRepositories(basePackages = "pl.coderslab.repositories")
@EnableTransactionManagement
public class AppConfig  implements WebMvcConfigurer {
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
	@Bean
	public LocalEntityManagerFactoryBean entityManagerFactory() {
	LocalEntityManagerFactoryBean emfb = new LocalEntityManagerFactoryBean();
	emfb.setPersistenceUnitName("twitterPersistenceUnit");
	return emfb; }
	
	
	
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
	JpaTransactionManager tm = new JpaTransactionManager(emf);
	return tm; }
	
//	//@Override //tutaj dodajemy nasze konwertery
//	public void addFormatters(FormatterRegistry registry) {
//	registry.addConverter(getPublisherConverter());
//	registry.addConverter(getBookAuthorConverter());
//	}
//	
//	@Bean
//	public PublisherConverter getPublisherConverter() {
//		return new PublisherConverter();
//	}
//	
//	@Bean
//	public	BookAuthorConverter	getBookAuthorConverter()	{
//					return	new	BookAuthorConverter();
//	}
	
	
	//beany do dzien 3 walidacja
	
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
	registry.addConverter(getTweetConverter());
	}
	
	
	@Bean
	public TweetConverter getTweetConverter() {
		return new TweetConverter();
	}
	
	@Bean(name="localeResolver")
	public LocaleContextResolver getLocaleContextResolver() {
	SessionLocaleResolver localeResolver = new SessionLocaleResolver();
	localeResolver.setDefaultLocale(new Locale("pl","PL"));
	return localeResolver; }
	
	
	@Bean
	public Validator validator() {
	return new LocalValidatorFactoryBean();
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver getCommonsMultipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    multipartResolver.setMaxUploadSize(1048576);   // 1MB
	    multipartResolver.setMaxInMemorySize(1048576);  // 1MB
	    return multipartResolver;
	}
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/resources/").setCachePeriod(49556926);
	}
	
	
	
	
}