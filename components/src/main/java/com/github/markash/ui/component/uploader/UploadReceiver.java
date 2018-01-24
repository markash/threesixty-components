package com.github.markash.ui.component.uploader;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * How to use:
 * <pre>
 * {@code
 * UploadReceiver sourceFileReceiver = new UploadReceiver(this::onReceiveSourceFile);
 * Upload sourceFileUpload = new Upload("File 1", sourceFileReceiver);
 * sourceFileUpload.addSucceededListener(sourceFileReceiver);
 * }
 * </pre>
 * @author Mark P Ashworth
 */
public class UploadReceiver implements Upload.Receiver, Upload.SucceededListener {
    private File file;
    private final String filePrefix;
    private final UploadSucceededListener uploadSucceededListener;
    private String mimeType;

    public UploadReceiver(final UploadSucceededListener uploadSucceededListener) {
        this("temp", uploadSucceededListener);
    }

    public UploadReceiver(final String filePrefix, final UploadSucceededListener uploadSucceededListener) {
        this.filePrefix = filePrefix;
        this.uploadSucceededListener = uploadSucceededListener;
    }

    public File getFile() { return this.file; }
    public String getFilePrefix() { return filePrefix; }
    public String getMimeType() { return mimeType; }

    public OutputStream receiveUpload(final String filename,
                                      final String mimeType) {

        try {
            this.mimeType = mimeType;
            this.file = File.createTempFile(filePrefix, filename);
            return new FileOutputStream(file);

        } catch (final java.io.FileNotFoundException e) {
            new Notification("Could not open file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;

        } catch (final java.io.IOException e) {
            new Notification("System problem uploading file<br/>",
                    e.getMessage(),
                    Notification.Type.ERROR_MESSAGE)
                    .show(Page.getCurrent());
            return null;
        }
    }

    public void uploadSucceeded(Upload.SucceededEvent event) {
        this.uploadSucceededListener.onUpload(this.file, event.getMIMEType(), event.getLength());
    }
}
