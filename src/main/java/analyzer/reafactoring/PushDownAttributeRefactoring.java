package analyzer.reafactoring;

import db.RefactoringDbItem;

public class PushDownAttributeRefactoring extends Refactoring{
    protected PushDownAttributeRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Push Down Attribute	private fn : Publisher<T> from class cyclops.reactive.IO to class cyclops.reactive.IO.ReactiveSeqIO----cyclops.reactive.IO.fn
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " from class ", " to class ");
        var attribute = substring(detail, "\t", " : ");
        attribute = removeAccessModifier(attribute);
        return cls + "." + attribute;
    }
}
