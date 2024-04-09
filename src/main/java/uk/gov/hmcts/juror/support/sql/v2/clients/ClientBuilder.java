package uk.gov.hmcts.juror.support.sql.v2.clients;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Add this class to your spring project to create a list of client classes based on your controller methods
 */
//@Configuration
@Slf4j
public class ClientBuilder {


    private static final String BASE_LOCATION =
        "/Users/benedwards/Desktop/Projects/juror/GitHub/juror-sql-support-library/src/main/java/";
    private static final String BASE_CONTROLLER_PACKAGE = "uk.gov.hmcts.juror.api.moj.controller";
    private static final String BASE_CLIENT_PACKAGE = "uk.gov.hmcts.juror.support.sql.v2.clients.generated";
    private static final Map<String, String> BASE_CLIENT_DTO_PACKAGE = Map.of(
        "uk.gov.hmcts.juror.api.moj.controller", "uk.gov.hmcts.juror.support.sql.v2.clients.dto",
        "uk.gov.hmcts.juror.api.moj.domain", "uk.gov.hmcts.juror.support.sql.v2.clients.domain"
    );
    Map<Class<?>, List<Map.Entry<RequestMappingInfo, HandlerMethod>>> fileMap = new HashMap<>();

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext
            .getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        requestMappingHandlerMapping.getHandlerMethods()
            .entrySet()
            .stream()
            .filter(requestMappingInfoHandlerMethodEntry -> {
                HandlerMethod value = requestMappingInfoHandlerMethodEntry.getValue();
                return value.getMethod().getDeclaringClass().getName().startsWith(BASE_CONTROLLER_PACKAGE);
            })
            .forEach(requestMappingInfoHandlerMethodEntry -> {
                HandlerMethod value = requestMappingInfoHandlerMethodEntry.getValue();
                Class<?> declaringClass = value.getMethod().getDeclaringClass();
                if (!fileMap.containsKey(declaringClass)) {
                    fileMap.put(declaringClass, new ArrayList<>());
                }
                fileMap.get(declaringClass).add(requestMappingInfoHandlerMethodEntry);
            });

