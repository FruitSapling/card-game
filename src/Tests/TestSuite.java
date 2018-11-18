import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CardGameTest.class,
        CardTest.class,
        DeckTest.class,
        PlayerTest.class
})

public class TestSuite {
}
