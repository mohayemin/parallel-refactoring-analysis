package analyzer.reafactoring;

import db.RefactoringDbItem;

public class MoveAndRenameClassRefactoring extends Refactoring {
    protected MoveAndRenameClassRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Move And Rename Class	jackpal.androidterm.util.AndroidCompat.AndroidCharacterComp moved and renamed to jackpal.androidterm.compat.AndroidCharacterCompat----jackpal.androidterm.util.AndroidCompat.AndroidCharacterComp
    @Override
    public String affectedElement() {
        return substring(dbItem.refactoringDetail, "\t", " moved and renamed to ");
    }
}
