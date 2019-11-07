public class RDFWriter {
    private MyFileWriter mFileWriter;

    public RDFWriter() {
        mFileWriter = new MyFileWriter("code_ontology.myRDF");
    }

    public RDFWriter(String aFileName) {
        mFileWriter = new MyFileWriter(aFileName);
    }

    public void write(String aSubject, String aPredicate, String aObject) {
        String resultingString = aSubject + " " + aPredicate + " " + aObject + "\n";
        mFileWriter.write(resultingString);
    }

    public void writeLever(String aObject, Pair<String, String> aParent) {
        write(aParent.mX, aParent.mY, aObject);
    }

    public void close()
    {
        mFileWriter.close();
    }
}
