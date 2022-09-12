package nl.knaw.huc.rdf4j.rio.nquadsnd;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.RDFWriterFactory;

import java.io.OutputStream;
import java.io.Writer;

public class NQuadsUdWriterFactory implements RDFWriterFactory {
    @Override
    public RDFFormat getRDFFormat() {
        return NQuadsUdParserFactory.NQUADS_UD_FORMAT;
    }

    @Override
    public RDFWriter getWriter(OutputStream out) {
        return new NQuadsUdWriter(out);
    }

    @Override
    public RDFWriter getWriter(OutputStream out, String baseURI) {
        return getWriter(out);
    }

    @Override
    public RDFWriter getWriter(Writer writer) {
        return new NQuadsUdWriter(writer);
    }

    @Override
    public RDFWriter getWriter(Writer writer, String baseURI) {
        return getWriter(writer);
    }
}
