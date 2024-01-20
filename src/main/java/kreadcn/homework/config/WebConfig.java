package kreadcn.homework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 李洪文
 * @description
 * @date 2019/12/12 9:32
 */
@Configuration
public class WebConfig implements WebMvcConfigurer, SchedulingConfigurer {
    /**
     * 跨域配置
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                .allowedHeaders("*")
                //设置允许的方法
                .allowedMethods("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = System.getProperty("user.dir") + File.separator + "frontend" + File.separator;
        File dir = new File(path);
        if (dir.exists()) {
            String currentDir = "file:" + path;
            registry
                    .addResourceHandler("/page/**")
                    .addResourceLocations(currentDir + "page" + File.separator)
                    .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)
                            .cachePrivate()
                            .mustRevalidate());
            registry
                    .addResourceHandler("/manage/**")
                    .addResourceLocations(currentDir + "manage" + File.separator)
                    .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)
                            .cachePrivate()
                            .mustRevalidate());
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // registry.setOrder(Ordered.LOWEST_PRECEDENCE);
        registerReactRoute(registry, "manage");
        registerReactRoute(registry, "page");
    }

    private void registerReactRoute(ViewControllerRegistry registry, String directoryName) {
        String regex = "/**/{spring:\\w+}";
        String forwardUrl = "forward:/" + directoryName + "/index.html";

        registry.addViewController("/" + directoryName + "/")
                .setViewName(forwardUrl);

        registry.addViewController("/" + directoryName + regex)
                .setViewName(forwardUrl);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}
