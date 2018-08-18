package org.biacode.escommons.toolkit.component.impl

import org.biacode.escommons.core.model.document.AbstractEsDocument
import org.biacode.escommons.core.model.response.DocumentsAndTotalCount
import org.biacode.escommons.toolkit.component.JsonComponent
import org.biacode.escommons.toolkit.component.SearchResponseComponent
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.SearchHits
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors.toList
import java.util.stream.Stream

/**
 * Created by Arthur Asatryan.
 * Date: 7/10/17
 * Time: 6:11 PM
 */
@Component
class SearchResponseComponentImpl : SearchResponseComponent {

    //region Dependencies
    @Autowired
    private lateinit var jsonComponent: JsonComponent
    //endregion

    //region Public methods
    override fun <T : AbstractEsDocument> convertToDocumentsAndTotalCount(searchResponse: SearchResponse, clazz: Class<T>): DocumentsAndTotalCount<T> {
        val searchHits = searchResponse.hits
        val documents = extractDocuments(searchHits, clazz)
        return DocumentsAndTotalCount(documents, searchHits.getTotalHits())
    }

    override fun <T : AbstractEsDocument> convertGetResponseToDocument(getResponse: GetResponse, clazz: Class<T>): T {
        val document = jsonComponent.deserializeFromString(getResponse.sourceAsString, clazz)
        document.id = getResponse.id
        return document
    }

    override fun <T : AbstractEsDocument> convertSearchResponseToDocuments(getResponse: SearchResponse, clazz: Class<T>): List<T> =
            extractDocuments(getResponse.hits, clazz)

    override fun convertToIdsList(searchResponse: SearchResponse): List<String> = Arrays
            .stream(searchResponse.hits.hits)
            .map<String> { it.id }
            .collect(toList())
    //endregion

    //region Utility methods
    private fun <T : AbstractEsDocument> extractDocuments(searchHits: SearchHits, clazz: Class<T>): List<T> = Stream
            .of(*searchHits.hits)
            .map { searchHitFields ->
                val document = jsonComponent.deserializeFromString(searchHitFields.sourceAsString, clazz)
                document.id = searchHitFields.id
                document
            }
            .collect(toList())
    //endregion
}
