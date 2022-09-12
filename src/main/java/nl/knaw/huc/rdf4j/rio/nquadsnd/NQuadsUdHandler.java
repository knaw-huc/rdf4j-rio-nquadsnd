package nl.knaw.huc.rdf4j.rio.nquadsnd;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;

public class NQuadsUdHandler extends AbstractRDFHandler implements RDFAssertionHandler {
    private final RdfProcessor rdfProcessor;
    private final String defaultGraph;

    public NQuadsUdHandler(RdfProcessor rdfProcessor, String defaultGraph) {
        this.rdfProcessor = rdfProcessor;
        this.defaultGraph = defaultGraph;
    }

    @Override
    public void handleNamespace(String prefix, String uri) throws RDFHandlerException {
        try {
            rdfProcessor.setPrefix(prefix, uri);
        } catch (RdfProcessingFailedException e) {
            throw new RDFHandlerException(e);
        }
    }

    @Override
    public void endRDF() throws RDFHandlerException {
        try {
            rdfProcessor.commit();
        } catch (RdfProcessingFailedException e) {
            throw new RDFHandlerException(e);
        }
    }

    @Override
    public void handleStatement(boolean isAssertion, Statement st) throws RDFHandlerException {
        try {
            if (Thread.currentThread().isInterrupted()) {
                rdfProcessor.commit();
                throw new RDFHandlerException("Interrupted");
            }

            String graph = defaultGraph != null
                    ? defaultGraph
                    : (st.getContext() != null ? st.getContext().stringValue() : null);

            rdfProcessor.onQuad(
                    isAssertion,
                    handleNode(st.getSubject()),
                    st.getPredicate().stringValue(),
                    handleNode(st.getObject()),
                    (st.getObject() instanceof Literal) ? ((Literal) st.getObject()).getDatatype().toString() : null,
                    (st.getObject() instanceof Literal) ? ((Literal) st.getObject()).getLanguage().orElse(null) : null,
                    graph
            );
        } catch (RdfProcessingFailedException e) {
            throw new RDFHandlerException(e);
        }
    }


    private String handleNode(Value resource) {
        return resource.stringValue();
    }
}
