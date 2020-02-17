/**
 *    Copyright 2006-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.utils;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author yttiany
 * @author yttiany
 * @Description:
 * @ProjectName: mybatis-generator
 * @Package: org.mybatis.generator.utils
 * @Date Create on 2020/2/8.13:04
 * -----------------------------------------------------------.
 * @Modify - - - - - - - - - - - - - - - - - -
 * @ModTime 2020/2/8.13:04
 * @ModDesc:
 * @Modify - - - - - - - - - - - - - - - - - -
 * -----------------------------------------------------------
 */
public class OutputUtilities {
    private static final String lineSeparator;

    private OutputUtilities() {
    }

    public static void javaIndent(StringBuilder sb, int indentLevel) {
        for(int i = 0; i < indentLevel; ++i) {
            sb.append("    ");
        }

    }

    public static void xmlIndent(StringBuilder sb, int indentLevel) {
        for(int i = 0; i < indentLevel; ++i) {
            sb.append("  ");
        }

    }

    public static void newLine(StringBuilder sb) {
        sb.append(lineSeparator);
    }

    public static Set<String> calculateImports(Set<FullyQualifiedJavaType> importedTypes) {
        StringBuilder sb = new StringBuilder();
        Set<String> importStrings = new TreeSet();
        Iterator var3 = importedTypes.iterator();

        while(var3.hasNext()) {
            FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType)var3.next();
            Iterator var5 = fqjt.getImportList().iterator();

            while(var5.hasNext()) {
                String importString = (String)var5.next();
                sb.setLength(0);
                sb.append("import ");
                sb.append(importString);
                sb.append(';');
                importStrings.add(sb.toString());
            }
        }

        return importStrings;
    }

    static {
        String ls = System.getProperty("line.separator");
        if (ls == null) {
            ls = "\n";
        }

        lineSeparator = ls;
    }
}
