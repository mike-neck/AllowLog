
package jp.group.android.atec.allowlog.model.entity;

import java.util.Date;

/**
 * POJO for {@code AllowanceLog}.
 * 
 * @author mike
 * 
 */
public class AllowanceLog {

    public static final String TABLE = "ALLOWANCE_LOG";

    public static final String LOG_DATE = "LOG_DATE";

    public static final String AMOUNT = "AMOUNT";

    /**
     * データベースで自動生成されるID.
     */
    private int id;

    /**
     * 小遣いを支払った日時.
     */
    private Date logDate;

    /**
     * 使用した金額.
     */
    private long amount;

    /**
     * getter for id.
     * 
     * @return - id in database.
     */
    public int getId() {
        return id;
    }

    /**
     * setter for id.
     * 
     * @param id
     *            - id in database.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter for logDate.
     * 
     * @return - logDate in {@code java.util.Date} format.
     */
    public Date getLogDate() {
        return logDate;
    }

    /**
     * getter for logDate.
     * 
     * @return - logDate in {@code long} format.
     */
    public long getLogDateAsLong() {
        return logDate.getTime();
    }

    /**
     * setter for logDate.
     * 
     * @param logDate
     *            - logDate in {@code java.util.Date} format.
     */
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    /**
     * setter for logDate.
     * 
     * @param logDate
     *            - logDate in {@code long} format.
     */
    public void setLogDateInLong(long logDate) {
        this.logDate = new Date(logDate);
    }

    /**
     * getter for amount.
     * 
     * @return - amount.
     */
    public long getAmount() {
        return amount;
    }

    /**
     * setter for amount.
     * 
     * @param amount
     *            - amount in {@code long}.
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
