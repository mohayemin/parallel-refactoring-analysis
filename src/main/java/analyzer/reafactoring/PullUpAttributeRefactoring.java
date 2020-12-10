package analyzer.reafactoring;

import db.RefactoringDbItem;

public class PullUpAttributeRefactoring extends Refactoring {
    protected PullUpAttributeRefactoring(RefactoringDbItem dbItem) {
        super(dbItem);
    }

    // Pull Up Attribute	protected producerNode : LinkedQueueAtomicNode<E> from class org.jctools.queues.atomic.BaseLinkedAtomicQueue to class org.jctools.queues.atomic.BaseLinkedAtomicQueueProducerNodeRef----org.jctools.queues.atomic.BaseLinkedAtomicQueue.producerNode
    @Override
    public String affectedElementRaw() {
        var detail = dbItem.refactoringDetail;
        var cls = substring(detail, " from class ", " to class ");
        var attribute = substring(detail, "\t", " :");
        attribute = substring(attribute, " ");
        return cls + "." + attribute;
    }
}
