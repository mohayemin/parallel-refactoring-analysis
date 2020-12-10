package analyzer.reafactoring;

import db.RefactoringDbItem;

import java.util.List;

public abstract class Refactoring {
    public final RefactoringDbItem dbItem;

    abstract List<String> affectedElements();

    protected Refactoring(RefactoringDbItem dbItem) {
        this.dbItem = dbItem;
    }

    public boolean overlaps(Refactoring other) {
        var myAffected = affectedElements();
        var otherAffected = other.affectedElements();

        for (var mf : myAffected)
            for (var of : otherAffected)
                if (mf.equals(of))
                    return true;

        return false;
    }
}

