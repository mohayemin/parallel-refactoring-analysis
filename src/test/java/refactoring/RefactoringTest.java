package refactoring;

import analyzer.reafactoring.RefactoringFactory;
import db.RefactoringDbItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RefactoringTest {

    @ParameterizedTest
    @CsvSource(
            delimiterString = "----",
            value = {
                    "Move Class\thudson.plugins.android_emulator.AndroidEmulator.EmulatorCommandTask moved to hudson.plugins.android_emulator.util.Utils.EmulatorCommandTask----hudson.plugins.android_emulator.AndroidEmulator.EmulatorCommandTask",
                    "Rename Method\tpublic delete(accountSid String, sid String) : AddressDeleter renamed to public deleter(accountSid String, sid String) : AddressDeleter in class com.twilio.rest.api.v2010.account.Address----com.twilio.rest.api.v2010.account.Address.delete(accountSid String, sid String)",
                    "Rename Parameter\trouteFile : String to routeFiles : List<String> in method public setRouteFiles(routeFiles List<String>) : void in class org.resthub.web.springmvc.router.RouterHandlerMapping----org.resthub.web.springmvc.router.RouterHandlerMapping.setRouteFiles(routeFiles List<String>)",
                    "Move Attribute\tpublic DEBUG : boolean from class jackpal.androidterm2.Term to class jackpal.androidterm2.TermDebug----jackpal.androidterm2.Term.DEBUG",
            }
    )
    public void testParsing(String refactoringDescription, String expectedAffectedItem) {
        var type = refactoringDescription.substring(0, refactoringDescription.indexOf('\t'));
        var dbItem = new RefactoringDbItem(type, refactoringDescription);
        var refactoring = new RefactoringFactory().create(dbItem);
        Assertions.assertEquals(expectedAffectedItem, refactoring.affectedElement());
    }
}
