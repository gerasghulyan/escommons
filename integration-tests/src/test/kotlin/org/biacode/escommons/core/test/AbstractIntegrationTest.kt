package org.biacode.escommons.core.test

import org.biacode.escommons.core.component.EsCommonsClientWrapper
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Created by Arthur Asatryan.
 * Date: 7/18/17
 * Time: 1:16 PM
 */
@Component
@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration("classpath:applicationContext-escommons-core-test.xml")
abstract class AbstractIntegrationTest {

    //region Dependencies
    @Autowired
    private lateinit var esCommonsClientWrapper: EsCommonsClientWrapper
    //endregion

    //region Test callbacks
    @Before
    fun before() {
        cleanUpIndices()
    }
    //endregion

    //region Protected methods
    protected fun refreshIndex(indexName: String) {
        esCommonsClientWrapper.refreshIndex(indexName)
    }
    //endregion

    //region Utility methods
    protected fun cleanUpIndices() {
        esCommonsClientWrapper.deleteIndices("*")
    }
    //endregion
}
