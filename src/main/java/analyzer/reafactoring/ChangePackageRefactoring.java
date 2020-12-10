package analyzer.reafactoring;

import db.RefactoringDbItem;

public class ChangePackageRefactoring extends Refactoring{
    protected ChangePackageRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Change Package	jackpal.androidterm.model to jackpal.androidterm.emulatorview----jackpal.androidterm.model
    @Override
    public String affectedElementRaw() {
        return substring(dbItem.refactoringDetail, "\t", " to ");
    }
}
