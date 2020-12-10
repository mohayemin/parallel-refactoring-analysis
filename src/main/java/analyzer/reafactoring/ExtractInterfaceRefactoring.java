package analyzer.reafactoring;

import db.RefactoringDbItem;

public class ExtractInterfaceRefactoring extends Refactoring{
    protected ExtractInterfaceRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Extract Interface	com.aol.cyclops.sequence.streamable.ConvertableToSequenceM from classes [com.aol.cyclops.sequence.JoolWindowing, com.aol.cyclops.sequence.streamable.ToStream]----[com.aol.cyclops.sequence.JoolWindowing, com.aol.cyclops.sequence.streamable.ToStream]
    @Override
    public String affectedElement() {
        return substring(dbItem.refactoringDetail, " from classes ");
    }
}