        createFiles(fileMap);
    }


    private void createFiles(Map<Class<?>, List<Map.Entry<RequestMappingInfo, HandlerMethod>>> fileMap) {
        String clientPackageDir = BASE_LOCATION + packageToFile(BASE_CLIENT_PACKAGE) + "/";
        log.info("TMP: clientPackageDir {}", clientPackageDir);
        File mainDirectory = new File(clientPackageDir);
        if (mainDirectory.exists()) {
            deleteDirectory(mainDirectory);
        }
        mainDirectory.mkdirs();

        fileMap.forEach((key, value) -> {
            //TMP
            if (!key.getName().contains("AdministrationController")) {
                return;
            }
            File file = new File(mainDirectory,
                packageToFile(key.getName().replace(BASE_CONTROLLER_PACKAGE, "")) + "Client.java");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    writeToFile(file, key, value);
                } catch (Exception e) {
                    log.error("Error creating file", e);
                }
            }
        });
    }

    private void writeToFile(File file, Class<?> key, List<Map.Entry<RequestMappingInfo, HandlerMethod>> value) {
        try (FileWriter fileWriter = new FileWriter(file);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            String className = key.getSimpleName();
            printWriter.println(
                "package " + BASE_CLIENT_PACKAGE + key.getName().replace(BASE_CONTROLLER_PACKAGE, "")
                    .replace("." + className, "") + ";");
            printWriter.println();
            printWriter.println("import org.springframework.beans.factory.annotation.Autowired;");
            printWriter.println("import org.springframework.boot.web.client.RestTemplateBuilder;");
            printWriter.println("import org.springframework.http.HttpMethod;");
            printWriter.println("import org.springframework.stereotype.Component;");
            printWriter.println("import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;");
            printWriter.println("import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;");
            printWriter.println();
            printWriter.println("import java.util.Map;");
            printWriter.println();
            printWriter.println("@Component");
            printWriter.println("public class " + key.getSimpleName() + "Client extends BaseClient {");
            printWriter.println();
            printWriter.println("    @Autowired");
            printWriter.println("    public " + key.getSimpleName() + "Client(RestTemplateBuilder "
                + "restTemplateBuilder) {");
            printWriter.println("        super(restTemplateBuilder);");
            printWriter.println("    }");
            printWriter.println();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : value) {
                printWriter.println(getMethodString(entry));
                printWriter.println();
            }
            printWriter.println("}");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMethodString(Map.Entry<RequestMappingInfo, HandlerMethod> entry) {
        HandlerMethod handlerMethod = entry.getValue();
        Type returnType = handlerMethod.getMethod().getGenericReturnType();

        if (returnType instanceof ParameterizedType returnTypeParamType
            && ResponseEntity.class.isAssignableFrom(
            (Class<?>) returnTypeParamType.getRawType())) {
            returnType = ((ParameterizedType) returnType).getActualTypeArguments()[0];
        }
        String returnTypeString = typeToDto(returnType.getTypeName());
        StringBuilder builder = new StringBuilder();

        if (returnTypeString.equals(Void.class.getName())) {
            builder.append("    public void ");
        } else {
            builder.append("    public ").append(returnTypeString).append(" ");
        }
        builder.append(handlerMethod.getMethod().getName()).append("(");
        List<String> params = new ArrayList<>();
        params.add("JwtDetails jwtDetails");
        Map<String, String> pathParams = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        String payloadName = "null";
        for (Parameter parameter : handlerMethod.getMethod().getParameters()) {

            params.add(typeToDto(parameter.getType().getName()) + " " + parameter.getName());
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                pathParams.put(
                    StringUtils.isBlank(pathVariable.name()) ?
                        pathVariable.value() : pathVariable.name(),
                    "String.valueOf(" + parameter.getName() + ")");
            }
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                queryParams.put(
                    StringUtils.isBlank(requestParam.name()) ?
                        requestParam.value() : requestParam.name(),
                    "String.valueOf(" + parameter.getName() + ")");
            }
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                payloadName = parameter.getName();
            }
        }
        builder.append(String.join(",\n", params));
        builder.append(") {\n");
        if (returnTypeString.equals(Void.class.getName())) {
            builder.append("        triggerApiVoidReturn(\n");
        } else {
            builder.append("        return triggerApi(\n");
        }
        builder.append("            HttpMethod.")
            .append(entry.getKey().getMethodsCondition().getMethods().iterator().next().name()).append(",\n");
        builder.append("            \"")
            .append(entry.getKey().getPathPatternsCondition().getPatterns().iterator().next().getPatternString())
            .append("\",\n");
        builder.append("            ").append(payloadName).append(",\n");
        builder.append("            Map.of(");
        if (!pathParams.isEmpty()) {
            builder.append("\"").append(pathParams.keySet().iterator().next()).append("\", ")
                .append(pathParams.values().iterator().next());
        }
        builder.append("),\n");
        builder.append("            Map.of(");
        if (!queryParams.isEmpty()) {
            builder.append("\"").append(queryParams.keySet().iterator().next()).append("\", ")
                .append(queryParams.values().iterator().next());
        }
        builder.append("),\n");
        builder.append("            jwtDetails,\n");
        builder.append("            new ParameterizedTypeReference<>() {}\n");
        builder.append("        );\n");


        builder.append("}");
        return builder.toString();
    }


    private String packageToFile(String packageName) {
        return packageName.replace(".", "/");
    }

    private String typeToDto(final String typeName) {
        String paramTypeName = typeName;
        for (Map.Entry<String, String> dtoPackage : BASE_CLIENT_DTO_PACKAGE.entrySet()) {
            paramTypeName = paramTypeName.replace(dtoPackage.getKey(), dtoPackage.getValue());
        }
        return paramTypeName;
    }


    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
