package analyzer.reafactoring;

import db.RefactoringDbItem;

public class ExtractSuperclassRefactoring extends Refactoring{
    protected ExtractSuperclassRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Extract Superclass	cyclops.control.AbstractValueTest from classes [cyclops.control.MaybeTest, cyclops.control.OptionTest]----[cyclops.control.MaybeTest, cyclops.control.OptionTest]
    @Override
    public String affectedElementRaw() {
        return substring(dbItem.refactoringDetail, " from classes ");
    }
}
