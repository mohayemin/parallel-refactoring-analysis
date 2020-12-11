package analyzer.reafactoring;

import db.RefactoringDbItem;

public class MoveMethodRefactoring extends Refactoring {
    protected MoveMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // "Move Method\tprivate readIntPref(key String, defaultValue int, maxValue int) : int from class jackpal.androidterm2.Term to private readIntPref(key String, defaultValue int, maxValue int) : int from class jackpal.androidterm2.util.TermSettings----jackpal.androidterm2.Term.readIntPref(key String, defaultValue int, maxValue int)",
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var method = substring(detail, "\t", " from class ");
        method = removeAccessModifier(method);
        method = substring(method, "", ")") + ")";
        var cls = substring(detail, " from class ");
        cls = substring(cls, "", " to ");
        return cls + "." + method;
    }
}
