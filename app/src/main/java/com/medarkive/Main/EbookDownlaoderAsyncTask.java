package com.medarkive.Main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.medarkive.Utilities.Functions;

import android.content.Context;
import android.os.AsyncTask;

public class EbookDownlaoderAsyncTask {
	 private Context ctx ;
	 private String folder;

	    public void startDownload(String[] url , Context ctx , String folderName) {
	        DownloadFileAsync dloadFAsync = new DownloadFileAsync(url);
	        dloadFAsync.execute(url);
	        this.ctx=ctx;
	        this.folder=folderName;
	    }

	    //              Async  Task
	    class DownloadFileAsync extends AsyncTask<String, String, String> {
	        int current=0;
	        String[] paths;
	        boolean show = false;

	        public DownloadFileAsync(String[] paths) {
	            super();
	            this.paths = paths;
	            for(int i=0; i<paths.length; i++)
	                System.out.println((i+1)+":  "+paths[i]);
	        }

	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	        }

	        @Override
	        protected String doInBackground(String... aurl) {
	            int rows = aurl.length;
	                while(current < rows)
	                {
	                    int count;
	                    try {
	                        System.out.println("Current:  "+current+"\t\tRows: "+rows);
	                        String  fileName = this.paths[current].toString().substring(
	                        		this.paths[current].toString().lastIndexOf('/') + 1,
	                        		this.paths[current].toString().length());

	                        File fold = ctx.getDir(folder, ctx.MODE_PRIVATE);
//	                        fpath = getFileName(this.paths[current]);
	                        URL url = new URL(this.paths[current]);
	                        URLConnection conexion = url.openConnection();
	                        conexion.connect();
	                        int lenghtOfFile = conexion.getContentLength();
	                        InputStream input = new BufferedInputStream(url.openStream(), 512);
	                        OutputStream output = new FileOutputStream(fold.getAbsolutePath()+"/"+fileName);
	                        byte data[] = new byte[512];
	                        long total = 0;
	                        while ((count = input.read(data)) != -1) {
	                            total += count;
	                            publishProgress(""+(int)((total*100)/lenghtOfFile));
	                            output.write(data, 0, count);
	                        }
	                        show = true;
	                        output.flush();
	                        output.close();
	                        input.close();
	                        current++;
	                    } catch (Exception e) {}
	            }   //  while end
	            return null;
	        }

	        @Override
	        protected void onProgressUpdate(String... progress) {
	        	
	        }

	        @Override
	        protected void onPostExecute(String unused) {
	        	Functions.dismissLoadingDialog();
	            System.out.println("unused: "+unused);
	            ((DisplayDataActivity)ctx).finishProgress();
	            ((DisplayDataActivity)ctx).enable(true);
	        }
	    }
}
