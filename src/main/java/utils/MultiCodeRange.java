package utils;

import java.util.ArrayList;
import java.util.Collection;

public class MultiCodeRange {
    private Collection<SingleCodeRange> units;

    public MultiCodeRange(Collection<SingleCodeRange> units) {
        this.units = units;
    }

    public MultiCodeRange(){
        this(new ArrayList<>());
    }

    public void addUnit(SingleCodeRange unit) {
        units.add(unit);
    }

    public boolean hasOverlap(MultiCodeRange otherMultiRange) {
        for (var thisUnit : this.units) {
            for (var otherUnit : otherMultiRange.units) {
                if (thisUnit.hasOverlap(otherUnit))
                    return true;
            }
        }

        return false;
    }
}
