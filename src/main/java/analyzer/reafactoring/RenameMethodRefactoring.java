package analyzer.reafactoring;

import db.RefactoringDbItem;

// Rename Method	public delete(accountSid String, sid String) : AddressDeleter renamed to public deleter(accountSid String, sid String) : AddressDeleter in class com.twilio.rest.api.v2010.account.Address
public class RenameMethodRefactoring extends Refactoring {
    protected RenameMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    @Override
    public String affectedElement() {
        var detail = dbItem.refactoringDetail;
        var cls = detail.substring(detail.indexOf("in class ") + "in class ".length());
        var method = detail.substring(detail.indexOf('\t') + 1);
        method = method.substring(method.indexOf(' ') + 1, method.indexOf(" :"));
        return cls + "." + method;
    }
}
