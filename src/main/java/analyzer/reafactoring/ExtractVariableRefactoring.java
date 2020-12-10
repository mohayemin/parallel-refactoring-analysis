package analyzer.reafactoring;

import db.RefactoringDbItem;

public class ExtractVariableRefactoring extends Refactoring{
    protected ExtractVariableRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Extract Variable	actionBar : ActionBarCompat in method public onCreate(icicle Bundle) : void from class jackpal.androidterm.Term----jackpal.androidterm.Term.actionBar
    @Override
    public String affectedElement() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " from class ");
        var variable = substring(detail, "\t", " :");
        return cls + "." + variable;
    }
}
