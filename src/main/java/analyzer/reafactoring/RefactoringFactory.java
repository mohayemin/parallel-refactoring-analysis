package analyzer.reafactoring;

import db.RefactoringDbItem;

public class RefactoringFactory {
    public Refactoring create(RefactoringDbItem dbItem) {
        return switch (dbItem.refactoringType) {
            case "Move Attribute" -> new MoveAttributeRefactoring(dbItem);
            default -> null;
        };
    }
}
