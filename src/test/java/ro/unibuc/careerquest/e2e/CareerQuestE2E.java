package ro.unibuc.careerquest.e2e;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.jupiter.api.Tag;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@Tag("E2E")
@CucumberOptions(features = "src/test/resources/", tags = "E2E")
public class CareerQuestE2E {

}
