package com.eventersapp.research.test;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.*;
import java.io.*;

import android.content.Context;
import android.os.*;
import android.graphics.*;
import android.util.Log;




public final class FaceService{
    // MS Face Service
    private FaceServiceClient faceServiceClient;
    private Context context;
    public FaceService(Context context){
        this.context = context;
        faceServiceClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", "37a6d04478d644118803ea41f51764a4");
        Log.d("info", "Service Initialized! ");
    }
    private class DetectionTask extends AsyncTask<InputStream, String, Face[]>{
        private boolean mSucceed = true;
        @Override
        protected Face[] doInBackground(InputStream... params) {
            Log.d("info", "Detecting! ");
            try{
                return faceServiceClient.detect(
                        params[0],  /* Input stream of image to detect */
                        true,       /* Whether to return face ID */
                        false,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                        new FaceServiceClient.FaceAttributeType[] {
                                FaceServiceClient.FaceAttributeType.Age,
                                FaceServiceClient.FaceAttributeType.Gender
                        });
            } catch (Exception e){
                mSucceed = false;
                publishProgress(e.getMessage());
                Log.d("info", e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPostExecute(Face[] faces){
            // Add advertisement related code here
            if(mSucceed){
                Log.d("info","Response: Success. Detected " + (faces == null ? 0 : faces.length)
                        + " Face(s)");
                Log.d("info", faces[0].faceAttributes.gender);
            }
        }
    }

    public void detect(final Bitmap imageBitmap) {
        if (imageBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            InputStream imageInputStream = new ByteArrayInputStream(stream.toByteArray());
            new DetectionTask().execute(imageInputStream);
        } else {
            Log.d("info", "Image Bitmap is null: ");
        }
    }
}
