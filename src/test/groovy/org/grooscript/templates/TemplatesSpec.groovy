/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grooscript.templates

import org.grooscript.builder.HtmlBuilder
import spock.lang.Specification

class TemplatesSpec extends Specification {

    def 'convert template'() {
        expect:
        templates.applyTemplate('one.gtpl') == '<p>Hello!</p>'
    }

    def 'convert template with model'() {
        expect:
        templates.applyTemplate('two.tpl', [list: [1, 2], message: 'Msg']) ==
                '<ul><li>1</li><li>2</li></ul><p>Msg</p>'
    }

    def 'convert template using other template'() {
        expect:
        templates.applyTemplate('three.gtpl', [list: [1, 1, 1]]) ==
                '<ul><p>Hello!</p><p>Hello!</p><p>Hello!</p></ul>'
    }

    Templates templates = new Templates()

    def setup() {
        templates.templates = [
            'one.gtpl': { model = [:] ->
                HtmlBuilder.build {
                    p 'Hello!'
                }
            },
            'two.tpl': { model = [:] ->
                HtmlBuilder.build {
                    ul {
                        model.list.each {
                            li it.toString()
                        }
                    }
                    p message
                }
            },
            'three.gtpl': { model = [:] ->
                HtmlBuilder.build {
                    ul {
                        model.list.each {
                            yieldUnescaped Templates.applyTemplate('one.gtpl', model)
                        }
                    }
                }
            }
        ]
    }
}
