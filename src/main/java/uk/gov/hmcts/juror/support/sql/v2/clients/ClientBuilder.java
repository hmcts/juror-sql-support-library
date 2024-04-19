package uk.gov.hmcts.juror.support.sql.v2.clients;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.SneakyThrows;
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
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Add this class to your spring project to create a list of client classes based on your controller methods
 */
@Configuration
@Slf4j
public class ClientBuilder {


    private static final String BASE_LOCATION =
        "/Users/benedwards/Desktop/Projects/juror/GitHub/juror-sql-support-library/src/main/java/";
    private static final String BASE_CONTROLLER_PACKAGE = "uk.gov.hmcts.juror.api.moj.controller";
    private static final String GENERATED_LOCATION = "uk.gov.hmcts.juror.support.sql.v2.generated";
    private static final String BASE_CLIENT_PACKAGE = GENERATED_LOCATION + ".clients";
    private static final Map<String, String> BASE_CLIENT_DTO_PACKAGE = Map.of(
        "uk.gov.hmcts.juror.api.moj.controller", GENERATED_LOCATION + ".dto",
        "uk.gov.hmcts.juror.api.moj.domain", GENERATED_LOCATION + "uk.gov.hmcts.juror.support.sql.v2.generated.domain"
    );
    Map<Class<?>, List<Map.Entry<RequestMappingInfo, HandlerMethod>>> fileMap = new HashMap<>();
    Set<Class<?>> classesToGenerate = new HashSet<>();

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
        createDataClasses(classesToGenerate);
    }

    private void createDataClasses(Set<Class<?>> classesToGenerate) {
        log.info("Creating data classes");
        Set<Class<?>> newClasses = new HashSet<>();
        for (Class<?> clazz : classesToGenerate) {
            log.info("Generating: " + clazz.getName());
            String packageName = typeToDto(clazz.getName()).substring(0, typeToDto(clazz.getName()).lastIndexOf("."));
            File mainDirectory = new File(BASE_LOCATION + packageToFile(packageName));
            if (!mainDirectory.exists()) {
                mainDirectory.mkdirs();
            }
            File file = new File(mainDirectory,
                clazz.getSimpleName() + ".java");
            if (file.exists()) {
                file.delete();

            }
            try {
                file.createNewFile();
                newClasses.addAll(writeClassDtoToFile(file, clazz));
            } catch (Exception e) {
                log.error("Error creating file", e);
            }
        }
        newClasses.removeAll(classesToGenerate);
        if (!newClasses.isEmpty()) {
            createDataClasses(newClasses);
        }
    }

    @SneakyThrows
    private Set<Class<?>> writeClassDtoToFile(File file, Class<?> clazz) {
        log.info("Writing file: " + file.getAbsoluteFile());
        Set<Class<?>> newClasses = new HashSet<>();
        try (FileWriter fileWriter = new FileWriter(file);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            String packageName = typeToDto(clazz.getName()).substring(0, typeToDto(clazz.getName()).lastIndexOf("."));
            printWriter.println("package " + packageName + ";");
            printWriter.println();
            printWriter.println("import com.fasterxml.jackson.annotation.JsonFormat;");
            printWriter.println("import com.fasterxml.jackson.annotation.JsonIgnore;");
            printWriter.println("import com.fasterxml.jackson.annotation.JsonProperty;");
            printWriter.println("import lombok.AllArgsConstructor;");
            printWriter.println("import lombok.Builder;");
            printWriter.println("import lombok.Data;");
            printWriter.println("import lombok.NoArgsConstructor;");
            printWriter.println();
            newClasses.addAll(writeClassString(printWriter, clazz));
        }
        return newClasses;
    }

    private Collection<Class<?>> writeClassString(PrintWriter printWriter, Class<?> clazz) {
        Set<Class<?>> newClasses = new HashSet<>();
        if (clazz.isEnum()) {
            writeEnumString(printWriter, clazz);
            return newClasses;
        }

        printWriter.println("@AllArgsConstructor");
        printWriter.println("@NoArgsConstructor");
        printWriter.println("@Data");
        printWriter.println("@Builder");
        printWriter.println(
            "public " + (Modifier.isStatic(clazz.getModifiers()) ? "static " : "") + "class " + clazz.getSimpleName() +
                " {");
        printWriter.println();
        for (Field field : clazz.getDeclaredFields()) {
            log.info("Adding Field: " + field.getName() + " " + field.getType().getName());

            if (field.isAnnotationPresent(JsonIgnore.class)) {
                JsonIgnore jsonIgnore = field.getAnnotation(JsonIgnore.class);
                if (jsonIgnore.value()) {
                    continue;
                }
            }
            if (field.isAnnotationPresent(JsonFormat.class)) {
                JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                printWriter.println("    @JsonFormat(pattern = \"" + jsonFormat.pattern() + "\")");
            }

            if (field.isAnnotationPresent(JsonProperty.class)) {
                JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
                printWriter.println("    @JsonProperty(\"" + jsonProperty.value() + "\")");
            }

            printWriter.println(
                "    private " +
                    typeToDto(getTypeString(newClasses, field).replaceAll("[A-Za-z0-9.]*\\$", "")) + " "
                    + field.getName() +
                    ";");
        }
        newClasses.stream()
            .filter(aClass -> aClass.getName().contains("$"))
            .forEach(aClass -> newClasses.addAll(writeClassString(printWriter, aClass)));

        newClasses.removeIf(aClass -> aClass.getName().contains("$"));
        printWriter.println("}");
        return newClasses;
    }

    private void writeEnumString(PrintWriter printWriter, Class<?> clazz) {
        printWriter.println("public enum " + clazz.getSimpleName() + " {");
        for (Object enumConstants : clazz.getEnumConstants()) {
            printWriter.println("    " + enumConstants + ",");
        }
        printWriter.println("}");
    }

    private String getTypeString(Set<Class<?>> newClasses, Field field) {
        Type fieldType = field.getGenericType();
        if (fieldType instanceof ParameterizedType parameterizedType) {
            Type[] arguments = parameterizedType.getActualTypeArguments();
            String[] typeNames = new String[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                typeNames[i] = typeToDto(arguments[i].getTypeName());
                addToClassesToGenerate(newClasses, arguments[i]);
            }
            return typeToDto(parameterizedType.getRawType().getTypeName()) + "<" + String.join(", ", typeNames) + ">";
        }
        addToClassesToGenerate(newClasses, fieldType);
        return fieldType.getTypeName();
    }


    private void createFiles(Map<Class<?>, List<Map.Entry<RequestMappingInfo, HandlerMethod>>> fileMap) {
        String clientPackageDir = BASE_LOCATION + packageToFile(BASE_CLIENT_PACKAGE) + "/";
        File mainDirectory = new File(clientPackageDir);
        if (!mainDirectory.exists()) {
            mainDirectory.mkdirs();
//            deleteDirectory(mainDirectory);
        }

        fileMap.forEach((key, value) -> {
            //TODO TMP
            if (!key.getName().contains("DisqualifyJurorController333")) {
                return;
            }
            File file = new File(mainDirectory,
                packageToFile(key.getName().replace(BASE_CONTROLLER_PACKAGE, "")) + "Client.java");
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                writeToFile(file, key, value);
            } catch (Exception e) {
                log.error("Error creating file", e);
            }

        });
    }

    @SneakyThrows
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
            printWriter.println("import org.springframework.core.ParameterizedTypeReference;");
            printWriter.println("import org.springframework.http.HttpMethod;");
            printWriter.println("import uk.gov.hmcts.juror.support.sql.v2.clients.BaseClient;");
            printWriter.println("import uk.gov.hmcts.juror.support.sql.v2.support.JwtDetails;");
            printWriter.println();
            printWriter.println("import java.util.Map;");
            printWriter.println();
            printWriter.println("public class " + key.getSimpleName() + "Client extends BaseClient {");
            printWriter.println();
            printWriter.println("    public " + key.getSimpleName() + "Client() {");
            printWriter.println("        super(uk.gov.hmcts.juror.support.sql.v2.DataCreator.restTemplateBuilder);");
            printWriter.println("    }");
            printWriter.println();
            for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : value) {
                printWriter.println(getMethodString(entry));
                printWriter.println();
            }
            printWriter.println("}");
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
        if (returnType.getTypeName().equals("?")) {
            returnType = Object.class;
        }
        addToClassesToGenerate(classesToGenerate, returnType);

        String returnTypeString = typeToDto(returnType.getTypeName());
        StringBuilder builder = new StringBuilder();

        boolean isVoid = returnTypeString.equals("void");
        if (isVoid) {
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
//            if (parameter.getType().isAssignableFrom(BureauJwtPayload.class)
//                || parameter.getType().isAssignableFrom(BureauJwtAuthentication.class)) {
//                continue;
//            }
            addToClassesToGenerate(classesToGenerate, parameter.getParameterizedType());
            params.add(typeToDto(parameter.getType().getName()) + " " + parameter.getName());
            if (parameter.isAnnotationPresent(PathVariable.class)) {
                PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
                pathParams.put(getFirstNoneBlank(pathVariable.value(), pathVariable.name(), parameter.getName()),
                    "String.valueOf(" + parameter.getName() + ")");
            }
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                queryParams.put(
                    getFirstNoneBlank(requestParam.value(), requestParam.name(), parameter.getName()),
                    "String.valueOf(" + parameter.getName() + ")");
            }
            if (parameter.isAnnotationPresent(RequestBody.class)) {
                payloadName = parameter.getName();
            }
        }
        builder.append(String.join(",\n", params));
        builder.append(") {\n");

        if (isVoid) {
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
            for (Map.Entry<String, String> pathParam : pathParams.entrySet()) {
                builder.append("\"").append(pathParam.getKey()).append("\", ")
                    .append(pathParam.getValue()).append(",\n");
            }
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("),\n");
        builder.append("            Map.of(");
        if (!queryParams.isEmpty()) {
            for (Map.Entry<String, String> queryParam : queryParams.entrySet()) {
                builder.append("\"").append(queryParam.getKey()).append("\", ")
                    .append(queryParam.getValue()).append(",\n");
            }
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("),\n");
        builder.append("            jwtDetails");
        if (!isVoid) {
            builder.append(",\n            new ParameterizedTypeReference<>() {}\n");
        }
        builder.append("        );\n");


        builder.append("}");
        return builder.toString();
    }

    @SneakyThrows
    private void addToClassesToGenerate(Set<Class<?>> classesToGenerate, Type returnType) {
        if (returnType.getTypeName().equals("void")
            || !returnType.getTypeName().startsWith("uk.gov.hmcts.juror")
            || returnType.getClass().isPrimitive()) {
            return;
        }
        if (returnType instanceof ParameterizedType returnTypeParamType) {
            for (Type type : returnTypeParamType.getActualTypeArguments()) {
                addToClassesToGenerate(classesToGenerate, type);
            }
            return;
        }
        if (returnType.getTypeName().endsWith("[]")) {
            returnType = Class.forName(returnType.getTypeName().replace("[]", ""));
        }
        if (returnType.getTypeName().endsWith("<>")) {
            returnType = Class.forName(returnType.getTypeName().replace("<>", ""));
        }
        log.info("Adding to classes to generate: " + returnType.getTypeName());
        classesToGenerate.add(Class.forName(returnType.getTypeName()));

    }

    private String getFirstNoneBlank(String... values) {
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return "";
    }


    private String packageToFile(String packageName) {
        return packageName.replace(".", "/");
    }

    private String typeToDto(final String typeName) {
        if (!typeName.startsWith("uk.gov.hmcts.juror")) {
            return typeName;
        }
//        for (Map.Entry<String, String> dtoPackage : BASE_CLIENT_DTO_PACKAGE.entrySet()) {
//            paramTypeName = paramTypeName.replace(dtoPackage.getKey(), dtoPackage.getValue());
//        }
        return typeName.replace("uk.gov.hmcts.juror", GENERATED_LOCATION);
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
