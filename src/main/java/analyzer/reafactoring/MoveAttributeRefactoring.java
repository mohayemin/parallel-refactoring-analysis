package analyzer.reafactoring;

import db.RefactoringDbItem;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Move Attribute	protected modelManager : StructureModelManager
// from class melnorme.lang.ide.ui.editor.structure.LangOutlineInformationControl
//   to class melnorme.lang.ide.ui.editor.structure.GetUpdatedStructureUIOperation
public class MoveAttributeRefactoring implements Refactoring {
    private final RefactoringDbItem dbItem;

    public MoveAttributeRefactoring(RefactoringDbItem dbItem) {
        this.dbItem = dbItem;
    }

    @Override
    public List<String> affectedElements() {
        var parts = dbItem.refactoringDetail.replace('\t', ' ').split(" ");
        var attributeName = parts[3];
        var fromClass = parts[8];
        return Collections.singletonList(fromClass + "." + attributeName);
    }

    @Override
    public RefactoringDbItem dbItem() {
        return dbItem;
    }
}

