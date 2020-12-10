package analyzer.reafactoring;

import db.RefactoringDbItem;

public class MoveMethodRefactoring extends Refactoring {
    protected MoveMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // "Move Method\tprivate readIntPref(key String, defaultValue int, maxValue int) : int from class jackpal.androidterm2.Term to private readIntPref(key String, defaultValue int, maxValue int) : int from class jackpal.androidterm2.util.TermSettings----jackpal.androidterm2.Term.readIntPref(key String, defaultValue int, maxValue int)",
    @Override
    public String affectedElement() {
        var detail = dbItem.refactoringDetail;
        var targetString = substring(detail, "\t", " to ");
        var cls = substring(targetString, " from class ");
        var method = substring(targetString, " ", ") ") + ")";
        return cls + "." + method;
    }
}
