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
        var start = dbItem.refactoringDetail.indexOf('\t') + 1;
        var end = dbItem.refactoringDetail.indexOf(" moved");
        return dbItem.refactoringDetail.substring(start, end);
    }
}

