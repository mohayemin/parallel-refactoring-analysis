package analyzer.reafactoring;

import db.RefactoringDbItem;

import java.util.List;

public abstract class Refactoring {
    public final RefactoringDbItem dbItem;

    public abstract String affectedElement();

    protected Refactoring(RefactoringDbItem dbItem) {
        this.dbItem = dbItem;
    }

    public boolean overlaps(Refactoring other) {
        return affectedElement().equals(other.affectedElement());
    }

    public String substring(String str, String from) {
        return str.substring(str.indexOf(from) + from.length());
    }

    public String substring(String str, String from, String to) {
        return str.substring(str.indexOf(from) + from.length(), str.indexOf(to));
    }
}

