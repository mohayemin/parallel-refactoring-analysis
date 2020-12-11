package analyzer.reafactoring;

import db.RefactoringDbItem;

public class MoveAttributeRefactoring extends Refactoring {
    public MoveAttributeRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Move Attribute	public DEBUG : boolean from class jackpal.androidterm2.Term to class jackpal.androidterm2.TermDebug----jackpal.androidterm2.Term.DEBUG----
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var attribute = substring(detail, "\t", " : ");
        attribute = removeAccessModifier(attribute);
        var cls = substring(detail, " from class ", " to class ");
        return cls + "." + attribute;
    }
}

