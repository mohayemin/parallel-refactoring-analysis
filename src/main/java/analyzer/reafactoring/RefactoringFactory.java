package analyzer.reafactoring;

import db.RefactoringDbItem;

public class RefactoringFactory {
    public Refactoring create(RefactoringDbItem dbItem) {
        return switch (dbItem.refactoringType) {
            case "Move Class" -> new MoveClassRefactoring(dbItem);
            case "Rename Method" -> new RenameMethodRefactoring(dbItem);
            case "Rename Variable" -> new RenameVariableRefactoring(dbItem);
            case "Rename Parameter" -> new RenameParameterRefactoring(dbItem);
            case "Move Attribute" -> new MoveAttributeRefactoring(dbItem);
            case "Extract Method" -> new ExtractMethodRefactoring(dbItem);
            case "Move Method" -> new MoveMethodRefactoring(dbItem);
            case "Pull Up Method" -> new PullUpMethodRefactoring(dbItem);
            case "Rename Class" -> new RenameClassRefactoring(dbItem);
            case "Pull Up Attribute" -> new PullUpAttributeRefactoring(dbItem);
            case "Extract And Move Method" -> new ExtractAndMoveMethodRefactoring(dbItem);
            case "Extract Variable" -> new ExtractVariableRefactoring(dbItem);
            case "Move And Rename Class" -> new MoveAndRenameClassRefactoring(dbItem);
            case "Push Down Method" -> new PushDownMethodRefactoring(dbItem);
            default -> null;
        };
    }
}
