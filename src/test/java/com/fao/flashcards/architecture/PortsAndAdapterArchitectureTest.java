package com.fao.flashcards.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import java.util.List;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.fao.flashcards")
public class PortsAndAdapterArchitectureTest {

        private static final List<String> ALLOWED_DEPENDENCIES_SERVICE_PACKAGE = List.of(
                        "..java..",
                        "..jakarta..",
                        "..com.fao.flashcards.shared.model..",
                        "..com.fao.flashcards.shared.application.port..",
                        "org.slf4j..",
                        "org.springframework.validation.annotation..",
                        "lombok..");

        @ArchTest
        public static void ocr_servicesShouldOnlyDependOnPortsAndDomain(JavaClasses classes) {
                List<String> allowedPackages = new java.util.ArrayList<>(List.of(
                                "..ocr.application..",
                                "..ocr.model.."));
                allowedPackages.addAll(ALLOWED_DEPENDENCIES_SERVICE_PACKAGE);

                ArchRule ocr = classes()
                                .that().resideInAPackage("..ocr.application.service..")
                                .should().onlyDependOnClassesThat()
                                .resideInAnyPackage(allowedPackages.toArray(new String[0]))
                                .because(
                                                "Application services should only depend on application ports, domain, and standard libraries.");

                ocr.check(classes);

        }

        @ArchTest
        public static void cards_servicesShouldOnlyDependOnPortsAndDomain(JavaClasses classes) {
                List<String> allowedPackages = new java.util.ArrayList<>(List.of(
                                "..cards.application..",
                                "..cards.model.."));
                allowedPackages.addAll(ALLOWED_DEPENDENCIES_SERVICE_PACKAGE);

                ArchRule cards = classes()
                                .that().resideInAPackage("..cards.application.service..")
                                .should().onlyDependOnClassesThat()
                                .resideInAnyPackage(allowedPackages.toArray(new String[0]))
                                .because(
                                                "Application services should only depend on application ports, domain, and standard libraries.");

                cards.check(classes);
        }

}
