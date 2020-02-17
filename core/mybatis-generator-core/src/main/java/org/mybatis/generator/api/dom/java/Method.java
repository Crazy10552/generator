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
package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.OutputUtilities;

import java.util.*;

public class Method extends JavaElement {

    private List<String> bodyLines = new ArrayList<>();

    private boolean constructor;

    private FullyQualifiedJavaType returnType;

    private String name;

    private List<TypeParameter> typeParameters = new ArrayList<>();

    private List<Parameter> parameters = new ArrayList<>();

    private List<FullyQualifiedJavaType> exceptions = new ArrayList<>();

    private boolean isSynchronized;

    private boolean isNative;

    private boolean isDefault;

    private boolean isAbstract;
    
    private boolean isFinal;

    public Method() {
    }

    public Method(String name) {
        this.name = name;
    }

    /**
     * Copy constructor. Not a truly deep copy, but close enough for most purposes.
     *
     * @param original
     *            the original
     */
    public Method(Method original) {
        super(original);
        this.bodyLines.addAll(original.bodyLines);
        this.constructor = original.constructor;
        this.exceptions.addAll(original.exceptions);
        this.name = original.name;
        this.typeParameters.addAll(original.typeParameters);
        this.parameters.addAll(original.parameters);
        this.returnType = original.returnType;
        this.isNative = original.isNative;
        this.isSynchronized = original.isSynchronized;
        this.isDefault = original.isDefault;
        this.isAbstract = original.isAbstract;
        this.isFinal = original.isFinal;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void addBodyLine(String line) {
        bodyLines.add(line);
    }

    public void addBodyLine(int index, String line) {
        bodyLines.add(index, line);
    }

    public void addBodyLines(Collection<String> lines) {
        bodyLines.addAll(lines);
    }

    public void addBodyLines(int index, Collection<String> lines) {
        bodyLines.addAll(index, lines);
    }

    public boolean isConstructor() {
        return constructor;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void addTypeParameter(TypeParameter typeParameter) {
        typeParameters.add(typeParameter);
    }

    public void addTypeParameter(int index, TypeParameter typeParameter) {
        typeParameters.add(index, typeParameter);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    public void addParameter(int index, Parameter parameter) {
        parameters.add(index, parameter);
    }

    public Optional<FullyQualifiedJavaType> getReturnType() {
        return Optional.ofNullable(returnType);
    }

    public void setReturnType(FullyQualifiedJavaType returnType) {
        this.returnType = returnType;
    }

    public List<FullyQualifiedJavaType> getExceptions() {
        return exceptions;
    }

    public void addException(FullyQualifiedJavaType exception) {
        exceptions.add(exception);
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    public boolean isNative() {
        return isNative;
    }

    public void setNative(boolean isNative) {
        this.isNative = isNative;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public String getFormattedContent(int indentLevel, boolean interfaceMethod, CompilationUnit compilationUnit){
         StringBuilder sb = new StringBuilder();
        this.addFormattedJavadoc(sb, indentLevel);
        this.addFormattedAnnotations(sb, indentLevel);
        OutputUtilities.javaIndent(sb, indentLevel);
        if (interfaceMethod) {
            if (this.isStatic()) {
                sb.append("static ");
            } else if (this.isDefault()) {
                sb.append("default ");
            }
        } else {
            sb.append(this.getVisibility().getValue());
            if (this.isStatic()) {
                sb.append("static ");
            }

            if (this.isFinal()) {
                sb.append("final ");
            }

            if (this.isSynchronized()) {
                sb.append("synchronized ");
            }

            if (this.isNative()) {
                sb.append("native ");
            } else if (this.bodyLines.size() == 0) {
                sb.append("abstract ");
            }
        }

        boolean comma;
        Iterator var6;
        if (!this.getTypeParameters().isEmpty()) {
            sb.append("<");
            comma = false;

            TypeParameter typeParameter;
            for(var6 = this.getTypeParameters().iterator(); var6.hasNext(); sb.append(typeParameter.getFormattedContent(compilationUnit))) {
                typeParameter = (TypeParameter)var6.next();
                if (comma) {
                    sb.append(", ");
                } else {
                    comma = true;
                }
            }

            sb.append("> ");
        }

        if (!this.constructor) {
            if (this.getReturnType() == null) {
                sb.append("void");
            } else {
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, this.getReturnType().get()));
            }

            sb.append(' ');
        }

        sb.append(this.getName());
        sb.append('(');
        comma = false;

        Parameter parameter;
        for(var6 = this.getParameters().iterator(); var6.hasNext(); sb.append(parameter.getFormattedContent(compilationUnit))) {
            parameter = (Parameter)var6.next();
            if (comma) {
                sb.append(", ");
            } else {
                comma = true;
            }
        }

        sb.append(')');
        if (this.getExceptions().size() > 0) {
            sb.append(" throws ");
            comma = false;

            FullyQualifiedJavaType fqjt;
            for(var6 = this.getExceptions().iterator(); var6.hasNext(); sb.append(JavaDomUtils.calculateTypeName(compilationUnit, fqjt))) {
                fqjt = (FullyQualifiedJavaType)var6.next();
                if (comma) {
                    sb.append(", ");
                } else {
                    comma = true;
                }
            }
        }

        if (this.bodyLines.size() != 0 && !this.isNative()) {
            sb.append(" {");
            ++indentLevel;
            ListIterator listIter = this.bodyLines.listIterator();

            while(listIter.hasNext()) {
                String line = (String)listIter.next();
                if (line.startsWith("}")) {
                    --indentLevel;
                }

                OutputUtilities.newLine(sb);
                OutputUtilities.javaIndent(sb, indentLevel);
                sb.append(line);
                if (line.endsWith("{") && !line.startsWith("switch") || line.endsWith(":")) {
                    ++indentLevel;
                }

                if (line.startsWith("break")) {
                    if (listIter.hasNext()) {
                        String nextLine = (String)listIter.next();
                        if (nextLine.startsWith("}")) {
                            ++indentLevel;
                        }

                        listIter.previous();
                    }

                    --indentLevel;
                }
            }

            --indentLevel;
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append('}');
        } else {
            sb.append(';');
        }

        return sb.toString();
    }
}
