package analyzer.reafactoring;

import db.RefactoringDbItem;

public class PullUpMethodRefactoring extends Refactoring {
    protected PullUpMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Pull Up Method	protected showAboutDialog() : void from class mediathek.mac.MediathekGuiMac to protected showAboutDialog() : void from class mediathek.MediathekGui----mediathek.mac.MediathekGuiMac.showAboutDialog()
    @Override
    public String affectedElement() {
        var detail = dbItem.refactoringDetail;
        var targetString = substring(detail, "\t", " to ");
        var cls = substring(targetString, " from class ");
        var method = substring(targetString, " ", ") ") + ")";
        return cls + "." + method;
    }
}
