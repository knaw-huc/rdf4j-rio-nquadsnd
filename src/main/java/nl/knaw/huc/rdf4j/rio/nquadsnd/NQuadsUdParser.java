package nl.knaw.huc.rdf4j.rio.nquadsnd;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.helpers.NTriplesParserSettings;
import org.eclipse.rdf4j.rio.nquads.NQuadsParser;

public class NQuadsUdParser extends NQuadsParser {
    private static final int ADD = '+';

    private boolean isAssertion = true;

    @Override
    protected void parseStatement() throws RDFParseException, RDFHandlerException {
        boolean ignoredAnError = false;
        try {
            skipWhitespace(false);
            if (!shouldParseLine() || !parseDiff()) {
                return;
            }

            skipWhitespace(true);
            if (!shouldParseLineAfterDiff()) {
                return;
            }

            parseSubject();

            skipWhitespace(true);

            parsePredicate();

            skipWhitespace(true);

            parseObject();

            skipWhitespace(true);

            parseContext();

            skipWhitespace(true);

            assertLineTerminates();
        } catch (RDFParseException e) {
            if (getParserConfig().isNonFatalError(NTriplesParserSettings.FAIL_ON_INVALID_LINES)) {
                reportError(e, NTriplesParserSettings.FAIL_ON_INVALID_LINES);
                ignoredAnError = true;
            }
            else {
                throw e;
            }
        }
        handleStatement(ignoredAnError);
    }

    protected boolean parseDiff() {
        if (this.lineChars[this.currentIndex] == '+' || this.lineChars[this.currentIndex] == '-') {
            int action = this.lineChars[this.currentIndex];

            int curIdx = ++this.currentIndex;
            while (curIdx < this.lineChars.length && (this.lineChars[curIdx] == ' ' || this.lineChars[curIdx] == '\t')) {
                ++curIdx;
            }

            if (this.lineChars[curIdx] == '<' || this.lineChars[curIdx] == '_') {
                isAssertion = action == ADD;
            }

            return true;
        }

        return false;
    }

    protected boolean shouldParseLineAfterDiff() {
        if (this.currentIndex < this.lineChars.length - 1) {
            if (this.lineChars[this.currentIndex] == '<' || this.lineChars[this.currentIndex] == '_') {
                return true;
            }

            if (this.lineChars[this.currentIndex] == '#' && this.rdfHandler != null) {
                this.rdfHandler.handleComment(
                        new String(this.lineChars, this.currentIndex + 1, this.lineChars.length - this.currentIndex - 1));
            }
        }

        return false;
    }

    @Override
    protected void handleStatement(boolean ignoredAnError) {
        if (rdfHandler != null && !ignoredAnError) {
            Statement statement = context == null
                    ? valueFactory.createStatement(subject, predicate, object)
                    : valueFactory.createStatement(subject, predicate, object, context);

            rdfHandler.handleStatement(statement);
            if (rdfHandler instanceof RDFAssertionHandler) {
                RDFAssertionHandler rdfAssertionHandler = (RDFAssertionHandler) rdfHandler;
                rdfAssertionHandler.handleStatement(isAssertion, statement);
            }
        }
        subject = null;
        predicate = null;
        object = null;
        context = null;
    }

    @Override
    public RDFFormat getRDFFormat() {
        return NQuadsUdParserFactory.NQUADS_UD_FORMAT;
    }
}
