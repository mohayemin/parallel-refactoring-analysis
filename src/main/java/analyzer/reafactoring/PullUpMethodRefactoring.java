package analyzer.reafactoring;

import db.RefactoringDbItem;

public class PullUpMethodRefactoring extends Refactoring {
    protected PullUpMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Pull Up Method	protected showAboutDialog() : void from class mediathek.mac.MediathekGuiMac to protected showAboutDialog() : void from class mediathek.MediathekGui----mediathek.mac.MediathekGuiMac.showAboutDialog()
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
