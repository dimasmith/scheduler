package scheduler.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import scheduler.config.root.DevelopmentConfiguration;
import scheduler.config.root.QuartzConfig;
import scheduler.config.root.RootContextConfig;
import scheduler.config.root.SecurityConfig;
import scheduler.config.servlet.RequestListener;
import scheduler.config.servlet.ServletContextConfig;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{
				RootContextConfig.class,
				DevelopmentConfiguration.class,
				SecurityConfig.class,
				QuartzConfig.class
		};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{ServletContextConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);

		servletContext.addListener(new SessionListener());
		servletContext.addListener(new RequestListener());
	}

	@Override
	protected Filter[] getServletFilters() {

		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);

		return new Filter[]{characterEncodingFilter};
	}
}
