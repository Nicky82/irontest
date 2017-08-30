package io.irontest.db;

import io.irontest.models.UserDefinedProperty;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

import static io.irontest.IronTestConstants.DB_UNIQUE_NAME_CONSTRAINT_NAME_SUFFIX;

/**
 * Created by Zheng on 29/08/2017.
 */
@RegisterMapper(UserDefinedPropertyMapper.class)
public abstract class UserDefinedPropertyDAO {
    @SqlUpdate("CREATE SEQUENCE IF NOT EXISTS udp_sequence START WITH 1 INCREMENT BY 1 NOCACHE")
    public abstract void createSequenceIfNotExists();

    @SqlUpdate("CREATE TABLE IF NOT EXISTS udp (" +
            "id BIGINT DEFAULT udp_sequence.NEXTVAL PRIMARY KEY, testcase_id BIGINT, " +
            "name VARCHAR(200) NOT NULL DEFAULT CURRENT_TIMESTAMP, value VARCHAR(500), " +
            "created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "FOREIGN KEY (testcase_id) REFERENCES testcase(id) ON DELETE CASCADE, " +
            "CONSTRAINT UDP_" + DB_UNIQUE_NAME_CONSTRAINT_NAME_SUFFIX + " UNIQUE(testcase_id, name))")
    public abstract void createTableIfNotExists();

    @SqlUpdate("insert into udp (testcase_id) values (:testcaseId)")
    @GetGeneratedKeys
    protected abstract long _insert(@Bind("testcaseId") long testcaseId);

    @SqlUpdate("update udp set name = :name where id = :id")
    protected abstract long updateNameForInsert(@Bind("id") long id, @Bind("name") String name);

    @Transaction
    public UserDefinedProperty insert(long testcaseId) {
        long id = _insert(testcaseId);
        String name = "Property " + id;
        updateNameForInsert(id, name);
        return findById(id);
    }

    @SqlQuery("select * from udp where id = :id")
    protected abstract UserDefinedProperty findById(@Bind("id") long id);

    @SqlQuery("select * from udp where testcase_id = :testcaseId")
    public abstract List<UserDefinedProperty> findByTestcaseId(@Bind("testcaseId") long testcaseId);

    @SqlUpdate("update udp set name = :name, value = :value where id = :id")
    public abstract void update(@BindBean UserDefinedProperty udp);
}
