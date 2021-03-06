package analyzer.reafactoring;

import db.RefactoringDbItem;

public class PushDownMethodRefactoring extends Refactoring{
    protected PushDownMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Push Down Method\tpublic map(s Function<? super T,? extends R>) : IO<R> from class cyclops.reactive.IO to public map(s Function<? super T,? extends R>) : IO<R> from class cyclops.reactive.IO.ReactiveSeqIO----cyclops.reactive.IO.map(s Function<? super T,? extends R>)",
    // Push Down Method	public distance(from Coordinate, to Coordinate) : double from class org.opentripplanner.common.geometry.DistanceLibrary to public distance(from Coordinate, to Coordinate) : double from class org.opentripplanner.common.geometry.SphericalDistanceLibrary----org.opentripplanner.common.geometry.DistanceLibrary.distance(from Coordinate, to Coordinate)
    // Push Down Method	public Finder(target Type) from class com.sun.tools.javac.ceylon.ExtensionFinder.Finder to public TypeFinder(target Type) from class com.sun.tools.javac.ceylon.ExtensionFinder.TypeFinder
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;

        var method = substring(detail, "\t", " from class ");
        method = removeAccessModifier(method);
        method = substring(method, "", ")") + ")";
        var cls = substring(detail, " from class ");
        cls = substring(cls, "", " to ");
        return cls + "." + method;
    }
}
