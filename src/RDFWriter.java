public class RDFWriter {
    private MyFileWriter mFileWriter;

    public RDFWriter() {
        mFileWriter = new MyFileWriter("code_ontology.myRDF");
    }

    public RDFWriter(String aFileName) {
        mFileWriter = new MyFileWriter(aFileName);
    }

    public String write(String aSubject, String aPredicate, String aObject) {
        String resultingString = aSubject + " " + aPredicate + " " + aObject + "\n";
        mFileWriter.write(resultingString);
    }
}
