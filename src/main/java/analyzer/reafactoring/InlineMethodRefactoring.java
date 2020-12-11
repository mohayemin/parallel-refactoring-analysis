package analyzer.reafactoring;

import db.RefactoringDbItem;

public class InlineMethodRefactoring extends Refactoring{
    protected InlineMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Inline Method	private startListening() : void inlined to public onCreate(icicle Bundle) : void in class jackpal.androidterm.Term----jackpal.androidterm.Term.startListening()
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " in class ");
        var method = substring(detail, "\t", ") ") + ")";
        method = removeAccessModifier(method);
        return cls + "." + method;
    }
}
