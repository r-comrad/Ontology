public class RDFWriter {
    private MyFileWriter mFileWriter;

    public RDFWriter() {
        mFileWriter = new MyFileWriter("output_ontology.myRDF");
    }

    public RDFWriter(String aFileName) {
        mFileWriter = new MyFileWriter(aFileName);
    }

    public void write(String aSubject, String aPredicate, String aObject) {
        String resultingString = aSubject + " " + aPredicate + " " + aObject + "\n";
        mFileWriter.write(resultingString);
    }

    public void writeLever(String aObject, Pair<String, String> aParent) {
        write(aObject, aParent.mX, aParent.mY);
    }

    public void close()
    {
        mFileWriter.close();
    }

    public void newLine()
    {
        mFileWriter.newLine();
    }
}
