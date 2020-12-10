package analyzer.reafactoring;

import db.RefactoringDbItem;

import java.util.Collections;
import java.util.List;

// Move Attribute	protected modelManager : StructureModelManager
// from class melnorme.lang.ide.ui.editor.structure.LangOutlineInformationControl
//   to class melnorme.lang.ide.ui.editor.structure.GetUpdatedStructureUIOperation
public class MoveAttributeRefactoring extends Refactoring {
    public MoveAttributeRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    @Override
    public String affectedElementRaw() {
        var parts = dbItem.refactoringDetail.replace('\t', ' ').split(" ");
        var attributeName = parts[3];
        var fromClass = parts[8];
        return fromClass + "." + attributeName;
    }
}

