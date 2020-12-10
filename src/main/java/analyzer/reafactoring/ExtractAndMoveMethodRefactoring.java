package analyzer.reafactoring;

import db.RefactoringDbItem;

public class ExtractAndMoveMethodRefactoring extends Refactoring {
    protected ExtractAndMoveMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Extract And Move Method	public updatePrefs(settings TermSettings, metrics DisplayMetrics) : void extracted from private updatePrefs() : void in class jackpal.androidterm.Term & moved to class jackpal.androidterm.EmulatorView----jackpal.androidterm.Term.updatePrefs()
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " in class ", " & ");
        var from = substring(detail, " extracted from ", " in class ");
        from = substring(from, " ", ") ") + ")";
        return cls + "." + from;
    }
}
