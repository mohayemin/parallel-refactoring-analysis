import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Pair;

import java.util.List;

public class RefactoringParserTest {
    public String pa(String refactoringDetail) {
        var parts = refactoringDetail.replace('\t', ' ').split(" ");
        var attributeName = parts[3];
        var className = parts[8];
        return className + "." + attributeName;
    }

    @Test
    public void pullAttribute() {
        Assertions.assertEquals("jackpal.androidterm2.Term.DEBUG", pa("Move Attribute\tpublic DEBUG : boolean from class jackpal.androidterm2.Term to class jackpal.androidterm2.TermDebug"));
        Assertions.assertEquals("jackpal.androidterm2.Term.LOG_IME", pa("Move Attribute\tpublic LOG_IME : boolean from class jackpal.androidterm2.Term to class jackpal.androidterm2.TermDebug"));
        Assertions.assertEquals("com.twilio.sdk.auth.ConversationGrant.configuration_profile_sid", pa("Move Attribute\tpublic configuration_profile_sid : String from class com.twilio.sdk.auth.ConversationGrant to class com.twilio.sdk.auth.ConversationGrant.Payload"));
        Assertions.assertEquals("com.twilio.sdk.auth.ConversationGrant.configuration_profile_sid", pa("Move Attribute\tpublic configuration_profile_sid : String from class com.twilio.sdk.auth.ConversationGrant to class com.twilio.sdk.auth.ConversationGrant.Payload"));
    }
}
