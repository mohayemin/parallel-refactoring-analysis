package analyzer.reafactoring;

import db.RefactoringDbItem;

public class RenameClassRefactoring extends Refactoring {
    protected RenameClassRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Rename Class	com.oath.cyclops.reactor.adapter.FluxReactiveSeq renamed to com.oath.cyclops.reactor.adapter.FluxReactiveSeqImpl----com.oath.cyclops.reactor.adapter.FluxReactiveSeq
    @Override
    public String affectedElement() {
        var detail = dbItem.refactoringDetail;
        return substring(detail, "\t", " renamed to ");
    }
}
