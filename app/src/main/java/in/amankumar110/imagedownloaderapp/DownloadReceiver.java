package in.amankumar110.imagedownloaderapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.net.Uri;
import android.widget.Toast;

public class DownloadReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String filePath = getDownloadedFilePath(context, downloadId);

            if (filePath != null)
                Toast.makeText(context, "File downloaded to downloads", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context, "File couldn't be downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    public String getDownloadedFilePath(Context context, long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        String filePath = null;

        if (downloadManager != null) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);

            Cursor cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String uriString = cursor.getString(columnIndex);

                if (uriString != null) {
                    Uri uri = Uri.parse(uriString);
                    filePath = uri.getPath();
                }
                cursor.close();
            }
        }
        return filePath;
    }
}

