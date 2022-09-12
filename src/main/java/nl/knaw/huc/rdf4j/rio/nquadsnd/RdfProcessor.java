package nl.knaw.huc.rdf4j.rio.nquadsnd;

import static org.eclipse.rdf4j.model.vocabulary.RDF.LANGSTRING;

public interface RdfProcessor {
  void setPrefix(String prefix, String iri) throws RdfProcessingFailedException;

  void addRelation(String subject, String predicate, String object, String graph)
      throws RdfProcessingFailedException;

  void addValue(String subject, String predicate, String value, String dataType, String graph)
      throws RdfProcessingFailedException;

  void addLanguageTaggedString(String subject, String predicate, String value, String language,
                               String graph) throws RdfProcessingFailedException;

  void delRelation(String subject, String predicate, String object, String graph)
      throws RdfProcessingFailedException;

  void delValue(String subject, String predicate, String value, String valueType, String graph)
      throws RdfProcessingFailedException;

  void delLanguageTaggedString(String subject, String predicate, String value, String language,
                               String graph) throws RdfProcessingFailedException;

  default void onQuad(boolean isAssertion, String subject, String predicate, String object,
                      String dataType, String language, String graph) throws RdfProcessingFailedException {
    if (isAssertion) {
      if (dataType == null || dataType.isEmpty()) {
        this.addRelation(subject, predicate, object, graph);
      } else {
        if (hasLanguageAnnotation(dataType, language)) {
          this.addLanguageTaggedString(subject, predicate, object, language, graph);
        } else {
          this.addValue(subject, predicate, object, dataType, graph);
        }
      }
    } else {
      if (dataType == null || dataType.isEmpty()) {
        this.delRelation(subject, predicate, object, graph);
      } else {
        if (hasLanguageAnnotation(dataType, language)) {
          this.delLanguageTaggedString(subject, predicate, object, language, graph);
        } else {
          this.delValue(subject, predicate, object, dataType, graph);
        }
      }
    }
  }

  default boolean hasLanguageAnnotation(String dataType, String language) {
    return language != null && !language.isEmpty() && dataType.equals(LANGSTRING.stringValue());
  }

  void commit() throws RdfProcessingFailedException;
}
