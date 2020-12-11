package analyzer.reafactoring;

import db.RefactoringDbItem;

import java.util.List;

public class ExtractMethodRefactoring extends Refactoring {

    protected ExtractMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Extract Method	private commonConstructor(context Context) : void extracted from public TermViewFlipper(context Context, attrs AttributeSet) in class jackpal.androidterm.TermViewFlipper----jackpal.androidterm.TermViewFlipper.TermViewFlipper(context Context, attrs AttributeSet)
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " in class ");
        var from = substring(detail, " extracted from ", " in class ");
        from = removeAccessModifier(from);
        from = substring(from, "", ")") + ")";
        return cls + "." + from;
    }
}
