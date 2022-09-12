package nl.knaw.huc.rdf4j.rio.nquadsnd;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.RDFParserFactory;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.eclipse.rdf4j.rio.RDFFormat.*;

public class NQuadsUdParserFactory implements RDFParserFactory {
    public static final RDFFormat NQUADS_UD_FORMAT = new RDFFormat(
            "NQuadsUnifiedDiff",
            "application/vnd.timbuctoo-rdf.nquads_unified_diff",
            UTF_8,
            "nqud",
            NO_NAMESPACES,
            SUPPORTS_CONTEXTS,
            NO_RDF_STAR
    );

    @Override
    public RDFFormat getRDFFormat() {
        return NQUADS_UD_FORMAT;
    }

    @Override
    public RDFParser getParser() {
        return new NQuadsUdParser();
    }
}