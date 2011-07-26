// Copyright 2007, 2011 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.corelib.components;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.corelib.base.AbstractPropertyOutput;
import org.apache.tapestry5.internal.services.GridOutputContext;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.PropertyOutputContext;

/**
 * Part of {@link Grid} that renders the markup inside a single data cell. GridCell is used inside a pair of loops; the
 * outer loop for each row, the inner loop for each property of the row.
 * 
 * @tapestrydoc
 */
public class GridCell extends AbstractPropertyOutput
{
    @Inject
    private Environment environment;

    Object beginRender(MarkupWriter writer)
    {
        Object returnValue = renderPropertyValue(writer, getPropertyModel().getId() + "Cell");

        pushGridOutputContextInEnvironment();

        return returnValue;
    }

    private void pushGridOutputContextInEnvironment()
    {
        GridOutputContext gridOutputContext = environment.peek(GridOutputContext.class);
        if (gridOutputContext == null)
        {
            gridOutputContext = new GridOutputContext();
            environment.push(GridOutputContext.class, gridOutputContext);
        }

        gridOutputContext.add(environment.peek(PropertyOutputContext.class));
    }
}
