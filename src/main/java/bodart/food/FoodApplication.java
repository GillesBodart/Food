package bodart.food;

import bodart.food.health.TemplateHealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import org.eclipse.jetty.servlets.CrossOriginFilter;

public class FoodApplication extends Application<FoodConfiguration> {

    public static void main(final String[] args) throws Exception {
        new FoodApplication().run(args);
    }

    @Override
    public String getName() {
        return "Food";
    }

    @Override
    public void initialize(final Bootstrap<FoodConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final FoodConfiguration configuration,
            final Environment environment) {
        configureCors(environment);
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
                configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
    }

    private void configureCors(Environment environment) {
        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Food-token");
        filter.setInitParameter("allowCredentials", "true");
    }

}
