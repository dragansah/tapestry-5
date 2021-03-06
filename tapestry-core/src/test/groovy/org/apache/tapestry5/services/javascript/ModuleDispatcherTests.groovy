package org.apache.tapestry5.services.javascript

import org.apache.tapestry5.internal.services.javascript.ModuleDispatcher
import org.apache.tapestry5.ioc.internal.QuietOperationTracker
import org.apache.tapestry5.ioc.test.TestBase
import org.apache.tapestry5.services.PathConstructor
import org.apache.tapestry5.services.Request
import org.apache.tapestry5.services.Response
import org.easymock.EasyMock
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import javax.servlet.http.HttpServletResponse

class ModuleDispatcherTests extends TestBase {

    @Test(dataProvider = "unknownPaths")
    void "invalid extension is ignored"(path) {
        def request = newMock Request
        def response = newMock Response
        def pc = newMock PathConstructor

        expect(pc.constructDispatchPath("modules")).andReturn("/modules")

        expect(request.path).andReturn(path)

        expect(response.sendError(EasyMock.eq(HttpServletResponse.SC_NOT_FOUND), EasyMock.notNull()))

        replay()

        def handler = new ModuleDispatcher(null, null, new QuietOperationTracker(), pc, "modules", false)

        assert handler.dispatch(request, response) == true

        verify()
    }

    @DataProvider
    Object[][] unknownPaths() {
        [
            "foo/bar.xyz",
            "foo",
            "foo/bar",
            ""
        ].collect({ it -> ["/modules/$it"] as Object[] }) as Object[][]
    }

    @Test
    void "returns false if no module is found"() {

        def manager = newMock ModuleManager
        def request = newMock Request
        def response = newMock Response
        def pc = newMock PathConstructor

        expect(pc.constructDispatchPath("modules")).andReturn("/modules")

        expect(request.path).andReturn("/modules/foo/bar.js")

        expect(manager.findResourceForModule("foo/bar")).andReturn null

        expect(response.sendError(EasyMock.eq(HttpServletResponse.SC_NOT_FOUND), EasyMock.notNull()))

        replay()

        def handler = new ModuleDispatcher(manager, null, new QuietOperationTracker(), pc, "modules", false)

        assert handler.dispatch(request, response) == true

        verify()
    }
}
