package analyzer.reafactoring;

import db.RefactoringDbItem;

// Rename Method	public delete(accountSid String, sid String) : AddressDeleter renamed to public deleter(accountSid String, sid String) : AddressDeleter in class com.twilio.rest.api.v2010.account.Address
public class RenameMethodRefactoring extends Refactoring {
    protected RenameMethodRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Rename Method	public delete(accountSid String, sid String) : AddressDeleter renamed to public deleter(accountSid String, sid String) : AddressDeleter in class com.twilio.rest.api.v2010.account.Address----com.twilio.rest.api.v2010.account.Address.delete(accountSid String, sid String)
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, "in class ");
        var method = substring(detail, "\t");
        method = substring(method, " ", ")") + ")";
        return cls + "." + method;
    }
}
