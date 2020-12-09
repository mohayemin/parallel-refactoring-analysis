package analyzer.reafactoring;

import db.RefactoringDbItem;

import java.util.List;

public interface Refactoring {
    List<String> affectedElements();
    RefactoringDbItem dbItem();

    default boolean overlaps(Refactoring other) {
        var myAffected = affectedElements();
        var otherAffected = other.affectedElements();

        for (var mf : myAffected)
            for (var of: otherAffected)
                if(mf.equals(of))
                    return true;

        return false;
    }
}

