package analyzer.reafactoring;

import db.RefactoringDbItem;

public class PushDownMethodRefactoring extends Refactoring{
    protected PushDownMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Push Down Method\tpublic map(s Function<? super T,? extends R>) : IO<R> from class cyclops.reactive.IO to public map(s Function<? super T,? extends R>) : IO<R> from class cyclops.reactive.IO.ReactiveSeqIO----cyclops.reactive.IO.map(s Function<? super T,? extends R>)",
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " from class ", " to ");
        var method = substring(detail, "\t", ") ");
        method = substring(method, " ") + ")";
        return cls + "." + method;
    }
}
