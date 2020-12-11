package analyzer.reafactoring;

import db.RefactoringDbItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Move Class	hudson.plugins.android_emulator.AndroidEmulator.EmulatorCommandTask
// moved to hudson.plugins.android_emulator.util.Utils.EmulatorCommandTask
public class MoveClassRefactoring extends Refactoring {

    protected MoveClassRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    @Override
    public String affectedElementRaw() {
        return substring(dbItem.refactoringDetail, "\t", " moved to ");
    }
}

