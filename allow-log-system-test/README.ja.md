#allow-log-system-test

AllowLogのシステムテスト格納ディレクトリです。

リファクタリング前後で共通のテストとするため下記の専用ブランチで作業します。適時、masterにmergeして使用してください。
    https://github.com/mike-neck/AllowLog/tree/systemtest

#### Known issue
v0.3時点で、プロダクトコード側の仕様変更（\0での登録を許可しない）のため、\0を登録実行するテストケースが失敗します。



#Run on command line
コマンドラインからのテスト実行方法

    $ adb shell am instrument -w org.androidtec.app.allowlog.systemtest/android.test.InstrumentationTestRunner



#Auther
主に、 @nowsprinting



#License

Copyright 2012 Android Test and Evaluation Club

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.