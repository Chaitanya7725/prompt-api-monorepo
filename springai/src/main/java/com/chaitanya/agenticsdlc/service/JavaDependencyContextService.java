package com.chaitanya.agenticsdlc.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.springframework.stereotype.Service;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JavaDependencyContextService {

    private static final String PROJECT_PACKAGE_PREFIX =
            "com.chaitanya.agenticsdlc.";

    private static final Path MAIN_SOURCE_ROOT = Path.of(
            "src/main/java"
    ).toAbsolutePath().normalize();

    public String extractProjectDependencyContext(String productionSource) {
        CompilationUnit unit = StaticJavaParser.parse(productionSource);

        List<String> importedProjectClasses = unit.getImports()
                .stream()
                .filter(importDeclaration -> !importDeclaration.isAsterisk())
                .map(ImportDeclaration::getNameAsString)
                .filter(name -> name.startsWith(PROJECT_PACKAGE_PREFIX))
                .toList();

        if (importedProjectClasses.isEmpty()) {
            return "No project-source dependencies were imported.";
        }

        return importedProjectClasses.stream()
                .map(this::readDependencyPublicApi)
                .collect(Collectors.joining("\n\n"));
    }

    private String readDependencyPublicApi(String qualifiedClassName) {
        Path sourceFile = MAIN_SOURCE_ROOT.resolve(
                qualifiedClassName.replace(".", "/") + ".java"
        ).normalize();

        if (!sourceFile.startsWith(MAIN_SOURCE_ROOT) || !Files.exists(sourceFile)) {
            return "Dependency source not found: " + qualifiedClassName;
        }

        try {
            CompilationUnit dependencyUnit = StaticJavaParser.parse(sourceFile);

            Optional<TypeDeclaration<?>> type = dependencyUnit.getTypes()
                    .stream()
                    .findFirst();

            if (type.isEmpty()) {
                return "No declared type found in: " + qualifiedClassName;
            }

            String packageName = dependencyUnit.getPackageDeclaration()
                    .map(packageDeclaration -> packageDeclaration.getNameAsString())
                    .orElse("");

//            String constructors = type.get().getConstructors()
//                    .stream()
//                    .filter(ConstructorDeclaration::isPublic)
//                    .map(constructor -> constructor.getDeclarationAsString(false, false, false))
//                    .collect(Collectors.joining("\n"));

            String methods = type.get().getMethods()
                    .stream()
                    .filter(MethodDeclaration::isPublic)
                    .map(method -> method.getDeclarationAsString(false, false, false))
                    .collect(Collectors.joining("\n"));

            TypeDeclaration<?> declaredType = type.get();

            String typeKind;

            if (declaredType.isRecordDeclaration()) {
                typeKind = "record";
            } else if (declaredType.isClassOrInterfaceDeclaration()) {
                ClassOrInterfaceDeclaration classOrInterface =
                        declaredType.asClassOrInterfaceDeclaration();

                typeKind = classOrInterface.isInterface()
                        ? "interface"
                        : "class";
            } else {
                typeKind = "type";
            }

            String constructors;

            if (declaredType.isRecordDeclaration()) {
                RecordDeclaration record = declaredType.asRecordDeclaration();

                String parameters = record.getParameters()
                        .stream()
                        .map(parameter -> parameter.getTypeAsString()
                                + " "
                                + parameter.getNameAsString())
                        .collect(Collectors.joining(", "));

                constructors = record.getNameAsString()
                        + "("
                        + parameters
                        + ")";
            } else {
                constructors = declaredType.getConstructors()
                        .stream()
                        .filter(ConstructorDeclaration::isPublic)
                        .map(constructor ->
                                constructor.getDeclarationAsString(false, false, false)
                        )
                        .collect(Collectors.joining("\n"));
            }

            return """
                    Project dependency API:
                    Package: %s
                    Type: %s %s

                    Public constructors:
                    %s

                    Public methods:
                    %s
                    """.formatted(
                    packageName,
                    typeKind,
                    type.get().getNameAsString(),
                    constructors.isBlank() ? "None" : constructors,
                    methods.isBlank() ? "None" : methods
            );

        } catch (Exception exception) {
            return "Could not parse dependency: "
                    + qualifiedClassName
                    + ". Reason: "
                    + exception.getMessage();
        }
    }
}