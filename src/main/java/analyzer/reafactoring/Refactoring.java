package analyzer.reafactoring;

import db.RefactoringDbItem;

public abstract class Refactoring {
    public final RefactoringDbItem dbItem;

    public abstract String affectedElement();

    protected Refactoring(RefactoringDbItem dbItem) {
        this.dbItem = dbItem;
    }

    public boolean overlaps(Refactoring other) {
        if(isSameOrRebase(other))
            return false;
        return affectedElement().equals(other.affectedElement());
    }

    public String substring(String str, String from) {
        return str.substring(str.indexOf(from) + from.length());
    }

    public String substring(String str, String from, String to) {
        return str.substring(str.indexOf(from) + from.length(), str.indexOf(to));
    }

    /**
     * This method uses a simple heuristic for rebase
     * If refactoring details are identical, possibly it is rebase
     * This is actually OK for the analysis because identical refactoring in
     * parallel branches will no effect on conflict
     * @param other
     * @return
     */
    private boolean isSameOrRebase(Refactoring other) {
        /*
         * TODO: sometimes, same unit comes from both branches
         *  This should not happen and needs to be fixed
         *  For now, just skipping such pair should work
         * */
        return dbItem.commitHash.equals(other.dbItem.commitHash) ||
                dbItem.refactoringDetail.equals(other.dbItem.refactoringDetail)
        ;
    }
}

