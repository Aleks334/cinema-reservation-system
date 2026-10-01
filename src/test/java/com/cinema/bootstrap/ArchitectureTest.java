package com.cinema.bootstrap;

import com.cinema.shared.Command;
import com.cinema.shared.CommandHandler;
import com.cinema.shared.Query;
import com.cinema.shared.QueryHandler;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.cinema",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ArchitectureTest {
    @ArchTest
    static final ArchRule subdomainsMustNotDependOnBootstrap = noClasses()
            .that().resideInAnyPackage(
                    "com.cinema.catalog..",
                    "com.cinema.ticketing..",
                    "com.cinema.facility..",
                    "com.cinema.shared.."
            )
            .should().dependOnClassesThat().resideInAPackage("com.cinema.bootstrap..")
            .because("Subdomains must never depend on the composition root.");

    @ArchTest
    static final ArchRule catalogCannotAccessOtherSubdomainsInternals = noClasses()
            .that().resideInAPackage("com.cinema.catalog..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "com.cinema.ticketing.domain..",
                    "com.cinema.ticketing.application..",
                    "com.cinema.ticketing.infrastructure..",
                    "com.cinema.facility.domain..",
                    "com.cinema.facility.application..",
                    "com.cinema.facility.infrastructure.."
            )
            .because("Catalog can only access other subdomains through their public '.api' packages.");

    @ArchTest
    static final ArchRule ticketingCannotAccessOtherSubdomainsInternals = noClasses()
            .that().resideInAPackage("com.cinema.ticketing..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "com.cinema.catalog.domain..",
                    "com.cinema.catalog.application..",
                    "com.cinema.catalog.infrastructure..",
                    "com.cinema.facility.domain..",
                    "com.cinema.facility.application..",
                    "com.cinema.facility.infrastructure.."
            )
            .because("Ticketing can only access other subdomains through their public '.api' packages.");

    @ArchTest
    static final ArchRule facilityCannotAccessOtherSubdomainsInternals = noClasses()
            .that().resideInAPackage("com.cinema.facility..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "com.cinema.catalog.domain..",
                    "com.cinema.catalog.application..",
                    "com.cinema.catalog.infrastructure..",
                    "com.cinema.ticketing.domain..",
                    "com.cinema.ticketing.application..",
                    "com.cinema.ticketing.infrastructure.."
            )
            .because("Facility can only access other subdomains through their public '.api' packages.");

    @ArchTest
    static final ArchRule noCyclicDependenciesBetweenSubdomains = SlicesRuleDefinition.slices()
            .matching("com.cinema.(*)..")
            .should().beFreeOfCycles();

    @ArchTest
    static final ArchRule domainShouldNotDependOnApplicationOrInfrastructure = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage("..application..", "..infrastructure..")
            .because("Domain layer must be completely pure and independent.");

    @ArchTest
    static final ArchRule applicationShouldNotDependOnInfrastructure = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("Application layer interacts with external infrastructure strictly through ports.");

    @ArchTest
    static final ArchRule controllersMustNotDependOnDomain = noClasses()
            .that().resideInAPackage("..infrastructure.adapter.in.web..").and().haveSimpleNameNotEndingWith("ExceptionHandlerMapper")
            .should().dependOnClassesThat().resideInAPackage("..domain..")
            .because("Web controllers must only communicate with application commands, queries, and DTOs using primitive types.");

    @ArchTest
    static final ArchRule commandHandlersMustBeCorrectlyNamedAndPlaced = classes()
            .that().implement(CommandHandler.class)
            .should().haveSimpleNameEndingWith("Handler")
            .andShould().resideInAPackage("..application.command..");

    @ArchTest
    static final ArchRule commandsMustBeCorrectlyNamedAndPlaced = classes()
            .that().implement(Command.class)
            .should().haveSimpleNameEndingWith("Command")
            .andShould().resideInAPackage("..application.command..");

    @ArchTest
    static final ArchRule queryHandlersMustBeCorrectlyNamedAndPlaced = classes()
            .that().implement(QueryHandler.class)
            .should().haveSimpleNameEndingWith("Handler")
            .andShould().resideInAPackage("..application.query..");

    @ArchTest
    static final ArchRule queriesMustBeCorrectlyNamedAndPlaced = classes()
            .that().implement(Query.class)
            .should().haveSimpleNameEndingWith("Query")
            .andShould().resideInAPackage("..application.query..");
}