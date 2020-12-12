package analyzer.reafactoring;

import db.RefactoringDbItem;


// Rename Parameter	routeFile : String to routeFiles : List<String> in method public setRouteFiles(routeFiles List<String>) : void in class org.resthub.web.springmvc.router.RouterHandlerMapping
public class RenameParameterRefactoring extends Refactoring {

    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " in class ");
        var method = substring(detail, " in method ", " in class");
        method = removeAccessModifier(method);
        method = substring(method, "", ")") + ")";
        var param = substring(detail, "\t", " : ");
        return cls + "." + method + "." + param;
    }

    protected RenameParameterRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }
}
