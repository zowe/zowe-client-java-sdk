package utility;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilDatasetTest {

    @Test
    public void checkValidDatasetName() {
        List<String> validDatasetNames = Arrays.asList(
                "DSNAME.NOTEXIST",
                "#DSNAME.NOTEXIST",
                "@DSNAME.NOTEXIST",
                "$DSNAME.NOTEXIST",
                "DS#NAME.NOTEXIST",
                "DS@NAME.NOTEXIST",
                "DS$NAME.NOTEXIST",
                "DSNAME.#NOEXIST",
                "DSNAME.@NOEXIST",
                "DSNAME.$NOEXIST",
                "DSNAME.NO$EXIST",
                "DSNAME.NO@EXIST",
                "DSNAME.NO#EXIST",
                "DSNAME.NO-EXIST",
                "DSNAME.LLQ1.#NOEXIST",
                "DSNAME.LLQ1.@NOEXIST",
                "DSNAME.LLQ1.$NOEXIST",
                "DSNAME.LLQ1.NO$EXIST",
                "DSNAME.LLQ1.NO@EXIST",
                "DSNAME.LLQ1.NO#EXIST",
                "DSNAME.LLQ1.NO-EXIST"
        );

        for (String validDatasetName : validDatasetNames) {
            assertDoesNotThrow(() -> UtilDataset.checkDatasetName(validDatasetName, true));
        }

        assertDoesNotThrow(() -> UtilDataset.checkDatasetName("DSNAME", false));

    }

    @Test
    public void checkInvalidDatasetName() {
        List<String> invalidDatasetNames = Arrays.asList(
                "DSNAME.ITSTOOLONG.DS",
                "DSNAME",
                "DSNAME..LLQ.DS",
                "DSNAME.LLQ.",
                "DSNAME.DS%^&.DS",
                "DSNAME.VERYLONG.MORE.THAN.FORTY.FOUR.CHARS.LONG.INVALID",
                "DSNAME.0123.DS"
        );

        for (String invalidDatasetName : invalidDatasetNames) {
            Exception exception = assertThrows(Exception.class,
                    () -> UtilDataset.checkDatasetName(invalidDatasetName, true));

            assertTrue(invalidDatasetName,
                    exception.getMessage().contains("Invalid data set name '" + invalidDatasetName + "'"));
        }
    }

}