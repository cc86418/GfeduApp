package cn.jun.pushMessage;


import android.os.AsyncTask;

public abstract class PushClass {

    public void toSubmit() {
        /** 获取LoadTast对象 */
        LoadTast loadtast = new LoadTast();
        /** 执行异步task */
        loadtast.execute();
    }

    /**
     * 这里写 逻辑处理
     *
     * @return
     */
    public abstract String init();

    /**
     * 此处为Runs on the UI thread before doInBackground
     */
    public abstract void preExecute();

    /**
     * 此处为Runs on the UI thread after doInBackground
     */
    public abstract void postExecute(String result);

    public abstract void progressUpdate(Integer... values);

    /**
     * 继承自AsyncTast的LoadTast
     */
    public class LoadTast extends AsyncTask<Void, Integer, String> {

        protected String doInBackground(Void... params) {
            /** 这里写验证代码 */
            return init();
        }

        public void onPreExecute() {
            /** 创建一个对话框并显示 */
            preExecute();

            // progressDialog = ProgressDialog.show(context, title, message,
            // true);
        }

        public void onProgressUpdate(Integer... values) {
            /** 将从publishProgress传递过来的值进行格式化后显示到TextView组件 */
            progressUpdate(values);
        }

        public void onPostExecute(String result) {
            /** 隐藏对话框 */
            postExecute(result);
            // progressDialog.dismiss();

        }

    }

    ;
}
