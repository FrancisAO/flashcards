package com.fao.flashcards.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.fao.flashcards")
public class PortsAndAdapterArchitectureTest {

    @ArchTest
    public static void servicesShouldOnlyDependOnPortsAndDomain(JavaClasses classes) {

        ArchRule ocr = classes()
                .that().resideInAPackage("..ocr.application.service..")
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        "..java..", "..jakarta..", "..ocr.application.port..", "..ocr.domain..", "org.slf4j..",
                        "lombok..")
                .because(
                        "Application services should only depend on application ports, domain, and standard libraries.");

        ocr.check(classes);

    }

}
