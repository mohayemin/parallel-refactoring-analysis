package test_with_db_access;

import analyzer.DatabaseOptions;
import analyzer.reafactoring.RefactoringFactory;
import db.Db;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class CatchRefactoringParserError {
    @Test
    public void catchMe() throws SQLException {
        var db = new Db(new DatabaseOptions("localhost:3306", "parallel_refactoring_analysis", "root", "admin"));
        var factoru = new RefactoringFactory();
        var errorCount = 0;
        var totalCount = 0;
        for (var r : db.refactorings) {
            totalCount++;
            var ref = factoru.create(r);
            try {
                ref.allAffectedElements();
            } catch (IndexOutOfBoundsException e) {
                errorCount++;
                System.out.println(e.getMessage());
                System.out.println(r.refactoringDetail);
            }
        }

        System.out.println(errorCount + "/" + totalCount);
    }
}
