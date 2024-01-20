package kreadcn.homework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig implements OpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        openApi.info(new Info().title("ProSearch API"))
                .getPaths()
                .forEach((s, pathItem) ->
                        pathItem.readOperations()
                                .forEach(operation ->
                                        operation.setTags(operation.getTags()
                                                .stream()
                                                .map(t -> t.replace("-controller", ""))
                                                .toList())));
    }
}
