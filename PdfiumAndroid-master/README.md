# Pdfium Android binding with Bitmap rendering
Use pdfium library [from AOSP](https://android.googlesource.com/platform/external/pdfium/)

The demo app (for not modified lib) is [here](https://github.com/mshockwave/PdfiumAndroid-Demo-App)

Forked for use with [AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer) project.

API is highly compatible with original version, only additional methods were created.

## What's new in 1.4.0?
* merge pull request by [usef](https://github.com/usef) with added support for rendering annotations. Due to limitations of _Pdfium_, messages from comments cannot be read and are rendered only as speech balloons.

## Installation
Add to _build.gradle_:

`compile 'com.github.barteksc:pdfium-android:1.4.0'`

Library is available in jcenter and Maven Central repositories.

## Simple example
``` java
void openPdf() {
    ImageView iv = (ImageView) findViewById(R.id.imageView);
    ParcelFileDescriptor fd = ...;
    int pageNum = 0;
    PdfiumCore pdfiumCore = new PdfiumCore(context);
    try {
        PdfDocument pdfDocument = pdfiumCore.newDocument(fd);

        pdfiumCore.openPage(pdfDocument, pageNum);

        int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNum);
        int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNum);

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNum, 0, 0,
                width, height);

        iv.setImageBitmap(bitmap);

        printInfo(pdfiumCore, pdfDocument);

        pdfiumCore.closeDocument(pdfDocument); // important!
    } catch(IOException ex) {
        ex.printStackTrace();
    }
}

public void printInfo(PdfiumCore core, PdfDocument doc) {
    PdfDocument.Meta meta = core.getDocumentMeta(doc);
    Log.e(TAG, "title = " + meta.getTitle());
    Log.e(TAG, "author = " + meta.getAuthor());
    Log.e(TAG, "subject = " + meta.getSubject());
    Log.e(TAG, "keywords = " + meta.getKeywords());
    Log.e(TAG, "creator = " + meta.getCreator());
    Log.e(TAG, "producer = " + meta.getProducer());
    Log.e(TAG, "creationDate = " + meta.getCreationDate());
    Log.e(TAG, "modDate = " + meta.getModDate());

    printBookmarksTree(core.getTableOfContents(doc), "-");

}

public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
    for (PdfDocument.Bookmark b : tree) {

        Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

        if (b.hasChildren()) {
            printBookmarksTree(b.getChildren(), sep + "-");
        }
    }
}

```
## Build native part
Go to `PROJECT_PATH/src/main/jni` and run command `$ ndk-build`.
This step may be executed only once, every future `.aar` build will use generated libs.
