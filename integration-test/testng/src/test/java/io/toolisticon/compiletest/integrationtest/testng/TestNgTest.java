package io.toolisticon.compiletest.integrationtest.testng;

import io.toolisticon.compiletesting.CompileTestBuilder;
import io.toolisticon.compiletesting.UnitTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Test class to test testng extension.
 */
public class TestNgTest {

    @Test
    public void warningMessageTest() {

        CompileTestBuilder

                .unitTest()
                .defineTest(new UnitTest() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment, Element typeElement) {
                        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.WARNING, "WARNING!");
                    }
                })
                .expectWarningMessagesThatContain("WARNING!")
                .compilationShouldSucceed()
                .executeTest();


    }

    @Test
    public void successfullFailingCompilationTest_ByErrorMessage() {

        CompileTestBuilder
                .unitTest()
                .defineTest(new UnitTest() {
                    @Override
                    public void unitTest(ProcessingEnvironment processingEnvironment, Element typeElement) {
                        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "ERROR!");
                    }
                })
                .expectErrorMessagesThatContain("ERROR!")
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void failingCompilationTest_ByErrorMessage() {

        try {
            CompileTestBuilder
                    .unitTest()
                    .defineTest(new UnitTest() {
                        @Override
                        public void unitTest(ProcessingEnvironment processingEnvironment, Element typeElement) {
                            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "ERROR!");
                        }
                    })
                    .compilationShouldSucceed()
                    .executeTest();

            Assert.fail("Should have failed");
        } catch (AssertionError error) {
            MatcherAssert.assertThat(error.getMessage(), Matchers.containsString("Compilation should have succeeded but failed"));
        }

    }

}
