/*
 * Copyright 2012 Android Test and Evaluation Club
 * 
 */

package jp.group.android.atec.allowlog;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

/**
 * AllowLogのシステムテスト.
 * <p>
 * Robotiumを使用
 * 
 * @author Koji Hasegawa
 * @since 1.0
 */
public class AllowlogRobotiumTest extends ActivityInstrumentationTestCase2<MainActivity> {

    //@formatter:off
    static final int TIMEOUT = 5000;
    static final int MAIN_PAYMENT_TEXT = 0;
    static final int MAIN_TOTAL_TEXT = 5;
    static final int REGISTER_PAYMENT = 1;
    static final int HISTORY_FIRST_AMOUNT = 1;
    Solo solo;
    //@formatter:on

    public AllowlogRobotiumTest() {
        super("jp.group.android.atec.allowlog", MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    /**
     * 金額欄に指定金額を入力→[登録]→確認画面でキャンセル.
     * <p>
     * システムテスト項目のうち、以下の確認を行なう
     * <p>
     * A.テストデータの金額を入力できる<br>
     * B.初期:ボタンが押せる<br>
     * 　<s>B-1.インストール直後であればログに何も表示されないこと</s><br>
     * C.登録ボタンが押せる<br>
     * 　C-1.確認画面が出る<br>
     * 　C-2.確認画面に表示されている金額は前画面で入力した値(手順X)である<br>
     * 　C-3.[キャンセル]したとき「今月使ったお金」の下にある合計額に変更がないこと<br>
     * 
     * @param inputAmount 入力金額
     */
    private void inputAmountAndCancel(String inputAmount) {
        solo.assertCurrentActivity("MainActivityが表示されること", MainActivity.class);
        int beforeTotal = Integer.parseInt(solo.getText(MAIN_TOTAL_TEXT).getText().toString());
        solo.enterText(MAIN_PAYMENT_TEXT, inputAmount);
        solo.clickOnButton("登録");

        assertTrue("C-1.確認画面が出る", solo.waitForActivity("RegisterActivity", TIMEOUT));
        String confirmValue = solo.getText(REGISTER_PAYMENT).getText().toString();
        assertEquals("C-2.確認画面に表示されている金額は前画面で入力した値(手順X)である", inputAmount, confirmValue);
        solo.clickOnButton("キャンセル");

        assertTrue("MainActivityに戻ること", solo.waitForActivity("MainActivity", TIMEOUT));

        // 合計欄はToastの存在で添字がずれるため、Solo#getText()でなくid指定で取得する
        TextView total = (TextView)solo.getCurrentActivity().findViewById(R.id.total);
        int afterTotal = Integer.parseInt(total.getText().toString());
        assertEquals("C-3.[キャンセル]したとき「今月使ったお金」の下にある合計額に変更がないこと", beforeTotal, afterTotal);
    }

    /**
     * 金額欄に指定金額を入力→[登録]→確認画面で[登録].
     * <p>
     * システムテスト項目のうち、以下の確認を行なう
     * <p>
     * A.テストデータの金額を入力できる<br>
     * B.初期:ボタンが押せる<br>
     * 　<s>B-1.インストール直後であればログに何も表示されないこと</s><br>
     * C.登録ボタンが押せる<br>
     * 　C-1.確認画面が出る<br>
     * 　C-2.確認画面に表示されている金額は前画面で入力した値(手順X)である<br>
     * 　C-4.[登録]したとき「今月使ったお金」の合計額に変更があること<br>
     * 　C-5.その額は「起動時の額」＋「前画面で入力した値」(テストデータの値)であること<br>
     * 
     * @param inputAmount 入力金額
     */
    private void inputAmountAndRegister(String inputAmount) {
        solo.assertCurrentActivity("MainActivityが表示されること", MainActivity.class);
        int beforeTotal = Integer.parseInt(solo.getText(MAIN_TOTAL_TEXT).getText().toString());
        solo.enterText(MAIN_PAYMENT_TEXT, inputAmount);
        solo.clickOnButton("登録");

        assertTrue("C-1.確認画面が出る", solo.waitForActivity("RegisterActivity", TIMEOUT));
        String confirmValue = solo.getText(REGISTER_PAYMENT).getText().toString();
        assertEquals("C-2.確認画面に表示されている金額は前画面で入力した値(手順X)である", inputAmount, confirmValue);
        solo.clickOnButton("登録");

        assertTrue("MainActivityに戻ること", solo.waitForActivity("MainActivity", TIMEOUT));
        int expectedTotal = beforeTotal + Integer.parseInt(inputAmount);

        // 合計欄はToastの存在で添字がずれるため、Solo#getText()でなくid指定で取得する
        TextView total = (TextView)solo.getCurrentActivity().findViewById(R.id.total);
        int afterTotal = Integer.parseInt(total.getText().toString());
        assertEquals("C-5.その額は「起動時の額」＋「前画面で入力した値」(テストデータの値)であること", expectedTotal, afterTotal);
    }

    /**
     * 金額欄に指定金額を入力→[登録]→確認画面で[登録].
     * <p>
     * システムテスト項目のうち、以下の確認を行なう
     * <p>
     * D.登録後:履歴ボタンが押せる<br>
     * 　D-1.C-4で登録した内容が最上部に表示されていること<br>
     * 
     * @param topValue 履歴の一番上（最新）にあるべき金額
     */
    private void checkHistoryTop(String topValue) {
        solo.assertCurrentActivity("MainActivityが表示されること", MainActivity.class);
        solo.clickOnButton("履歴");

        assertTrue("D.登録後:履歴ボタンが押せる", solo.waitForActivity("HistoryActivity", TIMEOUT));
        String confirmValue = solo.getText(HISTORY_FIRST_AMOUNT).getText().toString();
        assertEquals("D-1.C-4で登録した内容が最上部に表示されていること", topValue, confirmValue);
    }

    /**
     * 金額登録のテスト（金額欄に1000を入力して登録確認でキャンセル）.
     */
    public void test金額欄に1000を入力して登録確認でキャンセル() {
        inputAmountAndCancel("1000");
    }

    /**
     * 金額登録のテスト（金額欄に1000を入力して登録）.
     */
    public void test金額欄に1000を入力して登録() {
        String inputValue = "1000";
        inputAmountAndRegister(inputValue);
        checkHistoryTop(inputValue);
    }

    /**
     * 金額登録のテスト（金額欄に0を入力して登録確認でキャンセル）.
     */
    public void test金額欄に0を入力して登録確認でキャンセル() {
        inputAmountAndCancel("0");
    }

    /**
     * 金額登録のテスト（金額欄に0を入力して登録）.
     */
    public void test金額欄に0を入力して登録() {
        String inputValue = "0";
        inputAmountAndRegister(inputValue);
        checkHistoryTop(inputValue);
    }

}
