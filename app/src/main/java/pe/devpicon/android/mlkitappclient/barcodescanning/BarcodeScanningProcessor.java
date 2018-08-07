package pe.devpicon.android.mlkitappclient.barcodescanning;
// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.IOException;
import java.util.List;

import pe.devpicon.android.mlkitappclient.FrameMetadata;
import pe.devpicon.android.mlkitappclient.GraphicOverlay;
import pe.devpicon.android.mlkitappclient.VisionProcessorBase;

/**
 * Barcode Detector Demo.
 */
public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

    private static final String TAG = "BarcodeScanProc";

    private final FirebaseVisionBarcodeDetector detector;
    private final ResultHandler resultHandler;

    public BarcodeScanningProcessor(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
        // Note that if you know which format of barcode your app is dealing with, detection will be
        // faster to specify the supported barcode formats one by one, e.g.
        FirebaseVisionBarcodeDetectorOptions barcodeDetectorOptions = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE,
                        FirebaseVisionBarcode.FORMAT_PDF417,
                        FirebaseVisionBarcode.FORMAT_EAN_8,
                        FirebaseVisionBarcode.FORMAT_CODE_93,
                        FirebaseVisionBarcode.FORMAT_CODE_39,
                        FirebaseVisionBarcode.FORMAT_CODE_128,
                        FirebaseVisionBarcode.FORMAT_ITF)
                .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(barcodeDetectorOptions);
        // detector = FirebaseVision.getInstance().getVisionBarcodeDetector();
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @NonNull List<FirebaseVisionBarcode> barcodes,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        for (int i = 0; i < barcodes.size(); ++i) {
            FirebaseVisionBarcode barcode = barcodes.get(i);
            BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);
            graphicOverlay.add(barcodeGraphic);
            resultHandler.onSuccess(barcode.getRawValue());
        }
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        //Log.e(TAG, "Barcode detection failed " + e);
        resultHandler.onFailure("Barcode detection failed " + e.getMessage());
    }

    public interface ResultHandler {
        void onSuccess(String result);

        void onFailure(String message);
    }
}