/**
 * Copyright 2013-2018 the original author or authors from the Jeddict project (https://jeddict.github.io/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.jeddict.orm.generator.compiler.constraints;

import io.github.jeddict.bv.constraints.Min;
import io.github.jeddict.orm.generator.compiler.InvalidDataException;
import static io.github.jeddict.orm.generator.util.ORMConverterUtil.CLOSE_PARANTHESES;
import static io.github.jeddict.orm.generator.util.ORMConverterUtil.COMMA;
import static io.github.jeddict.orm.generator.util.ORMConverterUtil.OPEN_PARANTHESES;
import static io.github.jeddict.orm.generator.util.ORMConverterUtil.QUOTE;

/**
 *
 * @author Gaurav Gupta
 */
public class MinSnippet extends ConstraintSnippet<Min> {

    public MinSnippet(Min min) {
        super(min);
    }

    @Override
    protected String getAPI() {
        return "Min";
    }

    @Override
    public String getSnippet() throws InvalidDataException {
        if (constraint.getMessage() == null && constraint.getValue() == null) {
            return "@" + getAPI();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("@").append(getAPI()).append(OPEN_PARANTHESES);

        if (constraint.getValue() != null) {
            if (constraint.getMessage() != null) {
                builder.append("value=");
            }
            builder.append(constraint.getValue());
            builder.append(COMMA);
        }
         if (constraint.getMessage() != null) {
            builder.append("message=\"");
            builder.append(constraint.getMessage());
             builder.append(QUOTE);
             builder.append(COMMA);
        }

        return builder.substring(0, builder.length() - 1) + CLOSE_PARANTHESES;
    }

}