package analyzer.reafactoring;

import db.RefactoringDbItem;

public class RenameVariableRefactoring extends Refactoring{
    protected RenameVariableRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Rename Variable\targs : ArrayList<String> to argList : ArrayList<String> in method private createSubprocess(processId int[]) : void in class jackpal.androidterm.session.TermSession----jackpal.androidterm.session.TermSession.createSubprocess(processId int[]).args
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " in class ");
        var method = substring(detail, " in method ", " in class");
        method = removeAccessModifier(method);
        method = substring(method, "", ")") + ")";
        var previousName =  substring(detail, "\t", " : ");

        return cls + "." + method + "." + previousName;
    }
}
