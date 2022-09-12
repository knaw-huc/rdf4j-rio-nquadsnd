package nl.knaw.huc.rdf4j.rio.nquadsnd;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.nquads.NQuadsWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.function.BiConsumer;

public class NQuadsUdWriter extends NQuadsWriter implements RDFAssertionHandler {
    protected BiConsumer<Boolean, Statement> statementConsumer;

    public NQuadsUdWriter(OutputStream outputStream) {
        super(outputStream);
    }

    public NQuadsUdWriter(Writer writer) {
        super(writer);
    }

    @Override
    public RDFFormat getRDFFormat() {
        return NQuadsUdParserFactory.NQUADS_UD_FORMAT;
    }

    @Override
    public void startRDF() throws RDFHandlerException {
        super.startRDF();

        statementConsumer = this::consumeStatement;
        // TODO: Convert RDF-star
        // if (getWriterConfig().get(BasicWriterSettings.CONVERT_RDF_STAR_TO_REIFICATION)) {
        //     // All writers can convert RDF-star to reification on request
        //     statementConsumer = this::handleStatementConvertRDFStar;
        // } else if (!getRDFFormat().supportsRDFStar() && getWriterConfig().get(BasicWriterSettings.ENCODE_RDF_STAR)) {
        //     // By default non-RDF-star writers encode RDF-star to special RDF IRIs
        //     // (all parsers, including RDF-star will convert back the encoded IRIs)
        //     statementConsumer = this::handleStatementEncodeRDFStar;
        // }
    }

    @Override
    public void handleStatement(boolean isAssertion, Statement st) throws RDFHandlerException {
        checkWritingStarted();
        statementConsumer.accept(isAssertion, st);
    }

    public void consumeStatement(boolean isAssertion, Statement st) throws RDFHandlerException {
        try {
            writer.write(isAssertion ? "+" : "-");
            consumeStatement(st);
        } catch (IOException e) {
            throw new RDFHandlerException(e);
        }
    }
}
