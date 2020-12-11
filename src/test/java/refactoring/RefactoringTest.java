package refactoring;

import analyzer.reafactoring.RefactoringFactory;
import db.RefactoringDbItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RefactoringTest {

    /*
            [x] Move Class
            [x] Rename Method
            [x] Rename Variable
            [x] Rename Parameter
            [x] Move Attribute
            [x] Extract Method
            [x] Move Method
            [x] Pull Up Method
            [x] Rename Class
            [x] Pull Up Attribute
            [x] Extract And Move Method
            [x] Extract Variable
            [x] Move And Rename Class
            [x] Push Down Method
            [x] Inline Method
            [x] Push Down Attribute
            [x] Extract Superclass
            [x] Change Package
            [ ] Move Source Folder
            [x] Extract Interface
            [ ] Parameterize Variable
     */
    //// Affected elements can be multiple
    //// Move source folder not included
    //// Parameterize Variable not included
    ////
    @ParameterizedTest
    @CsvSource(

            delimiterString = "----",
            value = {
                    "Move Class\thudson.plugins.android_emulator.AndroidEmulator.EmulatorCommandTask moved to hudson.plugins.android_emulator.util.Utils.EmulatorCommandTask----hudson.plugins.android_emulator.AndroidEmulator.EmulatorCommandTask----",
                    "Rename Method\tpublic delete(accountSid String, sid String) : AddressDeleter renamed to public deleter(accountSid String, sid String) : AddressDeleter in class com.twilio.rest.api.v2010.account.Address----com.twilio.rest.api.v2010.account.Address.delete(accountSid String, sid String)----",
                    "Rename Variable\targs : ArrayList<String> to argList : ArrayList<String> in method private createSubprocess(processId int[]) : void in class jackpal.androidterm.session.TermSession----jackpal.androidterm.session.TermSession.createSubprocess(processId int[]).args----",
                    "Rename Parameter\trouteFile : String to routeFiles : List<String> in method public setRouteFiles(routeFiles List<String>) : void in class org.resthub.web.springmvc.router.RouterHandlerMapping----org.resthub.web.springmvc.router.RouterHandlerMapping.setRouteFiles(routeFiles List<String>)----",
                    "Move Attribute\tpublic DEBUG : boolean from class jackpal.androidterm2.Term to class jackpal.androidterm2.TermDebug----jackpal.androidterm2.Term.DEBUG----",
                    "Extract Method\tprivate commonConstructor(context Context) : void extracted from public TermViewFlipper(context Context, attrs AttributeSet) in class jackpal.androidterm.TermViewFlipper----jackpal.androidterm.TermViewFlipper.TermViewFlipper(context Context, attrs AttributeSet)----",
                    "Move Method\tprivate readIntPref(key String, defaultValue int, maxValue int) : int from class jackpal.androidterm2.Term to private readIntPref(key String, defaultValue int, maxValue int) : int from class jackpal.androidterm2.util.TermSettings----jackpal.androidterm2.Term.readIntPref(key String, defaultValue int, maxValue int)----",
                    "Pull Up Method\tprotected showAboutDialog() : void from class mediathek.mac.MediathekGuiMac to protected showAboutDialog() : void from class mediathek.MediathekGui----mediathek.mac.MediathekGuiMac.showAboutDialog()----",
                    "Rename Class\tcom.oath.cyclops.reactor.adapter.FluxReactiveSeq renamed to com.oath.cyclops.reactor.adapter.FluxReactiveSeqImpl----com.oath.cyclops.reactor.adapter.FluxReactiveSeq----",
                    "Pull Up Attribute\tprotected producerNode : LinkedQueueAtomicNode<E> from class org.jctools.queues.atomic.BaseLinkedAtomicQueue to class org.jctools.queues.atomic.BaseLinkedAtomicQueueProducerNodeRef----org.jctools.queues.atomic.BaseLinkedAtomicQueue.producerNode----",
                    "Extract And Move Method\tpublic updatePrefs(settings TermSettings, metrics DisplayMetrics) : void extracted from private updatePrefs() : void in class jackpal.androidterm.Term & moved to class jackpal.androidterm.EmulatorView----jackpal.androidterm.Term.updatePrefs()----",
                    "Extract Variable\tactionBar : ActionBarCompat in method public onCreate(icicle Bundle) : void from class jackpal.androidterm.Term----jackpal.androidterm.Term.actionBar----",
                    "Move And Rename Class\tjackpal.androidterm.util.AndroidCompat.AndroidCharacterComp moved and renamed to jackpal.androidterm.compat.AndroidCharacterCompat----jackpal.androidterm.util.AndroidCompat.AndroidCharacterComp----",
                    "Push Down Method\tpublic map(s Function<? super T,? extends R>) : IO<R> from class cyclops.reactive.IO to public map(s Function<? super T,? extends R>) : IO<R> from class cyclops.reactive.IO.ReactiveSeqIO----cyclops.reactive.IO.map(s Function<? super T,? extends R>)----",
                    "Inline Method\tprivate startListening() : void inlined to public onCreate(icicle Bundle) : void in class jackpal.androidterm.Term----jackpal.androidterm.Term.startListening()----",
                    "Push Down Attribute\tprivate fn : Publisher<T> from class cyclops.reactive.IO to class cyclops.reactive.IO.ReactiveSeqIO----cyclops.reactive.IO.fn----",
                    "Extract Superclass\tcyclops.control.AbstractValueTest from classes [cyclops.control.MaybeTest, cyclops.control.OptionTest]----cyclops.control.MaybeTest----cyclops.control.OptionTest",
                    "Change Package\tjackpal.androidterm.model to jackpal.androidterm.emulatorview----jackpal.androidterm.model----",
                    "Extract Interface\tcom.aol.cyclops.sequence.streamable.ConvertableToSequenceM from classes [com.aol.cyclops.sequence.JoolWindowing, com.aol.cyclops.sequence.streamable.ToStream]----com.aol.cyclops.sequence.JoolWindowing----com.aol.cyclops.sequence.streamable.ToStream",
                    "Extract And Move Method\tpublic verifySupport(name String) : void extracted from private AMDCompressedATCTexture() in class c.org.rajawali3d.gl.extensions.texture.AMDCompressedATCTexture & moved to class c.org.rajawali3d.gl.extensions.GLExtension----c.org.rajawali3d.gl.extensions.texture.AMDCompressedATCTexture.AMDCompressedATCTexture()----",
                    "Move Method\tpublic mapValidationMessageFieldNames(validation_messages ValidationMessageBase[], from String[], to String[]) : void from class water.api.ValidationMessageBase to public mapValidationMessageFieldNames(validation_messages ValidationMessageV3[], from String[], to String[]) : void from class water.api.schemas3.ValidationMessageV3----water.api.ValidationMessageBase.mapValidationMessageFieldNames(validation_messages ValidationMessageBase[], from String[], to String[])----"
            }
    )
    public void testParsing(String refactoringDescription, String firstItem, String secondItem) {
        var type = refactoringDescription.substring(0, refactoringDescription.indexOf('\t'));
        var dbItem = new RefactoringDbItem(type, refactoringDescription);
        var refactoring = new RefactoringFactory().create(dbItem);
        var expectedList = secondItem == null ? Collections.singletonList(firstItem) : Arrays.asList(firstItem, secondItem);
        Assertions.assertTrue(expectedList.containsAll(refactoring.allAffectedElements()));
        Assertions.assertTrue(refactoring.allAffectedElements().containsAll(expectedList));
    }
}
