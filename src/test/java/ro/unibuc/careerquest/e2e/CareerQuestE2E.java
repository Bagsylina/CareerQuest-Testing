package ro.unibuc.careerquest.e2e;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/careerQuest.feature", tags = "E2E")
public class CareerQuestE2E {

}
